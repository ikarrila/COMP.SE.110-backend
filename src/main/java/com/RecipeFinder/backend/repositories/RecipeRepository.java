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

    /**
     * Persists a Recipe object to a CSV file.
     *
     * @param recipe Recipe object to save.
     * @throws IOException if writing to the file fails.
     *
     * This method supports simple persistence of recipes without a database,
     * useful in small-scale applications or local setups. It follows the
     * Data Access Object (DAO) pattern, encapsulating low-level file
     * I/O and providing a consistent API for managing recipes.
     */
    public void saveRecipe(Recipe recipe) throws IOException {
        FileWriter writer = new FileWriter(FILE_NAME, true);
        writer.write(convertRecipeToCsv(recipe) + "\n");
        writer.close();
    }

    /**
     * Retrieves a Recipe by its unique ID from the CSV file.
     *
     * @param id Unique identifier for the recipe.
     * @return Optional Recipe object, or empty if not found.
     * @throws IOException if reading from the file fails.
     *
     * This method provides an efficient way to find specific recipes while
     * avoiding loading all data into memory. It employs the Repository pattern,
     * abstracting away file access and enabling straightforward CRUD operations.
     */
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

    /**
     * Loads all recipes from the CSV file.
     *
     * @return List of all Recipe objects.
     * @throws IOException if reading from the file fails.
     *
     * This method reads each recipe entry from the file, converting it to
     * `Recipe` objects and aggregating them in a list, which supports bulk
     * data retrieval.
     */
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

    // Helper method to convert a Recipe object to CSV format
    private String convertRecipeToCsv(Recipe recipe) {
        return recipe.getId() + "," + recipe.getName(); // Add more fields as needed
    }

    // Helper method to convert a CSV line to a Recipe object
    private Recipe convertCsvToRecipe(String csvLine) {
        String[] fields = csvLine.split(",");
        Recipe recipe = new Recipe();
        recipe.setId(Long.parseLong(fields[0]));
        recipe.setName(fields[1]);
        // Add more fields as needed
        return recipe;
    }
}
