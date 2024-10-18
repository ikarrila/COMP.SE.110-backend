package com.RecipeFinder.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.repositories.RecipeRepository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService {
    private final String apiUrl = "https://api.spoonacular.com/recipes/complexSearch";
    private final String apiKey = "4b2cdc4ff73546b89cc40882c81c8e9c";
    
  private List<Integer> recipeIds = new ArrayList<>();
  
  //TODO: new function
  //applyFoodItemFilter
  //ottaa food item listan, includeIngredients, tallentaan recipeIdt, käyttää syötteenä dummy listaa

    public void fetchRecipes() {
        RestTemplate restTemplate = new RestTemplate();
        //Exceliin kaikki filtterit
        String url = apiUrl + "?apiKey=" + apiKey + "&query=pasta" + "&number=2";
        
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

    //UUSI URL
    private final String infoUrl = "https://api.spoonacular.com/recipes/{id}/information";
    public void fetchRecipeInformation() {
      RestTemplate restTemplate = new RestTemplate();
      try {
        for (Integer id : recipeIds) {
          String url = infoUrl.replace("{id}", id.toString()) + "?apiKey=" + apiKey;
          String response = restTemplate.getForObject(url, String.class);
          System.out.println("Recipe Information for ID " + id + ": " + response);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }

    }

  }
