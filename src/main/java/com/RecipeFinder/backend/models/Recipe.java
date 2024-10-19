package com.RecipeFinder.backend.models;

import java.util.List;

public class Recipe {
    private Long id;
    private String name;
    private List<Ingredient> ingredients;
    private NutritionalInfo nutritionalInfo;
    private boolean isDairyFree;
    private boolean isGlutenFree;
    private boolean isSustainable;
    private int healthScore;


    // Default constructor
    public Recipe() {}

    // Constructor with fields
    public Recipe(Long id, String name, List<Ingredient> ingredients, NutritionalInfo nutritionalInfo) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.nutritionalInfo = nutritionalInfo;
    }

    // Getters and Setters example
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public NutritionalInfo getNutritionalInfo() {
        return nutritionalInfo;
    }

    public void setNutritionalInfo(NutritionalInfo nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
    }
}


//TODO: Add missing items from prototype, f.e. isCheap, isSustainable