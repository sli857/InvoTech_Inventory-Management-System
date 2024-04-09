package com.depot.ims.services;

import com.depot.ims.models.User;
import com.depot.ims.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * UserService provides business logic for handling user operations within the
 * Inventory Management System.
 * This includes creating, updating, retrieving, and deleting user information.
 */
@Service
public class UserService {
    private final UserRepository usersRepository;

    /**
     * Constructs a UserService with a repository for data access.
     *
     * @param usersRepository The repository providing data access operations for users.
     */
    public UserService(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Updates the details of an existing user using their ID.
     * Allows for partial updates where only specified fields are updated.
     *
     * @param userID       The ID of the user to update.
     * @param newUsername  The new username for the user (nullable).
     * @param newPassword  The new password for the user (nullable).
     * @param newPosition  The new position/title for the user (nullable).
     * @return ResponseEntity<?> Returns OK if the update was successful, or Bad Request on error.
     */
    public ResponseEntity<?> updateUser( Long userID,
                                         String newUsername,
                                         String newPassword,
                                         String newPosition) {
        if (!usersRepository.existsById(userID)) {
            return ResponseEntity.badRequest().body("User not found by user id!");
        }
        // user id cannot be changed!
        if (Stream.of(newUsername, newPassword, newPosition).allMatch(Objects::isNull)) {
            return ResponseEntity.badRequest().body("No value for this update is specified");
        }

        User user = usersRepository.findByUserId(userID);
        if (newUsername != null) user.setUsername(newUsername);
        if (newPassword != null) user.setPassword(newPassword);
        if (newUsername != null) user.setPosition(newPosition);

        User updatedUser = usersRepository.save(user);
        return ResponseEntity.ok("Successfully updated");
    }


    /**
     * Retrieves the details of a user by their ID or username.
     *
     * @param userId    The ID of the user to find (nullable).
     * @param username  The username of the user to find (nullable).
     * @return ResponseEntity<?> Returns OK with the user details or Bad Request
     * if neither ID nor username is provided.
     */
    public ResponseEntity<?> getUser( Long userId, String username) {
        // user id has to be unique!!
        if (userId != null) {
            return ResponseEntity.ok(usersRepository.findByUserId(userId));
            // username has to be unique!!
        } else if (username != null) {
            return ResponseEntity.ok(usersRepository.findByUsername(username));
        } else {
            return ResponseEntity.badRequest().body("You have to provide your user Id or username");
        }
    }


    /**
     * Confirms a user's existence and matches the provided password with the stored password.
     *
     * @param username  The username of the user.
     * @param password  The password to verify.
     * @return ResponseEntity<?> Returns OK if the user exists and the password matches,
     * or Bad Request on error.
     */
    public ResponseEntity<?> confirm(String username, String password) {
        // an unique user id will be provided by the user
        if (username == null || username.trim().isEmpty() || password == null ||
                password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        User user = usersRepository.findByUsername(username);
        if (user != null && passwordMatches(user.getPassword(), password)) {
            return ResponseEntity.ok().body("User exists and password matches");
        } else if (user != null) {
            return ResponseEntity.badRequest().body("Password does not match");
        } else {
            return ResponseEntity.badRequest().body("This username does not exist");
        }
    }


    /**
     * Compares the provided password with the user's stored password.
     *
     * @param hashedPassword The stored hashed password.
     * @param inputPassword  The password provided by the user.
     * @return boolean True if the passwords match, false otherwise.
     */
    private boolean passwordMatches(String hashedPassword, String inputPassword) {
        return hashedPassword.equals(inputPassword);
    }


    /**
     * Deletes a user using their ID.
     *
     * @param userID The ID of the user to delete.
     * @return ResponseEntity<?> Returns OK if the deletion was successful, or Bad Request on error.
     */
    public ResponseEntity<?> delete(Long userID) {
        try {
            boolean isFound = usersRepository.existsById(userID);
            if (isFound) {
                usersRepository.deleteById(userID);
                return ResponseEntity.ok().body("Successfully deleted");
            }
            return ResponseEntity.badRequest().body("User not found by user Id");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("User Id cannot be null");
        }
    }


}
