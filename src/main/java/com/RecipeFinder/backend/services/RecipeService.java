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

    // Service method to the recipes with the ingredients
    public List<Recipe> applyIngredientFilter(List<String> ingredients) {
        return SpoonacularAPIRepository.applyIngredientFilter(ingredients);
    }

    // Service method to get recipe informatio with recipe id
    public String getRecipeInformation(Integer id) {
        return SpoonacularAPIRepository.getRecipeInformation(id);
    }
}


