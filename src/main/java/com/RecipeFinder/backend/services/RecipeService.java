package com.RecipeFinder.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.RecipeFinder.backend.repositories.SpoonacularAPIRepository;
import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private SpoonacularAPIRepository SpoonacularAPIRepository;

    // Service method to the recipes with the ingredients
    public void applyIngredientFilter(List<String> ingredients) {
      SpoonacularAPIRepository.applyIngredientFilter(ingredients);
    }

    // Service method to get recipe informatio with recipe id
    public void getRecipeInformation(Integer id) {
      SpoonacularAPIRepository.getRecipeInformation(id);
    }
}


