package com.RecipeFinder.backend.repositories;

import com.RecipeFinder.backend.models.Ingredient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class IngredientRepository {

    // Placeholder: In the future, this can retrieve ingredients from a data source
    public Optional<Ingredient> findById(Long id) {
        // Placeholder: Currently does nothing
        return Optional.empty();
    }

    // Placeholder: Retrieve all ingredients
    public List<Ingredient> findAll() {
        // Placeholder: Currently returns an empty list
        return List.of();
    }

    // Placeholder: Save ingredient
    public void save(Ingredient ingredient) {
        // Placeholder: Does nothing for now
    }
}
