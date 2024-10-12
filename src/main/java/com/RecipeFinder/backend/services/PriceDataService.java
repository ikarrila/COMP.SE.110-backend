package com.RecipeFinder.backend.services;

import com.RecipeFinder.backend.models.PriceDataQuery;
import com.RecipeFinder.backend.repositories.PriceDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class PriceDataService {

  private final String apiUrl = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/khi/statfin_khi_pxt_11xb.px";

  @Autowired
  private PriceDataRepository priceDataRepository;

  // TODO: Remove
  public void fetchAndPrintPriceData() {
    RestTemplate restTemplate = new RestTemplate();

    // Build the query object
    PriceDataQuery query = buildQuery();

    // Convert the query object to JSON
    String requestJson = convertQueryToJson(query);

    try {
      // Test print
      String response = restTemplate.postForObject(apiUrl, requestJson, String.class);
      System.out.println("API Response: " + response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private PriceDataQuery buildQuery() {
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

  private String convertQueryToJson(PriceDataQuery query) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(query);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}