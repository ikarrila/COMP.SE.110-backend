package com.RecipeFinder.backend.services;

import com.RecipeFinder.backend.models.PriceData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PriceDataService {

  private final String apiUrl = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/khi/statfin_khi_pxt_11xb.px";

  public PriceData fetchPriceData() {
    RestTemplate restTemplate = new RestTemplate();
    String requestJson = """
        {
          "query": [
            {
              "code": "Hyödyke",
              "selection": {
                "filter": "item",
                "values": [
                  "01111",
                  "01112",
                  "01113",
                  "01114",
                  "01115",
                  "01116",
                  "01117",
                  "01118",
                  "01121",
                  "01122",
                  "01124",
                  "01125",
                  "01127",
                  "01128",
                  "01131",
                  "01132",
                  "01135",
                  "01136",
                  "01142",
                  "01144",
                  "01145",
                  "01146",
                  "01147",
                  "01151",
                  "01152",
                  "01153",
                  "01154",
                  "01161",
                  "01162",
                  "01163",
                  "01164",
                  "01171",
                  "01172",
                  "01173",
                  "01174",
                  "01175",
                  "01181",
                  "01182",
                  "01183",
                  "01184",
                  "01185",
                  "01191",
                  "01192",
                  "01193",
                  "01194",
                  "01199"
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
      String response = restTemplate.postForObject(apiUrl, requestJson, String.class);
      return parsePriceData(response);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private PriceData parsePriceData(String jsonResponse) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(jsonResponse);

    PriceData priceData = new PriceData();
    
    // Parse categories (months)
    List<String> categories = new ArrayList<>();
    JsonNode monthsNode = root.path("dimension").path("Kuukausi").path("category").path("label");
    monthsNode.fields().forEachRemaining(entry -> categories.add(entry.getValue().asText()));
    priceData.setCategories(categories);

    // Parse series data
    List<PriceData.Series> seriesList = new ArrayList<>();
    JsonNode commodityNode = root.path("dimension").path("Hyödyke").path("category").path("label");
    JsonNode valuesNode = root.path("value");

    int numMonths = categories.size();
    // int numCommodities = commodityNode.size();

    AtomicInteger index = new AtomicInteger(0);
    commodityNode.fields().forEachRemaining(entry -> {
      PriceData.Series series = new PriceData.Series();
      series.setName(entry.getValue().asText());
      
      List<Double> data = new ArrayList<>();
      int commodityIndex = index.getAndIncrement();
      for (int i = 0; i < numMonths; i++) {
        int valueIndex = i + commodityIndex * numMonths;
        data.add(valuesNode.get(valueIndex).asDouble());
      }
      series.setData(data);
      
      seriesList.add(series);
    });
    priceData.setSeries(seriesList);

    return priceData;
  }
}
