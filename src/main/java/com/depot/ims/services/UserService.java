package com.depot.ims.services;

import com.depot.ims.models.User;
import com.depot.ims.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Service class for managing User operations
 */
@Service
public class UserService {
    private final UserRepository usersRepository;

    /**
     * Constructor for UserService.
     *
     * @param usersRepository  The UsersRepository instance.
     */
    public UserService(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Update any information for a user.
     *
     * @param Long userID, String newUsername, String newPassword
     * @return ResponseEntity the returned http status of the code
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
     * Get all the information of a user
     *
     * @param  Long userId, String username
     * @return the information of the user
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
     * Confirm the existence of the user and confirm the correct password is entered
     *
     * @param String username, String password
     * @return ResponseEntity the returned http status of the code
     */
    public ResponseEntity<?> confirm(String username, String password) {
        // an unique user id will be provided by the user
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
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
     * This is a function that test if the user provide password match what we have in the database
     *
     * @param String hashedPassword, String inputPassword
     * @return ResponseEntity the returned http status of the code
     */
    private boolean passwordMatches(String hashedPassword, String inputPassword) {
        return hashedPassword.equals(inputPassword);
    }


    /**
     * Delete the information of a sepcified user.
     *
     * @param Long userID
     * @return ResponseEntity the returned http status of the code
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
