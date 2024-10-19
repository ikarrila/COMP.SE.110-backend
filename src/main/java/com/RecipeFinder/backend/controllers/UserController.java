package com.RecipeFinder.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.RecipeFinder.backend.services.UserService;
import com.RecipeFinder.backend.models.User;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

@RestController
@RequestMapping("/api/profile")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public User getUserProfile() {
        // System.out.println("TÄÄLLÄ EMIL TÄÄLLÄ");
        User user = userService.fetchProfile();

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        return user; // Spring automatically converts User to JSON
    }

}
