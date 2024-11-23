package com.RecipeFinder.backend.controllers;

import com.RecipeFinder.backend.services.RecipeService;
import com.RecipeFinder.backend.models.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    //Search recipes based on ingredients
    @GetMapping("/search")
    public List<Recipe> getIngredientRecipes(
        @RequestParam(value = "ingredients", required = false) List<String> ingredients
    ) {
        return recipeService.getIngredientRecipes(ingredients);
    }
    
    //Search recipes based on the ingredients and the user (default user is the userid 1)
    @GetMapping("/search/user")
    public List<Recipe> getUserRecipes(
        @RequestParam(value = "ingredients", required = false) List<String> ingredients
    ) {
        return recipeService.getUserRecipes(ingredients);
    }
    
    //Search recipes based on the ingrdient, the user (user id 1) and the mealplan filter
    //Mealplan filter is hardcoded in the repository
    @GetMapping("/search/user/mealplan")
    public List<Recipe> getMealplanRecipes(
        @RequestParam(value = "ingredients", required = false) List<String> ingredients
    ) {
        return recipeService.getMealplanRecipes(ingredients);
    }
    
    @GetMapping("/{id}")
    public String getRecipeInformation(@PathVariable("id") Integer id) {
        return recipeService.getRecipeInformation(id);
    }

    @GetMapping("/active-filters")
    public JsonNode getActiveFilters() {
        //TODO: recipeFitler mode to jsonode
        
        return recipeService.getActiveFilters();
    }
}
