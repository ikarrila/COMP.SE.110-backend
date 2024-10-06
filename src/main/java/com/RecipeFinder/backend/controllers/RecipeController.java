package com.RecipeFinder.backend.controllers;

import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @PostMapping
    public String saveRecipe(@RequestBody Recipe recipe) throws IOException {
        recipeRepository.saveRecipe(recipe);
        return "Recipe saved!";
    }

    @GetMapping
    public List<Recipe> getAllRecipes() throws IOException {
        return recipeRepository.loadAllRecipes();
    }
}
