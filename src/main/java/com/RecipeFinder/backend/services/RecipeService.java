package com.RecipeFinder.backend.services;

import org.springframework.stereotype.Service;
import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.repositories.RecipeRepository;

import java.io.IOException;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /* Example */
    public Recipe getRecipeById(Long id) {
        try {
            return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the recipe file", e);
        }
    }
}
