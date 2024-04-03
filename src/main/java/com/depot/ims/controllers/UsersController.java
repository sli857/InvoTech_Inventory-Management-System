package com.depot.ims.controllers;

import com.depot.ims.models.User;
import com.depot.ims.repositories.UsersRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping
    public List<User> getUsers() {
        return this.usersRepository.findAll();
    }

    @GetMapping("/userId={userID}")
    public User getUserById(@PathVariable Long userID) {
        return this.usersRepository.findByUserId(userID);
    }

    @PostMapping(value = "/addUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User addItem(@RequestBody User user) {
        return this.usersRepository.save(user);
    }

}
