package com.RecipeFinder.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RecipeFinder.backend.models.User;
import com.RecipeFinder.backend.repositories.UserRepository;

import java.io.IOException;
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
            id = 1;
            return userRepository.getUserById(id);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
