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

    //Variables for session filter management
    private final ConcurrentHashMap<Integer, RecipeFilter> activeFilters = new ConcurrentHashMap<Integer, RecipeFilter>();
    private final AtomicInteger sessionIdCounter = new AtomicInteger(0);
    private int latestSessionId = -1;

    /**
     * Filters recipes based on ingredients, user preferences and mealplan settings.
     * @param ingredients List of ingredients to filter recipes by
     * @return List of Recipe objects that include the specified ingredients
     * 
     * This method delegates the filtering task to SpoonacularAPIRepository,
     * which communicates with an external API to retrieve recipes based on
     * the provided ingredients, user preferences and mealplan settings. 
     * This service design centralizes business logic for filtering recipes and abstracts 
     * lower-level API interactions.
     */
     public List<Recipe> getMealplanRecipes(List<String> ingredients) {
        User defaultUser = userService.getUserById(1)
                                      .orElseThrow(() -> new RuntimeException("User not found!"));
        
        //Test RecipeFilter model for testing and demo purposes
        RecipeFilter testRecipeFilter = SpoonacularAPIRepository.createTestRecipeFilter();
        String url = SpoonacularAPIRepository.urlBuilder(ingredients);
        String userFilteredUrl = SpoonacularAPIRepository.applyUserFilters(url, defaultUser);
        String mealplanFilteredUrl = SpoonacularAPIRepository.applyMealplanFilters(userFilteredUrl, testRecipeFilter);
        RecipeFilter combined = SpoonacularAPIRepository.combineFilters(testRecipeFilter, defaultUser, ingredients);
        updateActiveFilters(combined);
        System.out.println(getActiveFilters());
        

        return SpoonacularAPIRepository.returnRecipes(mealplanFilteredUrl);
    }

    /**
     * Filters recipes based on ingredients and user preferences.
     * @param ingredients List of ingredients to filter recipes by
     * @return List of Recipe objects that include the specified ingredients
     * 
     * This method delegates the filtering task to SpoonacularAPIRepository,
     * which communicates with an external API to retrieve recipes based on
     * the provided ingredients and user preferences. This service design centralizes business
     * logic for filtering recipes and abstracts lower-level API interactions.
     */
    public List<Recipe> getUserRecipes(List<String> ingredients) {
        User defaultUser = userService.getUserById(1)
                                      .orElseThrow(() -> new RuntimeException("User not found!"));
        String url = SpoonacularAPIRepository.urlBuilder(ingredients);
        String userFilteredUrl = SpoonacularAPIRepository.applyUserFilters(url, defaultUser);
        RecipeFilter emptyFilter = new RecipeFilter();
        RecipeFilter combined = SpoonacularAPIRepository.combineFilters(emptyFilter, defaultUser, ingredients);
        updateActiveFilters(combined);

        return SpoonacularAPIRepository.returnRecipes(userFilteredUrl);
    }

    /**
     * Filters recipes based on ingredients.
     * @param ingredients List of ingredients to filter recipes by
     * @return List of Recipe objects that include the specified ingredients
     * 
     * This method delegates the filtering task to SpoonacularAPIRepository,
     * which communicates with an external API to retrieve recipes based on
     * the provided ingredients. This service design centralizes business
     * logic for filtering recipes and abstracts lower-level API interactions.
     */
    public List<Recipe> getIngredientRecipes(List<String> ingredients) {
        String url = SpoonacularAPIRepository.urlBuilder(ingredients);
        RecipeFilter emptyFilter = new RecipeFilter();
        User emptyUser = new User();
        RecipeFilter combined = SpoonacularAPIRepository.combineFilters(emptyFilter, emptyUser, ingredients);
        updateActiveFilters(combined);

        return SpoonacularAPIRepository.returnRecipes(url);
    }

    /**
     * Updates the ConcurrentHashMap of the session with new active filters
     * when a new api call is made
     * @param recipeFilter RecipeFilter containing the currently active filters
     */
    public void updateActiveFilters(RecipeFilter recipeFilter) {
        int newSessionId = sessionIdCounter.incrementAndGet();
        activeFilters.put(newSessionId, recipeFilter);
        latestSessionId = newSessionId;
    }

    /**
     * Returns the currently active filters from the ConcurrentHashMap as a JsonNode
     * @return JsonNode containing all the information about currently active filters
     */
    public JsonNode getActiveFilters() {
        RecipeFilter filter = activeFilters.getOrDefault(latestSessionId, new RecipeFilter());
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.valueToTree(filter);
        } catch (Exception e) {
            e.printStackTrace();
            return mapper.createObjectNode(); 
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


