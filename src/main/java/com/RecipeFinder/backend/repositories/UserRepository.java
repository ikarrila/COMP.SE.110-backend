package com.RecipeFinder.backend.repositories;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.models.User;

@Repository
public class UserRepository {

    private static final String FILE_NAME = "users.csv";

    // Save a new User to the file
    public void saveUser(User user) throws IOException {
        FileWriter writer = new FileWriter(FILE_NAME, true);
        writer.write(convertUserToCsv(user) + "\n");
        writer.close();
    }

    // Load all users from the file
    public List<User> loadAllUsers() throws IOException {
        List<User> users = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            users.add(convertCsvToUser(line));
        }
        reader.close();
        return users;
    }

    // Convert User to CSV format
    private String convertUserToCsv(User user) {
        StringBuilder csvBuilder = new StringBuilder();
        // Convert basic fields
        csvBuilder.append(user.getId()).append(";");
        csvBuilder.append(user.getName()).append(";");
        csvBuilder.append(user.getBirthday() != null ? user.getBirthday().toString() : "").append(";");
        csvBuilder.append(user.getDiet()).append(";");
        csvBuilder.append(user.getFavouriteCuisine()).append(";");
        // Convert allergies (list of strings) to CSV
        csvBuilder.append(String.join(",", user.getAllergies())).append(";");
        // Convert saved recipes (assuming we want only recipe IDs in CSV)
        csvBuilder.append(user.getSavedRecipes() != null ? user.getSavedRecipes().stream()
                .map(recipe -> recipe.getId().toString()) // assuming Recipe has getId() method
                .collect(Collectors.joining(",")) : "");
        return csvBuilder.toString();
    }

    // Convert a CSV line to a User object
    private User convertCsvToUser(String csvLine) {
        String[] fields = csvLine.split(";", -1); // Split CSV line by ;

        User user = new User();
        user.setId(Integer.parseInt(fields[0])); // Parse ID as Integer
        user.setName(fields[1]); // Set name

        // Parse birthday (handle empty string case)
        if (!fields[2].isEmpty()) {
            user.setBirthday(LocalDate.parse(fields[2])); // Assuming the birthday is stored in ISO format (yyyy-MM-dd)
        }

        user.setDiet(fields[3]); // Set diet
        user.setFavouriteCuisine(fields[4]); // Set favorite cuisine

        // Parse allergies (split by ",")
        if (!fields[5].isEmpty()) {
            user.setAllergies(List.of(fields[5].split("\\,")));
        }

        // Parse saved recipes (IDs split by ",")
        if (!fields[6].isEmpty()) {
            List<Recipe> savedRecipes = Arrays.stream(fields[6].split("\\,"))
                    .map(id -> {
                        Recipe recipe = new Recipe();
                        recipe.setId(Long.parseLong(id)); // Assuming Recipe has a setId method
                        return recipe;
                    })
                    .collect(Collectors.toList());
            user.setSavedRecipes(savedRecipes);
        }

        return user;
    }

}
