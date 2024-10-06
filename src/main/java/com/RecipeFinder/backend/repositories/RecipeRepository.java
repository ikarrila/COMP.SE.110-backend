package com.RecipeFinder.backend.repositories;

import com.RecipeFinder.backend.models.Recipe;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RecipeRepository {

    private static final String FILE_NAME = "recipes.csv";

    // Save a new recipe to the file
    public void saveRecipe(Recipe recipe) throws IOException {
        FileWriter writer = new FileWriter(FILE_NAME, true);
        writer.write(convertRecipeToCsv(recipe) + "\n");
        writer.close();
    }

    // Find a recipe by ID
    public Optional<Recipe> findById(Long id) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            Recipe recipe = convertCsvToRecipe(line);
            if (recipe.getId().equals(id)) {
                reader.close();
                return Optional.of(recipe);
            }
        }
        reader.close();
        return Optional.empty();
    }

    // Load all recipes from the file
    public List<Recipe> loadAllRecipes() throws IOException {
        List<Recipe> recipes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            recipes.add(convertCsvToRecipe(line));
        }
        reader.close();
        return recipes;
    }

    // Convert a recipe to CSV format
    private String convertRecipeToCsv(Recipe recipe) {
        return recipe.getId() + "," + recipe.getName(); // Add more fields as needed
    }

    // Convert a CSV line to a Recipe object
    private Recipe convertCsvToRecipe(String csvLine) {
        String[] fields = csvLine.split(",");
        Recipe recipe = new Recipe();
        recipe.setId(Long.parseLong(fields[0]));
        recipe.setName(fields[1]);
        // Add more fields as needed
        return recipe;
    }
}
