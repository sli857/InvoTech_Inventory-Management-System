// Package declaration
package com.depot.ims.controllers;

// Import statements
import com.depot.ims.models.User;
import com.depot.ims.repositories.UserRepository;
import com.depot.ims.services.UserService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController class provides API endpoints for managing users within the Inventory Management System.
 * This controller facilitates operations such as creating, updating, deleting, and fetching user details.
 */
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173/")

public class UserController {

    // Fields for the user repository and user service
    private final UserRepository usersRepository;
    private final UserService userService;

    /**
     * Constructor for UserController.
     *
     * @param usersRepository Repository for user data access.
     * @param userService Service for user-related operations.
     */
    public UserController(UserRepository usersRepository, UserService userService) {
        this.usersRepository = usersRepository;
        this.userService = userService;
    }


    /**
     * Endpoint to retrieve all users.
     *
     * @return ResponseEntity with the list of all users.
     */
    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(this.usersRepository.findAll());
    }

    /**
     * Endpoint to fetch a specific user by their ID or username.
     *
     * @param userId The ID of the user to retrieve (optional).
     * @param username The username of the user to retrieve (optional).
     * @return ResponseEntity with the details of the specified user.
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUser(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "username", required = false) String username) {
        return userService.getUser(userId, username);
    }

    /**
     * Endpoint to confirm a user's credentials.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return ResponseEntity indicating whether the credentials are valid.
     */
    @GetMapping("/confirm")
    public ResponseEntity<?> findUserByUsername(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        return userService.confirm(username, password);
    }

    /**
     * Endpoint to add a new user.
     * Accepts user details in the form of a JSON object.
     *
     * @param user User object containing the details of the new user.
     * @return The saved User entity.
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(@RequestBody User user) {
        return this.usersRepository.save(user);
    }

    /**
     * Endpoint to update the details of an existing user.
     * This method allows partial updates to user properties.
     *
     * @param userID The ID of the user to update.
     * @param newUsername New username for the user (optional).
     * @param newPassword New password for the user (optional).
     * @param newPosition New position/title for the user (optional).
     * @return ResponseEntity indicating the result of the update operation.
     */
    @Modifying
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestParam(value = "userId")
            Long userID,
            @RequestParam(value = "username", required = false)
            String newUsername,
            @RequestParam(value = "password", required = false)
            String newPassword,
            @RequestParam(value = "position", required = false)
            String newPosition) {
        return userService.updateUser(userID, newUsername, newPassword, newPosition);
    }

    /**
     * Endpoint to delete a user by their ID.
     *
     * @param userID The ID of the user to delete.
     * @return ResponseEntity indicating the result of the delete operation.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") Long userID) {
        return userService.delete(userID);
    }

}