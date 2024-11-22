package com.RecipeFinder.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.models.RecipeFilter;
import com.RecipeFinder.backend.models.User;
import com.RecipeFinder.backend.repositories.SpoonacularAPIRepository;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RecipeService {

    @Autowired
    private SpoonacularAPIRepository SpoonacularAPIRepository;

    @Autowired
    private UserService userService;

    private final ConcurrentHashMap<Integer, RecipeFilter> activeFilters = new ConcurrentHashMap<Integer, RecipeFilter>();

    private final AtomicInteger sessionIdCounter = new AtomicInteger(0);
    private int latestSessionId = -1;


    public RecipeFilter createRecipeFilter() {
        return SpoonacularAPIRepository.createTestRecipeFilter();
    }

    public String urlBuilder(List<String> ingredients) {
        return SpoonacularAPIRepository.urlBuilder(ingredients);
    }


    /**
     * Filters recipes based on specified ingredients.
     *
     * @param ingredients List of ingredients to filter recipes by.
     * @return List of Recipe objects that include the specified ingredients.
     *
     * This method delegates the filtering task to SpoonacularAPIRepository,
     * which communicates with an external API to retrieve recipes based on
     * the provided ingredients. This service design centralizes business
     * logic for filtering recipes and abstracts lower-level API interactions.
     */
    
     public List<Recipe> getMealplanRecipes(List<String> ingredients) {
        User defaultUser = userService.getUserById(1)
                                      .orElseThrow(() -> new RuntimeException("User not found!"));
        
        //Give a test parameter recipeFilter for the repository 
        RecipeFilter testRecipeFilter = createRecipeFilter();
        String url = SpoonacularAPIRepository.urlBuilder(ingredients);
        String userFilteredUrl = SpoonacularAPIRepository.applyUserFilters(url, defaultUser);
        String mealplanFilteredUrl = SpoonacularAPIRepository.applyMealplanFilters(userFilteredUrl, testRecipeFilter);

        updateActiveFilters(testRecipeFilter);
        System.out.println(getActiveFilters());
        

        return SpoonacularAPIRepository.returnRecipes(mealplanFilteredUrl);
    }


    public List<Recipe> getUserRecipes(List<String> ingredients) {
        User defaultUser = userService.getUserById(1)
                                      .orElseThrow(() -> new RuntimeException("User not found!"));
        String url = SpoonacularAPIRepository.urlBuilder(ingredients);
        String userFilteredUrl = SpoonacularAPIRepository.applyUserFilters(url, defaultUser);
        return SpoonacularAPIRepository.returnRecipes(userFilteredUrl);
    }

    public List<Recipe> getIngredientRecipes(List<String> ingredients) {
        String url = SpoonacularAPIRepository.urlBuilder(ingredients);
        return SpoonacularAPIRepository.returnRecipes(url);
    }

    public void updateActiveFilters(RecipeFilter recipeFilter) {
        int newSessionId = sessionIdCounter.incrementAndGet();
        activeFilters.put(newSessionId, recipeFilter);
        latestSessionId = newSessionId;
    }

    public JsonNode getActiveFilters() {
        RecipeFilter filter = activeFilters.getOrDefault(latestSessionId, new RecipeFilter());
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Convert the RecipeFilter object to a JsonNode
            return mapper.valueToTree(filter);
        } catch (Exception e) {
            e.printStackTrace();
            return mapper.createObjectNode(); // Return an empty JSON object in case of an error
        }
    }


    /**
     * Retrieves detailed information for a recipe by its unique ID.
     *
     * @param id Unique identifier for the recipe.
     * @return String JSON response with detailed recipe information.
     *
     * By using SpoonacularAPIRepository, this service method isolates API
     * communication and exposes the core business functionality. This structure
     * follows the Facade pattern, offering a simplified interface for controllers.
     */
    public String getRecipeInformation(Integer id) {
        return SpoonacularAPIRepository.getRecipeInformation(id);
    }

}


