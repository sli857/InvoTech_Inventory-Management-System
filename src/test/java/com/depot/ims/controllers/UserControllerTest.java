package com.depot.ims.controllers;

import com.depot.ims.models.User;
import com.depot.ims.repositories.UserRepository;
import com.depot.ims.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for UserController - ensures correct handling of user-related requests.
 * Mocks the UserRepository and UserService to isolate controller logic and verify interactions.
 */
public class UserControllerTest {
    @InjectMocks
    UserController userController;
    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;

    private MockMvc mockMvc;

    /**
     * Setup method to initialize Mockito mocks and configure the MockMvc instance for
     * standalone controller testing.
     * This setup is performed before each test case.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    /**
     * Tests retrieval of all users, verifying correct HTTP status and JSON structure.
     * Mocks UserRepository to return a predefined list of users.
     * Asserts the size of the returned user list and specific user attributes.
     */
    @Test
    public void testGetUsers() throws Exception {
        // Setup mock users
        List<User> users = Arrays.asList(
                new User(1L, "user1", "password1", "role1"),
                new User(2L, "user2", "password2", "role2"));
        when(userRepository.findAll()).thenReturn(users);

        // Perform the test and verify results
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username",
                        org.hamcrest.Matchers.is("user1")))
                .andReturn(); // Capture the response
    }

    /**
     * Tests retrieval of a single user by ID, verifying correct HTTP status and JSON structure.
     * Mocks UserService to return a specific user for a given ID.
     * Asserts the attributes of the returned user.
     */
    @Test
    public void testGetUser() throws Exception {
        // Setup mock user
        User user = new User(1L, "user1", "password1", "role1");

        ResponseEntity<User> res = new ResponseEntity<>(
                user,
                null,
                HttpStatus.OK
        );

        doReturn(res).when(userService).getUser(1L, null);

        // Perform GET request to /user endpoint with userId as request parameter
        mockMvc.perform(get("/users/user")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.password", is("password1")))
                .andExpect(jsonPath("$.position", is("role1")))
                .andReturn(); // Capture the response
    }


    /**
     * Tests adding a new user, verifying correct HTTP status and JSON structure of the response.
     * Mocks UserRepository to simulate saving of the user and return a mock persisted user entity.
     * Asserts the attributes of the saved user.
     */
    @Test
    public void testAddUser() throws Exception {
        User user = new User(null, "user1", "password1", "role1");
        User savedUser = new User(1L, "user1", "password1", "role1");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        // Perform the test and verify results
        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.password", is("password1")))
                .andExpect(jsonPath("$.position", is("role1")));
    }


    /**
     * Tests updating an existing user, verifying the correct HTTP status.
     * Mocks UserService to simulate the update operation and verify the update request's attributes.
     */
    @Test
    public void testUpdateUser() throws Exception {
        Long userId = 1L;
        User update = new User(userId, "newUsername", "newPassword", "role2");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(update);

        // Mock the service call to return a success response
        doReturn(ResponseEntity.ok("Successfully updated")).when(userService)
                .updateUser(userId, "newUsername", "newPassword", "role2");

        // Perform the test and verify results
        mockMvc.perform(post("/users/update")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());
    }

    /**
     * Tests the deletion of an existing user, verifying the correct HTTP status and success message.
     * Mocks UserService to confirm deletion operation.
     */
    @Test
    void deleteUserFound() throws Exception {
        Long userId = 1L;
        // Mock the service call to return a success response
        doReturn(ResponseEntity.ok("Successfully deleted")).when(userService).delete(1L);

        // Perform the test and verify results
        mockMvc.perform(delete("/users/delete")
                        .param("userId", userId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));

        // Verify that the userService's delete method was called
        verify(userService, times(1)).delete(userId);
    }

    /**
     * Tests the attempt to delete a non-existing user, verifying the correct HTTP status and error message.
     * Mocks UserService to simulate the case where the user does not exist.
     */
    @Test
    void deleteUserNotFound() throws Exception {
        Long userId = 2L;
        // Mock the service call to return an error response
        doReturn(ResponseEntity.badRequest().body("User not found by user Id"))
                .when(userService).delete(2L);


        // Perform the test and verify results
        mockMvc.perform(delete("/users/delete")
                        .param("userId", userId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found by user Id"));

        // Verify that the userService's delete method was called
        verify(userService, times(1)).delete(userId);
    }

}