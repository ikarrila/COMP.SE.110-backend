package com.RecipeFinder.backend.repositories;

import com.RecipeFinder.backend.models.PriceData;
import com.RecipeFinder.backend.models.PriceDataQuery;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PriceDataRepository {

    private final String apiUrl = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/khi/statfin_khi_pxt_11xb.px";
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Retrieves all food price data with a predefined set of food item codes.
     *
     * @return PriceData object containing all food price data.
     *
     * This method employs the Template Method pattern by following a fixed sequence:
     * constructing a request, sending it, and parsing the response. It also applies
     * the Builder Pattern in constructing JSON queries, making the query setup
     * reusable and scalable.
     */
    public PriceData getAllFoodPriceData() {
        String predefinedRequestJson = highLevelItems();
        System.out.println("Sending predefined query to API: " + predefinedRequestJson);

        // Send request to API and get response
        String response = restTemplate.postForObject(apiUrl, predefinedRequestJson, String.class);
        System.out.println("API Response: " + response);

        return parseResponse(response);
    }

    /**
     * Retrieves filtered food price data based on optional start and end months and a list of commodities.
     *
     * @param startMonth Optional start month for filtering data.
     * @param endMonth Optional end month for filtering data.
     * @param commodities List of commodity codes to filter the data by.
     * @return PriceData object with filtered data.
     *
     * By building queries dynamically, this method uses the Builder Pattern
     * to construct JSON requests flexibly based on provided parameters.
     * This modular approach allows for scalable query configurations, making
     * it straightforward to add more filtering criteria in the future.
     */
    public PriceData getFilteredPriceData(String startMonth, String endMonth, List<String> commodities) {
        PriceDataQuery query = buildQueryWithOptionalMonths(startMonth, endMonth, commodities);
        String requestJson = convertQueryToJson(query);

        System.out.println("Sending filtered query to API: " + requestJson);

        // Send request to API and get response
        String response = restTemplate.postForObject(apiUrl, requestJson, String.class);
        return parseResponse(response);
    }

    /**
     * Builds a query for filtering by optional months and commodities.
     *
     * @param startMonth Optional start month.
     * @param endMonth Optional end month.
     * @param commodities List of commodity codes to include.
     * @return PriceDataQuery object.
     *
     * This method applies the Builder Pattern for dynamic query generation,
     * allowing the client to specify flexible parameters for more targeted queries.
     */
    private PriceDataQuery buildQueryWithOptionalMonths(String startMonth, String endMonth, List<String> commodities) {
        List<String> months = new ArrayList<>();

        // If both start and end months are provided, generate the range of months
        if (startMonth != null && endMonth != null) {
            months = getMonthRange(startMonth, endMonth); // Helper method to generate month range
        }

        // Query items for months and commodities
        PriceDataQuery.QueryItem monthItem = createQueryItemWithValues("Kuukausi", "item", months.isEmpty() ? List.of("all") : months);
        PriceDataQuery.QueryItem commodityItem = createQueryItemWithValues("Hyödyke", "item", commodities);
        PriceDataQuery.QueryItem infoItem = createQueryItemWithValues("Tiedot", "item", Arrays.asList("indeksipisteluku"));

        // Build the full query
        return createPriceDataQuery(Arrays.asList(monthItem, commodityItem, infoItem), "json-stat2");
    }

    /**
     * Converts a PriceDataQuery object to JSON for API request.
     *
     * @param query PriceDataQuery object.
     * @return JSON string representation of the query.
     *
     * This method encapsulates the object-to-JSON conversion process,
     * following the Adapter Pattern to enable seamless interaction
     * between the Java object model and JSON-based API.
     */
    private String convertQueryToJson(PriceDataQuery query) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(query);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parses the API response into a PriceData object.
     *
     * @param response JSON response from API.
     * @return PriceData object.
     *
     * The parsing logic demonstrates the Factory Pattern, constructing
     * and populating `PriceData` and `PriceData.Series` objects based on
     * JSON input. This separation of parsing logic facilitates testability
     * and reusability across different API responses.
     */
    private PriceData parseResponse(String response) {
        PriceData priceData = new PriceData();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode values = rootNode.path("value");
            JsonNode monthsNode = rootNode.path("dimension").path("Kuukausi").path("category").path("label");
            JsonNode commoditiesNode = rootNode.path("dimension").path("Hyödyke").path("category").path("label");

            // Parse categories (months)
            List<String> categories = new ArrayList<>();
            monthsNode.fields().forEachRemaining(entry -> categories.add(entry.getValue().asText()));
            priceData.setCategories(categories);

            // Parse series data (commodities and their values across months)
            List<PriceData.Series> seriesList = new ArrayList<>();
            int numMonths = categories.size();
            AtomicInteger index = new AtomicInteger(0);

            commoditiesNode.fields().forEachRemaining(entry -> {
                PriceData.Series series = new PriceData.Series();
                series.setName(entry.getValue().asText());

                List<Double> data = new ArrayList<>();
                int commodityIndex = index.getAndIncrement();
                for (int i = 0; i < numMonths; i++) {
                    int valueIndex = i + commodityIndex * numMonths;
                    JsonNode valueNode = values.get(valueIndex);
                    // Check if the value is null, some API responses return null or missing data
                    data.add(valueNode.isNull() ? null : valueNode.asDouble());
                }
                series.setData(data);
                seriesList.add(series);
            });

            priceData.setSeries(seriesList);

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }

    return priceData;
}

    // Helper method to create a PriceDataQuery.QueryItem with specific values
    private PriceDataQuery.QueryItem createQueryItemWithValues(String code, String filter, List<String> values) {
        PriceDataQuery.Selection selection = new PriceDataQuery.Selection();
        selection.setFilter(filter);
        selection.setValues(values);

        PriceDataQuery.QueryItem queryItem = new PriceDataQuery.QueryItem();
        queryItem.setCode(code);
        queryItem.setSelection(selection);

        return queryItem;
    }

    // Helper method to create a PriceDataQuery object
    private PriceDataQuery createPriceDataQuery(List<PriceDataQuery.QueryItem> queryItems, String responseFormat) {
        PriceDataQuery query = new PriceDataQuery();
        query.setQuery(queryItems);

        PriceDataQuery.ResponseFormat format = new PriceDataQuery.ResponseFormat();
        format.setFormat(responseFormat);
        query.setResponse(format);

        return query;
    }

    // Helper method to generate month range (from startMonth to endMonth)
    private List<String> getMonthRange(String startMonth, String endMonth) {
        List<String> monthRange = new ArrayList<>();
    
        // Extract year and month from the start and end months
        int startYear = Integer.parseInt(startMonth.substring(0, 4));
        int startMonthNumber = Integer.parseInt(startMonth.substring(5));
        int endYear = Integer.parseInt(endMonth.substring(0, 4));
        int endMonthNumber = Integer.parseInt(endMonth.substring(5));
    
        // Iterate from the start month to the end month
        for (int year = startYear; year <= endYear; year++) {
            int monthStart = (year == startYear) ? startMonthNumber : 1;
            int monthEnd = (year == endYear) ? endMonthNumber : 12;
    
            for (int month = monthStart; month <= monthEnd; month++) {
                // Format month as 'YYYYMM' (e.g., '2020M11')
                String formattedMonth = String.format("%04dM%02d", year, month);
                monthRange.add(formattedMonth);
            }
        }
    
        return monthRange;
    }

    // Items to be shown on the dashboard by default
    private String highLevelItems() {
        return """
        {
        "query": [
            {
            "code": "Hyödyke",
            "selection": {
                "filter": "item",
                "values": [
                "0111", "0112", "0113", "0114", "0115", "0116", "0117", "0118", "0119", "0121", "0122"
                ]
            }
            },
            {
            "code": "Tiedot",
            "selection": {
                "filter": "item",
                "values": [
                "indeksipisteluku"
                ]
            }
            }
        ],
        "response": {
            "format": "json-stat2"
        }
        }
        """;
    }

}
