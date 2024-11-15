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

class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**@Test
    void testApplyIngredientFilter() {
        List<String> ingredients = List.of("tomato", "cheese");
        Recipe mockRecipe = new Recipe();
        List<Recipe> mockRecipes = List.of(mockRecipe);

        when(recipeService.applyIngredientFilter(ingredients)).thenReturn(mockRecipes);

        List<Recipe> response = recipeController.applyIngredientFilter(ingredients);
        assertEquals(1, response.size());
        verify(recipeService, times(1)).applyIngredientFilter(ingredients);
    }**/

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
