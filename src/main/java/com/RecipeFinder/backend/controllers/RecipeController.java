package com.RecipeFinder.backend.controllers;

import com.RecipeFinder.backend.services.RecipeService;
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
    private RecipeService recipeService;


    @GetMapping
    public void fetchRecipes() {
        recipeService.fetchRecipes();
    }
}
