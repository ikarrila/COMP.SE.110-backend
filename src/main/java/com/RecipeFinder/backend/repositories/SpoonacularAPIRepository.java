package com.RecipeFinder.backend.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import com.RecipeFinder.backend.models.Recipe;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class SpoonacularAPIRepository {


    private final String apiUrl = "https://api.spoonacular.com/recipes/complexSearch";
    private final String apiKey = "4b2cdc4ff73546b89cc40882c81c8e9c";
        
    private List<Integer> recipeIds = new ArrayList<>();
      
    public void applyIngredientFilter(List<String> ingredients) {
    RestTemplate restTemplate = new RestTemplate();
        //Creating comma separated string
    String includeIngrdients = ingredients.stream().collect(Collectors.joining(","));
    String url = apiUrl + "?apiKey=" + apiKey + "&query=pasta" + "&number=3" + "&includeIngredients=" + includeIngrdients;
    
    try {
          String response = restTemplate.getForObject(url, String.class);
          System.out.println("API Response: " + response);
    
          //Parsing
          ObjectMapper mapper = new ObjectMapper();
          JsonNode root = mapper.readTree(response);
          JsonNode results = root.path("results");
    
          recipeIds.clear();
    
          for (JsonNode result : results) {
            int id = result.path("id").asInt();
            recipeIds.add(id);
            System.out.println("Recipe ID: " + id);
          }
    
        } catch (Exception e) {
                e.printStackTrace();
            }
      }
    
      private final String infoUrl = "https://api.spoonacular.com/recipes/{id}/information";
      public void getRecipeInformation(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        try {
          String url = infoUrl.replace("{id}", id.toString()) + "?apiKey=" + apiKey;
          String response = restTemplate.getForObject(url, String.class);
          System.out.println("Recipe Information for ID " + id + ": " + response);
        } catch (Exception e) {
          e.printStackTrace();
        }
    
      }
    
      public String extractItemName(String item) {
        String[] parts = item.split(" ", 2);
        //For error handling
        if (parts.length > 1) {
          return parts[1].toLowerCase();
        }
        return "";
    }
   
}
    
