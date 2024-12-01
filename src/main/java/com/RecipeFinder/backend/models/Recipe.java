package com.RecipeFinder.backend.models;

import java.util.List;

public class Recipe {
    private Long id;
    private String name;
    private List<String> ingredients;
    private List<String> diets;
    private List<String> cuisines;

    private boolean isDairyFree;
    private boolean isGlutenFree;
    private boolean isSustainable;
    private boolean isCheap;
    private boolean isPopular;
    private boolean isHealthy;

    private int healthScore;

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

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }


    public List<String> getDiets() {
        return diets;
    }

    public void setDiets(List<String> diets) {
        this.diets = diets;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<String> cuisines) {
        this.cuisines = cuisines;
    }

    public boolean getIsDairyFree() {
        return isDairyFree;
    }

    public void setIsDairyFree(boolean isDairyFree) {
        this.isDairyFree = isDairyFree;
    }

    public boolean getIsGlutenFree() {
        return isGlutenFree;
    }

    public void setIsGlutenFree(boolean isGlutenFree) {
        this.isGlutenFree = isGlutenFree;
    }

    public boolean getIsSustainable() {
        return isSustainable;
    }

    public void setIsSustainable(boolean isSustainable) {
        this.isSustainable = isSustainable;
    }

    public boolean getIsCheap() {
        return isCheap;
    }

    public void setIsCheap(boolean isCheap) {
        this.isCheap = isCheap;
    }

    public boolean getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(boolean isPopular) {
        this.isPopular = isPopular;
    }

    public boolean getIsHealthy() {
        return isHealthy;
    }

    public void setIsHealthy(boolean isHealthy) {
        this.isHealthy = isHealthy;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public void setHealthScore (int healthScore) {
        this.healthScore = healthScore;
    }

}
