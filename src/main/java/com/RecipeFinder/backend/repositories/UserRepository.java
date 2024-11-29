package com.RecipeFinder.backend.repositories;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

import com.RecipeFinder.backend.models.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.RecipeFinder.backend.models.Recipe;

@Repository
public class UserRepository {

    private static final String FILE_NAME = "users.csv";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Load all users from the file
    public List<User> loadAllUsers() throws IOException {
        List<User> users = new ArrayList<>();

        // Use class loader to access the CSV file in the resources
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(FILE_NAME)))) {

            List<String> lines = reader.lines().collect(Collectors.toList()); // Read all lines

            System.out.println("CSV Lines Read:");
            lines.forEach(System.out::println); // Debug log all lines

            users = lines.stream()
                    .skip(1) // Skip header row if present
                    .map(line -> {
                        String[] fields = line.split(";");
                        System.out.println("Parsing user from line: " + line); // Log the parsed line
                        List<Recipe> savedRecipes = Collections.emptyList();
                        if (fields.length > 6 && !fields[6].isEmpty()) {
                            try {
                                savedRecipes = objectMapper.readValue(fields[6], new TypeReference<List<Recipe>>() {
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return new User(
                                Integer.parseInt(fields[0]), // ID
                                fields[1], // Name
                                LocalDate.parse(fields[2]), // Birthday
                                fields[3], // Diet
                                fields[4], // Favourite cuisine
                                fields.length > 5 && !fields[5].isEmpty() ? List.of(fields[5].split(","))
                                        : Collections.emptyList(), // Allergies
                                savedRecipes // Saved recipes
                        );
                    })
                    .collect(Collectors.toList());
        }

        return users;
    }

    // Convert User to CSV format
    private String convertUserToCsv(User user) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append(user.getId()).append(";");
        csvBuilder.append(user.getName()).append(";");
        csvBuilder.append(user.getBirthday() != null ? user.getBirthday().toString() : "").append(";");
        csvBuilder.append(user.getDiet()).append(";");
        csvBuilder.append(user.getFavouriteCuisine()).append(";");
        csvBuilder.append(String.join(",", user.getAllergies())).append(";");
        try {
            csvBuilder.append(objectMapper.writeValueAsString(user.getSavedRecipes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvBuilder.toString();
    }

    // Get a user by ID
    public Optional<User> getUserById(int id) throws IOException {
        // Load all users and filter by the provided ID
        return loadAllUsers().stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    // Save a new User to the file
    public void saveUser(User user) throws IOException {
        List<User> users = loadAllUsers(); // Load all existing users

        // Check if the user already exists
        Optional<User> existingUser = users.stream()
                .filter(u -> u.getId() == user.getId())
                .findFirst();

        if (existingUser.isPresent()) {
            // Update the existing user
            users.remove(existingUser.get());
        }
        // Add the new or updated user
        users.add(user);

        // Write all users back to the file
        try (FileWriter writer = new FileWriter(getClass().getClassLoader().getResource(FILE_NAME).getPath())) {
            // Write the header
            writer.write("ID;Name;Birthday;Diet;FavouriteCuisine;Allergies;SavedRecipes\n");
            for (User u : users) {
                writer.write(convertUserToCsv(u) + "\n");
            }
        }
    }
}
