package com.RecipeFinder.backend.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.models.User;
import com.RecipeFinder.backend.models.RecipeFilter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.net.URL;
import java.net.URLDecoder;

@Repository
public class SpoonacularAPIRepository {

    private final String apiUrl = "https://api.spoonacular.com/recipes/complexSearch";
    private final String apiKey = "4b2cdc4ff73546b89cc40882c81c8e9c";
    /**
     * Searches for recipes that include specified ingredients.
     *
     * @param ingredients List of ingredients to include in search results.
     * @return List of Recipe objects matching the criteria.
     *
     * Constructs an API request with specified ingredients to retrieve recipes.
     * The method leverages Builder Pattern concepts, dynamically constructing
     * URL parameters based on input. The Facade pattern is evident in its
     * interface, allowing the service layer to fetch recipes without
     * concern for the underlying API specifics.
     */ 
    
     public String urlBuilder(List<String> ingredients) {
        String includeIngredients = ingredients.stream().collect(Collectors.joining(","));
        String url = apiUrl + "?apiKey=" + apiKey + "&number=10" + "&includeIngredients=" + includeIngredients;
        System.out.println(url);
        return url;
     } 
    
     

    public List<Recipe> returnRecipes(String url) {
    RestTemplate restTemplate = new RestTemplate();

    //Creating comma separated string
    //String includeIngrdients = ingredients.stream().collect(Collectors.joining(","));
    //String url = apiUrl + "?apiKey=" + apiKey + "&number=10" + "&includeIngredients=" + includeIngrdients;
    
    //System.out.println(url);

    //String userFilteredUrl = applyUserFilters(url, user);
    //String mealplanFilteredUrl = applyMealplanFilters(userFilteredUrl, recipeFilter);
    
    //System.out.println(userFilteredUrl);
    //System.out.println(mealplanFilteredUrl);

    try {
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("API Response: " + response);

        //Parse the API response to extract recipe details
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);
        JsonNode results = root.path("results");

        List<Recipe> recipes = new ArrayList<>();    

    
        for (JsonNode result : results) {
            Recipe recipe = new Recipe();
            recipe.setId(result.path("id").asLong());
            recipe.setName(result.path("title").asText());
            recipes.add(recipe);
            System.out.println("Recipe ID: " + recipe.getId() + "Name: " + recipe.getName());
        }

        return recipes;

    } catch (Exception e) {
            e.printStackTrace();
    }
    return new ArrayList<>();
    }
    
    private final String infoUrl = "https://api.spoonacular.com/recipes/{id}/information";

    /**
     * Retrieves detailed information about a specific recipe by ID.
     *
     * @param id Unique identifier for the recipe.
     * @return JSON response string with recipe information.
     *
     * Constructs a URL for fetching detailed recipe information and parses the
     * result. This method encapsulates the complexity of API interaction.
     */
    public String getRecipeInformation(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = infoUrl.replace("{id}", id.toString()) + "?apiKey=" + apiKey;
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Recipe Information for ID " + id + ": " + response);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
    }
    return null;
    }
   /**
     * Extracts the item name from a given string after the first space.
     *
     * @param item Input string with an item number and name.
     * @return Extracted item name in lowercase.
     *
     * This helper method enhances the robustness of text parsing by handling
     * input format variation, aiming to improve error resilience.
     */
    public String extractItemName(String item) {
    String[] parts = item.split(" ", 2);
    //For error handling
    if (parts.length > 1) {
        return parts[1].toLowerCase();
    }
    return "";
    }

    //Applies the parameters in the API based on the user filters
    public String applyUserFilters(String url, User user) {
        if (user.getDiet() != null && !user.getDiet().isEmpty()) {
            url += "&diet=" + user.getDiet();
        }

        if (user.getFavouriteCuisine() != null && !user.getFavouriteCuisine().isEmpty()) {
            url += "&cuisine=" + user.getFavouriteCuisine();
        }

        if (user.getAllergies() != null && !user.getAllergies().isEmpty()) {
            String includeAllergies = user.getAllergies().stream().collect(Collectors.joining(","));
            url += "&excludeIngredients=" + includeAllergies;
        }
        System.out.println(url);
        return url;
    }

    //Applies the parameters in the API based on the frontend selections
    public String applyMealplanFilters(String url, RecipeFilter recipeFilter) {
        if (recipeFilter.getCuisine() != null && !recipeFilter.getCuisine().isEmpty()) {
            if (url.contains("&cuisine=")) {
                url = url.replace("&cuisine=", "&cuisine=" + recipeFilter.getCuisine() + ",");
            } else {
                url += "&cuisine=" + recipeFilter.getCuisine();
            }
        }
        if (recipeFilter.isDairyFree()) {
            url += "&intolerances=dairy";
        }

        if (recipeFilter.isGlutenFree()) {
            url += "&intolerances=dairy";
        }
   
        if (recipeFilter.isCaloriesUsed()) {
            url += "&minCalories=" + recipeFilter.getMinCalories();
            url += "&maxCalories=" + recipeFilter.getMaxCalories();
        }

        if (recipeFilter.isProteinUsed()) {
            url += "&minProtein=" + recipeFilter.getMinProtein();
            url += "&maxProtein=" + recipeFilter.getMaxProtein();
        }

        if (recipeFilter.isCarbsUsed()) {
            url += "&minCarbs=" + recipeFilter.getMinCarbs();
            url += "&maxCarbs=" + recipeFilter.getMaxCarbs();
        } 
        System.out.println(url);
        return url;
    }
 
    //Placeholder recipe filter in order to search recipes based on the mealplan filters
    public RecipeFilter createTestRecipeFilter() {
        RecipeFilter recipeFilter = new RecipeFilter();
        recipeFilter.setCuisine("italian");
        recipeFilter.setDairyFree(false);
        recipeFilter.setGlutenFree(false);
        recipeFilter.setCaloriesUsed(false);
        recipeFilter.setProteinUsed(false);
        recipeFilter.setCarbsUsed(false);

        return recipeFilter;
        
    }

    public String getActiveFilters(String url) {
        Map<String, String> filters = new HashMap<>();
        try {
            String query = new URL(url).getQuery();
            if (query != null && !query.isEmpty()) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=", 2);
                    String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    String value = keyValue.length > 1
                            ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                            : "";
    
                    if (!key.equalsIgnoreCase("apiKey")) {
                        filters.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return "{}"; // Return empty JSON if an error occurs

    }
    
}

    
