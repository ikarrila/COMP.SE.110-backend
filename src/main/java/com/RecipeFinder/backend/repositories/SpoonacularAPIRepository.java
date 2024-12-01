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
     * Builds the base url for the API calls.
     * @param ingredients List of ingredients that the recipe search will be based on
     * @return The base url that is used for the API call
     */
     public String urlBuilder(List<String> ingredients) {
        String includeIngredients = ingredients.stream().collect(Collectors.joining(","));
        String url = apiUrl + "?apiKey=" + apiKey + "&number=10" + "&includeIngredients=" + includeIngredients;
        System.out.println(url);
        return url;
     } 
    
     
    /**
     * Returns the list of recipes based on the filters
     * @param url The url for the API call
     * @return List of Recipe models
     */
    public List<Recipe> returnRecipes(String url) {
    RestTemplate restTemplate = new RestTemplate();

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

    /**
     * Applies filters to the API call for recipe search based on the settings
     * in the user profile
     * @param url Input string containing the base API call
     * @param user Selected user model where settings are fetched
     * @return The updated API call with user filters
     */
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

    /**
     * Applies the mealplan filters from the RecipeFilter model
     * @param url The base url for the API call containing user and ingeredient filters
     * @param recipeFilter The filters set by the user in the frontend
     * @return The updated url for the API call with all the filters in place
     */
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
 
    /**
     * Hardcoded RecipeFilter model for backend testing and demo purposes
     * @return RecipeFilter model used for additional filtering criteria
     */
    public RecipeFilter createTestRecipeFilter() {
        RecipeFilter recipeFilter = new RecipeFilter();
        recipeFilter.setCuisine("italian");
        recipeFilter.setDiet("vegetarian");
        recipeFilter.setDairyFree(false);
        recipeFilter.setGlutenFree(false);
        recipeFilter.setCaloriesUsed(false);
        recipeFilter.setProteinUsed(false);
        recipeFilter.setCarbsUsed(false);

        return recipeFilter;
       
    }

    /**
     * Combines the filters coming from the price data API, user model and mealplan filter
     * for active-filters endpoint without sending API call
     * @param recipeFilter Mealplan filters set by the user in the frontend
     * @param user The user filters coming from the user profile
     * @param ingredients Ingredients selected from the price data API
     * @return RecipeFilter model containing all the filters
     */
    public RecipeFilter combineFilters(RecipeFilter recipeFilter, User user, List<String> ingredients) {
        RecipeFilter combinedFilter = new RecipeFilter();

        combinedFilter.setCuisine(recipeFilter.getCuisine());
        combinedFilter.setDiet(recipeFilter.getDiet());
        combinedFilter.setDairyFree(recipeFilter.isDairyFree());
        combinedFilter.setGlutenFree(recipeFilter.isGlutenFree());
        combinedFilter.setCaloriesUsed(recipeFilter.isCaloriesUsed());
        combinedFilter.setMinCalories(recipeFilter.getMinCalories());
        combinedFilter.setMaxCalories(recipeFilter.getMaxCalories());
        combinedFilter.setProteinUsed(recipeFilter.isProteinUsed());
        combinedFilter.setMinProtein(recipeFilter.getMinProtein());
        combinedFilter.setMaxProtein(recipeFilter.getMaxProtein());
        combinedFilter.setCarbsUsed(recipeFilter.isCarbsUsed());
        combinedFilter.setMinCarbs(recipeFilter.getMinCarbs());
        combinedFilter.setMaxCarbs(recipeFilter.getMaxCarbs());
        combinedFilter.setExcludeIngredients(recipeFilter.getExcludeIngredients());
        combinedFilter.setIncludeIngredients(recipeFilter.getIncludeIngredients());


        if (user.getDiet() != null && !user.getDiet().isEmpty()) {
            combinedFilter.setDiet(combinedFilter.getDiet() == null
                ? user.getDiet()
                : combinedFilter.getDiet() + ", " + user.getDiet());
        }
    
        if (user.getFavouriteCuisine() != null && !user.getFavouriteCuisine().isEmpty()) {
            combinedFilter.setCuisine(combinedFilter.getCuisine() == null
                ? user.getFavouriteCuisine()
                : combinedFilter.getCuisine() + ", " + user.getFavouriteCuisine());
        }
    
        List<String> combinedExcludedIngredients = new ArrayList<>();
        if (recipeFilter.getExcludeIngredients() != null) {
            combinedExcludedIngredients.addAll(recipeFilter.getExcludeIngredients());
        }
        if (user.getAllergies() != null) {
            combinedExcludedIngredients.addAll(user.getAllergies());
        } combinedFilter.setExcludeIngredients(
            combinedExcludedIngredients.stream()
                .distinct()
                .collect(Collectors.toList())
        );

        List<String> combinedIncludedIngredients = new ArrayList<>();
        if (recipeFilter.getIncludeIngredients() != null) {
            combinedIncludedIngredients.addAll(recipeFilter.getIncludeIngredients());
        }
        if (ingredients != null) {
            combinedIncludedIngredients.addAll(ingredients);
        }
        combinedFilter.setIncludeIngredients(
            combinedIncludedIngredients.stream().distinct().collect(Collectors.toList())
        );  

        return combinedFilter;
    
    }

}

    
