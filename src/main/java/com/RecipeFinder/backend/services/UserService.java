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

    public List<User> getAllUsers() {
        try {
            return userRepository.loadAllUsers();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<User> getUserById(int id) {
        try {
            return userRepository.getUserById(id);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

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
