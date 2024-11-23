package com.RecipeFinder.backend.models;

import java.util.List;

//TODO: true false valuet onko sliderit käytössä?
public class RecipeFilter {
    private String cuisine;
    private String diet;
    private boolean isDairyFree;
    private boolean isGlutenFree;

    private boolean isCaloriesUsed;
    private double minCalories;
    private double maxCalories;

    private boolean isProteinUsed;
    private double minProtein;
    private double maxProtein;

    private boolean isCarbsUsed;
    private double minCarbs;
    private double maxCarbs;

    private List<String> includeIngredients;
    private List<String> excludeIngredients;

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public boolean isDairyFree() {
        return isDairyFree;
    }

    public void setDairyFree(boolean dairyFree) {
        isDairyFree = dairyFree;
    }

    public boolean isGlutenFree() {
        return isGlutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        isGlutenFree = glutenFree;
    }

    public boolean isCaloriesUsed() {
        return isCaloriesUsed;
    }

    public void setCaloriesUsed(boolean caloriesUsed) {
        isCaloriesUsed = caloriesUsed;
    }

    public double getMinCalories() {
        return minCalories;
    }

    public void setMinCalories(double minCalories) {
        this.minCalories = minCalories;
    }

    public double getMaxCalories() {
        return maxCalories;
    }

    public void setMaxCalories(double maxCalories) {
        this.maxCalories = maxCalories;
    }

    public boolean isProteinUsed() {
        return isProteinUsed;
    }

    public void setProteinUsed(boolean proteinUsed) {
        isProteinUsed = proteinUsed;
    }

    public double getMinProtein() {
        return minProtein;
    }

    public void setMinProtein(double minProtein) {
        this.minProtein = minProtein;
    }

    public double getMaxProtein() {
        return maxProtein;
    }

    public void setMaxProtein(double maxProtein) {
        this.maxProtein = maxProtein;
    }

    public boolean isCarbsUsed() {
        return isCarbsUsed;
    }

    public void setCarbsUsed(boolean carbsUsed) {
        isCarbsUsed = carbsUsed;
    }

    public double getMinCarbs() {
        return minCarbs;
    }

    public void setMinCarbs(double minCarbs) {
        this.minCarbs = minCarbs;
    }

    public double getMaxCarbs() {
        return maxCarbs;
    }

    public void setMaxCarbs(double maxCarbs) {
        this.maxCarbs = maxCarbs;
    }

    public List<String> getIncludeIngredients() {
        return includeIngredients;
    }

    public void setIncludeIngredients(List<String> includeIngredients) {
        this.includeIngredients = includeIngredients;
    }

    public List<String> getExcludeIngredients() {
        return excludeIngredients;
    }

    public void setExcludeIngredients(List<String> excludeIngredients) {
        this.excludeIngredients = excludeIngredients;
    }

    
}
