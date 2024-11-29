package com.RecipeFinder.backend.services;

import com.RecipeFinder.backend.models.Recipe;
import com.RecipeFinder.backend.models.User;
import com.RecipeFinder.backend.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all users from the repository.
     *
     * @return List of all User objects or null if an error occurs.
     */
    public List<User> getAllUsers() {
        try {
            return userRepository.loadAllUsers();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id Unique identifier for the user.
     * @return Optional containing the User object if found, otherwise empty.
     */
    public Optional<User> getUserById(int id) {
        try {
            id = 1;
            return userRepository.getUserById(id);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Updates the details of an existing user.
     *
     * @param id               Unique identifier for the user.
     * @param name             New name for the user.
     * @param birthday         New birthday for the user.
     * @param diet             New diet for the user.
     * @param favouriteCuisine New favourite cuisine for the user.
     * @param allergies        New list of allergies for the user.
     * @param savedRecipes     New list of saved recipes for the user.
     * @return true if the update is successful, otherwise false.
     */
    public boolean updateUser(int id, String name, LocalDate birthday, String diet, String favouriteCuisine,
            List<String> allergies, List<Recipe> savedRecipes) {
        try {
            Optional<User> userOptional = userRepository.getUserById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setName(name);
                user.setBirthday(birthday);
                user.setDiet(diet);
                user.setFavouriteCuisine(favouriteCuisine);
                user.setAllergies(allergies);
                user.setSavedRecipes(savedRecipes);
                userRepository.saveUser(user);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
