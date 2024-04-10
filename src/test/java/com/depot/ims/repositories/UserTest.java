package com.depot.ims.repositories;

import com.depot.ims.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for the User entity using the H2 in-memory database.
 * Tests validate the basic CRUD operations provided by UserRepository.
 */
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=update"
})

public class UserTest {
    @Autowired
    private UserRepository userRepository;

    /**
     * Sets up the database with initial data before each test.
     * This method creates and saves user entities to ensure a
     * consistent starting state for each test.
     */
    @BeforeEach
    void setup() {
        // Preparing test data
        createUser("user1", "password1", "role1");
        createUser("user2", "password2", "role2");
    }

    /**
     * Tests retrieval of all user entities from the database.
     * Validates that the findAll operation can successfully retrieve the correct
     * number of user entities.
     */
    @Test
    void findAllTest() {
        List<User> users = userRepository.findAll();
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    /**
     * Tests retrieval of a user by username.
     * Validates that the findByUsername operation returns the correct user entity.
     */
    @Test
    void findByUsernameTest() {
        User user = userRepository.findByUsername("user1");
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }

    /**
     * Helper method to create and persist a User entity for testing.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param position The role or position of the new user.
     * @return The persisted User entity.
     */
    private User createUser(String username, String password, String position) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setPosition(position);
        return userRepository.saveAndFlush(newUser);
    }
}

