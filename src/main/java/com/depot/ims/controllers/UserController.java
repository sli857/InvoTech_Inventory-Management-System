package com.depot.ims.controllers;

import com.depot.ims.models.User;
import com.depot.ims.repositories.UserRepository;
import com.depot.ims.services.UserService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173/")

public class UserController {

    private final UserRepository usersRepository;
    private final UserService userService;

    public UserController(UserRepository usersRepository, UserService userService) {
        this.usersRepository = usersRepository;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(this.usersRepository.findAll());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "username", required = false) String username) {
        return userService.getUser(userId, username);
    }
    @GetMapping("/confirm")
    public ResponseEntity<?> findUserByUsername(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        return userService.confirm(username, password);
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
            String newPassword,
            @RequestParam(value = "position", required = false)
            String newPosition) {
        return userService.updateUser(userID, newUsername, newPassword, newPosition);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") Long userID) {
        return userService.delete(userID);
    }

}