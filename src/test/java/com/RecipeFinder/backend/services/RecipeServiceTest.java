package com.RecipeFinder.backend.services;

import com.RecipeFinder.backend.repositories.SpoonacularAPIRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    @Mock
    private SpoonacularAPIRepository spoonacularAPIRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


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
