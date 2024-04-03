package com.depot.ims.controllers;
import com.depot.ims.models.User;
import com.depot.ims.repositories.UsersRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.Objects;

@RestController
@RequestMapping(value = "/users",  produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173/")
public class UsersController {
    private final UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(this.usersRepository.findAll());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "username", required = false) String username) {
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

    @GetMapping("/confirm")
    public ResponseEntity<?> findUserByUsername(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
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

    private boolean passwordMatches(String hashedPassword, String inputPassword) {
        return hashedPassword.equals(inputPassword);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(@RequestBody User user) {
        return this.usersRepository.save(user);
    }

    @Modifying
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestParam(value = "userId")
            Long userID,
            @RequestParam(value = "username", required = false)
            String newUsername,
            @RequestParam(value = "password", required = false)
            String newPassword) {
        if (!usersRepository.existsById(userID)) {
            return ResponseEntity.badRequest().body("User not found by user id!");
        }
        // user id cannot be changed!
        if (Stream.of(newUsername, newPassword).allMatch(Objects::isNull)) {
            return ResponseEntity.badRequest().body("No value for this update is specified");
        }

        User user = usersRepository.findByUserId(userID);
        if (newUsername != null) user.setUsername(newUsername);
        if (newPassword != null) user.setPassword(newPassword);

        User updatedUser = usersRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") Long userID) {
        try {
            boolean isFound = usersRepository.existsById(userID);
            if (isFound) {
                usersRepository.deleteById(userID);
                return ResponseEntity.ok().body("Successfully deleted");
            }
            return ResponseEntity.badRequest().body("User not found by user Id");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("user Id cannot be null");
        }
    }

}