package com.RecipeFinder.backend.services;

import org.springframework.stereotype.Service;

import com.RecipeFinder.backend.models.User;
import com.RecipeFinder.backend.repositories.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService {

    private final Integer currentUserId = 1; // Hardcoded current user id

    @Autowired
    private UserRepository userRepository;

    /**
     * Fetch current User's profile
     * 
     * NOTE: Because we decided to skip registration and login part,
     * we will use a default user which is now hardcoded as the result.
     */
    public User fetchProfile() {
        System.out.println("TÄÄLLÄ EMIL TÄÄLLÄ");

        try {
            List<User> users = userRepository.loadAllUsers();

            Optional<User> userOpt = users.stream()
                    .filter(user -> user.getId().equals(currentUserId))
                    .findFirst();

            // Return null or throw an exception if not found
            return userOpt.orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
