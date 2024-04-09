package com.depot.ims.services;

import com.depot.ims.models.User;
import com.depot.ims.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import java.util.stream.Stream;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the UserService class.
 * Validates the functionality of user management operations, including retrieval,
 * confirmation, deletion, and updating of user details. Utilizes Mockito to mock
 * the UserRepository for isolated testing of service logic.
 */
public class UserServiceTest {
    @Mock
    private UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @BeforeEach
    void setup() {
    }

    /**
     * Tests retrieving a user by ID or username.
     * Verifies correct user retrieval and handling of null input scenarios.
     */
    @Test
    void testGetUser() {
        User user1 = new User(1L, "user1", "password1", "role1");
        User user2 = new User(2L, "user2", "password2", "role2");

        when(userRepository.findByUserId(1L)).thenReturn(user1);
        when(userRepository.findByUsername("user2")).thenReturn(user2);

        ResponseEntity<?> res1 = userService.getUser(1L, null);
        ResponseEntity<?> res2 = userService.getUser(null, "user2");
        ResponseEntity<?> res3 = userService.getUser(null, null);

        Stream.of(res1, res2, res3).forEach(Assertions::assertNotNull);
        assertEquals(ResponseEntity.ok(user1), res1);
        assertEquals(ResponseEntity.ok(user2), res2);
        assertTrue(res3.getStatusCode().is4xxClientError());
    }

    /**
     * Tests user authentication by confirming username and password.
     * Verifies responses for correct credentials, incorrect password,
     * nonexistent user, and missing inputs.
     */
    @Test
    void testConfirm() {
        User existingUser = new User(1L,
                "user1", "correctPassword", "role1");

        when(userRepository.findByUsername("user1")).thenReturn(existingUser);
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        // Scenario 1: Correct username and password
        ResponseEntity<?> response = userService.confirm("user1",
                "correctPassword");
        assertEquals("User exists and password matches", response.getBody());

        // Scenario 2: Correct username, incorrect password
        response = userService.confirm("user1", "wrongPassword");
        assertEquals("Password does not match", response.getBody());

        // Scenario 3: Non-existent username
        response = userService.confirm("nonexistent", "anyPassword");
        assertEquals("This username does not exist", response.getBody());

        // Scenario 4: Missing username or password
        response = userService.confirm("", "");
        assertEquals("Username and password are required", response.getBody());
    }


    /**
     * Tests the deletion of an existing user.
     * Verifies successful deletion and proper handling when a user is found.
     */
    @Test
    void testDeleteUserFound() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        ResponseEntity<?> response = userService.delete(userId);

        assertEquals("Successfully deleted", response.getBody());
        verify(userRepository).deleteById(userId);
    }

    /**
     * Tests the deletion of an user that do not exist.
     * Verifies successful deletion and proper handling when a user is not found.
     */
    @Test
    void testDeleteUserNotFound() {
        Long userId = 2L;
        when(userRepository.existsById(userId)).thenReturn(false);

        ResponseEntity<?> response = userService.delete(userId);

        assertEquals("User not found by user Id", response.getBody());
        verify(userRepository, never()).deleteById(userId);
    }


    /**
     * Tests updating user information, handling case where the user is not found
     */
    @Test
    void testUpdateUserUserNotFound() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        ResponseEntity<?> response = userService.updateUser(userId,
                "newUsername", "newPassword", "newPosition");
        assertEquals("User not found by user id!", response.getBody());
    }

    /**
     * Tests updating user information, handling case no update values are specified
     */
    @Test
    void testUpdateUserNoUpdateValuesSpecified() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        ResponseEntity<?> response = userService.updateUser(userId,
                null, null, null);
        assertEquals("No value for this update is specified", response.getBody());
    }

    /**
     * Tests updating user information, handling cases where the user is found and a successful update.
     */
    @Test
    void testUpdateUserSuccessful() {
        Long userId = 1L;
        User user = new User(userId, "oldUsername", "oldPassword", "oldPosition");
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userService.updateUser(userId,
                "newUsername", "newPassword", "newPosition");
        assertEquals("Successfully updated", response.getBody());
        verify(userRepository).save(user);
    }
}
