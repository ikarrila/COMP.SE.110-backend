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
    //Mealplan filter is hardcoded in the repository for testing purposes
    @GetMapping("/search/user/mealplan")
    public List<Recipe> getMealplanRecipes(
        @RequestParam(value = "ingredients", required = false) List<String> ingredients
    ) {
        return recipeService.getMealplanRecipes(ingredients);
    }
    
    //Get all the information abot the recipe with id
    @GetMapping("/{id}")
    public String getRecipeInformation(@PathVariable("id") Integer id) {
        return recipeService.getRecipeInformation(id);
    }

    //Get all the active filters affecting the recipe search
    @GetMapping("/active-filters")
    public JsonNode getActiveFilters() {     
        return recipeService.getActiveFilters();
    }
}
