package com.RecipeFinder.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.repositories.SpoonacularAPIRepository;
import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private SpoonacularAPIRepository SpoonacularAPIRepository;

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
    public List<Recipe> applyIngredientFilter(List<String> ingredients) {
        return SpoonacularAPIRepository.applyIngredientFilter(ingredients);
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


