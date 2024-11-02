package com.RecipeFinder.backend.services;

import com.RecipeFinder.backend.models.User;
import com.RecipeFinder.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() throws IOException {
        User user = new User(1, "John Doe", LocalDate.of(1990, 1, 1), "Vegetarian", "Italian", List.of("Nuts"), List.of());
        when(userRepository.loadAllUsers()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }

    @Test
    void testGetUserById_UserExists() throws IOException {
        User user = new User(1, "John Doe", LocalDate.of(1990, 1, 1), "Vegetarian", "Italian", List.of("Nuts"), List.of());
        when(userRepository.getUserById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void testGetUserById_UserDoesNotExist() throws IOException {
        when(userRepository.getUserById(1)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(1);
        assertFalse(result.isPresent());
    }
}
