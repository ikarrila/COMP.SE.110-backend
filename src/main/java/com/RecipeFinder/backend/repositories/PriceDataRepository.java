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

    // Service to retrieve all food price data with predefined food item codes
    public PriceData getAllFoodPriceData() {
        String predefinedRequestJson = highLevelItems();
        System.out.println("Sending predefined query to API: " + predefinedRequestJson);

        // Send request to API and get response
        String response = restTemplate.postForObject(apiUrl, predefinedRequestJson, String.class);
        System.out.println("API Response: " + response);

        return parseResponse(response);
    }

    // Service to retrieve food price data with optional filtering by start and end months
    public PriceData getFilteredPriceData(String startMonth, String endMonth, List<String> commodities) {
        PriceDataQuery query = buildQueryWithOptionalMonths(startMonth, endMonth, commodities);
        String requestJson = convertQueryToJson(query);

        System.out.println("Sending filtered query to API: " + requestJson);

        // Send request to API and get response
        String response = restTemplate.postForObject(apiUrl, requestJson, String.class);
        return parseResponse(response);
    }

    // Builds the query with optional start and end months
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

    // Converts the query object to a JSON string
    private String convertQueryToJson(PriceDataQuery query) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(query);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Parses the API response into a list of PriceData objects
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

    // These items are to be shown on the dashboard
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
