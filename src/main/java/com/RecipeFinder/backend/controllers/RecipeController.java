package com.RecipeFinder.backend.controllers;

import com.RecipeFinder.backend.services.RecipeService;
import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.repositories.RecipeRepository;
import com.RecipeFinder.backend.repositories.SpoonacularAPIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;


    @GetMapping("/search")
    public List<Recipe> applyIngredientFilter(
        @RequestParam(value = "ingredients", required = false) List<String> ingredients
    ) {
        return recipeService.applyIngredientFilter(ingredients);
    }

    @GetMapping("/{id}")
    public String getRecipeInformation(@PathVariable("id") Integer id) {
        return recipeService.getRecipeInformation(id);
    }
}
