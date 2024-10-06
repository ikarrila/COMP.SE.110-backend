package com.RecipeFinder.backend.services;

import com.RecipeFinder.backend.models.PriceData;
import com.RecipeFinder.backend.repositories.PriceDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PriceDataService {

  private final String apiUrl = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/khi/statfin_khi_pxt_11xb.px";

  public void fetchAndPrintPriceData() {
    RestTemplate restTemplate = new RestTemplate();

    // JSON query for testing the connection
    // TODO: A smart way to manage queries
    String requestJson = """
        {
          "query": [
            {
              "code": "Hyödyke",
              "selection": {
                "filter": "item",
                "values": [
                  "011"
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

    try {
      // Test print
      String response = restTemplate.postForObject(apiUrl, requestJson, String.class);
      System.out.println("API Response: " + response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
