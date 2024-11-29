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
        User user = new User(1, "Jane Smith", LocalDate.of(1985, 8, 22), "Vegan", "Mexican", List.of("Dairy", "Soy"),
                List.of());
        when(userRepository.loadAllUsers()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Jane Smith", users.get(0).getName());
    }

    @Test
    void testGetUserById_UserExists() throws IOException {
        User user = new User(1, "Jane Smith", LocalDate.of(1985, 8, 22), "Vegan", "Mexican", List.of("Dairy", "Soy"),
                List.of());
        when(userRepository.getUserById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);
        assertTrue(result.isPresent());
        assertEquals("Jane Smith", result.get().getName());
    }

    @Test
    void testGetUserById_UserDoesNotExist() throws IOException {
        when(userRepository.getUserById(2)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(2);
        assertFalse(result.isPresent());
    }
}
