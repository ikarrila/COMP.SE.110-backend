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

        // Use class loader to access the CSV file in the resources
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(FILE_NAME)))) {

            List<String> lines = reader.lines().collect(Collectors.toList()); // Read all lines

            System.out.println("CSV Lines Read:");
            lines.forEach(System.out::println);  // Debug log all lines

            users = lines.stream()
                .skip(1) // Skip header row if present
                .map(line -> {
                    String[] fields = line.split(";");
                    System.out.println("Parsing user from line: " + line); // Log the parsed line
                    return new User(
                        Integer.parseInt(fields[0]),  // ID
                        fields[1],                    // Name
                        LocalDate.parse(fields[2]),   // Birthday
                        fields[3],                    // Diet
                        fields[4],                    // Favourite cuisine
                        fields.length > 5 && !fields[5].isEmpty() ? List.of(fields[5].split(",")) : Collections.emptyList(), // Allergies
                        Collections.emptyList()       // Placeholder for savedRecipes
                    );
                })
                .collect(Collectors.toList());
        }

        return users;
    }

    // Get a user by ID
    public Optional<User> getUserById(int id) throws IOException {
        // Load all users and filter by the provided ID
        return loadAllUsers().stream()
                .filter(user -> user.getId() == id)
                .findFirst();
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
        csvBuilder.append(user.getSavedRecipes() != null ? user.getSavedRecipes().stream()
                .map(recipe -> recipe.getId().toString())
                .collect(Collectors.joining(",")) : "");
        return csvBuilder.toString();
    }
}
