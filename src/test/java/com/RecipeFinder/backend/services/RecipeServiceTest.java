package com.RecipeFinder.backend.services;

import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.repositories.SpoonacularAPIRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.util.List;

class RecipeServiceTest {

    @Mock
    private SpoonacularAPIRepository spoonacularAPIRepository;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**@Test
    void testApplyIngredientFilter() {
        List<String> ingredients = List.of("tomato", "cheese");
        Recipe mockRecipe = new Recipe(); // Create a mock Recipe object
        List<Recipe> mockRecipes = List.of(mockRecipe);

        when(spoonacularAPIRepository.applyIngredientFilter(ingredients)).thenReturn(mockRecipes);

        List<Recipe> recipes = recipeService.applyIngredientFilter(ingredients);
        assertEquals(1, recipes.size());
        verify(spoonacularAPIRepository, times(1)).applyIngredientFilter(ingredients);
    }**/

    @Test
    void testGetRecipeInformation() {
        Integer recipeId = 123;
        String mockRecipeInfo = "Mock Recipe Information";

        when(spoonacularAPIRepository.getRecipeInformation(recipeId)).thenReturn(mockRecipeInfo);

        String recipeInfo = recipeService.getRecipeInformation(recipeId);
        assertEquals(mockRecipeInfo, recipeInfo);
        verify(spoonacularAPIRepository, times(1)).getRecipeInformation(recipeId);
    }
}
