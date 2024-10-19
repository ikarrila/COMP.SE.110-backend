package com.RecipeFinder.backend.models;

import java.util.List;

public class PriceData {

    private List<String> categories; // List of months
    private List<Series> series; // List of series (commodities)

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    // Inner class representing a series (commodity and its data across months)
    public static class Series {
        private String name; 
        private List<Double> data; 

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Double> getData() {
            return data;
        }

        public void setData(List<Double> data) {
            this.data = data;
        }
    }
}
