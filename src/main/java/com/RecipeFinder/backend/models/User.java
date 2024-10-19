package com.RecipeFinder.backend.models;

import java.time.LocalDate;
import java.util.List;

public class User {
    private Integer id;
    private String name;
    // private String image; // @TODO: Add support for image URL
    private LocalDate birthday;
    private String diet;
    private String favouriteCuisine;
    private List<String> allergies;
    private List<Recipe> savedRecipes;

    // Constructors
    public User(Integer id, String name, LocalDate birthday, String diet, String favouriteCuisine, List<String> allergies, List<Recipe> savedRecipes) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.diet = diet;
        this.favouriteCuisine = favouriteCuisine;
        this.allergies = allergies;
        this.savedRecipes = savedRecipes;
    }

    public User() {}

    // Getters

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getDiet() {
        return diet;
    }

    public String getFavouriteCuisine() {
        return favouriteCuisine;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public List<Recipe> getSavedRecipes() {
        return savedRecipes;
    }

    // Setters

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public void setFavouriteCuisine(String favouriteCuisine) {
        this.favouriteCuisine = favouriteCuisine;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public void setSavedRecipes(List<Recipe> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }
}
