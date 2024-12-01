package com.RecipeFinder.backend.controllers;

import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIngredientRecipes() {
        List<String> ingredients = List.of("tomato", "potato");
        Recipe mockRecipe = new Recipe();
        List<Recipe> mockRecipes = List.of(mockRecipe);

        when(recipeService.getIngredientRecipes(ingredients)).thenReturn(mockRecipes);

        List<Recipe> response = recipeController.getIngredientRecipes(ingredients);
        assertEquals(1, response.size());
        verify(recipeService, times(1)).getIngredientRecipes(ingredients);
    }

    @Test
    void testGetUserRecipes() {
        List<String> ingredients = List.of("chicken", "garlic");
        Recipe mockRecipe = new Recipe();
        List<Recipe> mockRecipes = List.of(mockRecipe);

        when(recipeService.getUserRecipes(ingredients)).thenReturn(mockRecipes);

        List<Recipe> response = recipeController.getUserRecipes(ingredients);
        assertEquals(1, response.size());
        verify(recipeService, times(1)).getUserRecipes(ingredients);
    }

    @Test
    void testGetMealplanRecipes() {
        List<String> ingredients = List.of("onion", "pepper");
        Recipe mockRecipe = new Recipe();
        List<Recipe> mockRecipes = List.of(mockRecipe);

        when(recipeService.getMealplanRecipes(ingredients)).thenReturn(mockRecipes);

        List<Recipe> response = recipeController.getMealplanRecipes(ingredients);
        assertEquals(1, response.size());
        verify(recipeService, times(1)).getMealplanRecipes(ingredients);
    }

    @Test
    void testGetActiveFilters() {
        JsonNode mockFilters = mock(JsonNode.class);

        when(recipeService.getActiveFilters()).thenReturn(mockFilters);

        JsonNode response = recipeController.getActiveFilters();
        assertEquals(mockFilters, response);
        verify(recipeService, times(1)).getActiveFilters();
    }


    @Test
    void testGetRecipeInformation() {
        Integer recipeId = 123;
        String mockRecipeInfo = "Mock Recipe Information";

        when(recipeService.getRecipeInformation(recipeId)).thenReturn(mockRecipeInfo);

        String response = recipeController.getRecipeInformation(recipeId);
        assertEquals(mockRecipeInfo, response);
        verify(recipeService, times(1)).getRecipeInformation(recipeId);
    }
}
