package com.RecipeFinder.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.repositories.RecipeRepository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class RecipeService {
    private final String apiUrl = "https://api.spoonacular.com/recipes/complexSearch";
    private final String apiKey = "4b2cdc4ff73546b89cc40882c81c8e9c";
    
    
    public void fetchRecipes() {
        RestTemplate restTemplate = new RestTemplate();
        String url = apiUrl + "?apiKey=" + apiKey + "&query=pasta" + "&number=2";
        
        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("API Response: " + response);
          } catch (Exception e) {
            e.printStackTrace();
        }
    }

  }
