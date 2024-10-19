package com.RecipeFinder.backend.repositories;

import com.RecipeFinder.backend.models.PriceData;
import com.RecipeFinder.backend.models.PriceDataQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Repository
public class PriceDataRepository {

    private final String apiUrl = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/khi/statfin_khi_pxt_11xb.px";
    private final RestTemplate restTemplate = new RestTemplate();

    // Method to get all food price data
    public List<PriceData> getPriceData() {
        PriceDataQuery query = buildQueryForAllFoodPrices();
        String requestJson = convertQueryToJson(query);

        // Temporary print statement to see the JSON data being sent to the API
        System.out.println("Sending JSON request to API: " + requestJson);

        // Send request to API and get response
        String response = restTemplate.postForObject(apiUrl, requestJson, String.class);

        // Temporary print statement to see the JSON data received from the API
        System.out.println("Received JSON response from API: " + response);

        // Parse response into PriceData objects (for now, return mock data)
        return parseResponse(response);
    }

    // Helper method to build the query for all food prices
    private PriceDataQuery buildQueryForAllFoodPrices() {
        PriceDataQuery.QueryItem item1 = new PriceDataQuery.QueryItem();
        item1.setCode("Hyödyke");
        PriceDataQuery.Selection selection1 = new PriceDataQuery.Selection();
        selection1.setFilter("item");
        selection1.setValues(Arrays.asList("011"));
        item1.setSelection(selection1);

        PriceDataQuery.QueryItem item2 = new PriceDataQuery.QueryItem();
        item2.setCode("Tiedot");
        PriceDataQuery.Selection selection2 = new PriceDataQuery.Selection();
        selection2.setFilter("item");
        selection2.setValues(Arrays.asList("indeksipisteluku"));
        item2.setSelection(selection2);

        PriceDataQuery query = new PriceDataQuery();
        query.setQuery(Arrays.asList(item1, item2));

        PriceDataQuery.ResponseFormat responseFormat = new PriceDataQuery.ResponseFormat();
        responseFormat.setFormat("json-stat2");
        query.setResponse(responseFormat);

        return query;
    }

    // Helper method to convert the query to JSON
    private String convertQueryToJson(PriceDataQuery query) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(query);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to parse the API response into a list of PriceData objects
    private List<PriceData> parseResponse(String response) {
        // For now, return a placeholder empty list. Actual parsing logic will depend on
        // the API response format
        return List.of(); // Replace with actual parsing logic
    }
}
