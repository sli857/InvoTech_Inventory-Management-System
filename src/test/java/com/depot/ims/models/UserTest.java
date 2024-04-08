package com.depot.ims.models;

import com.depot.ims.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=update"
})

public class UserTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        // Preparing test data
        createUser("user1", "password1", "role1");
        createUser("user2", "password2", "role2");
    }

    @Test
    void findAllTest() {
        List<User> users = userRepository.findAll();
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void findByUsernameTest() {
        User user = userRepository.findByUsername("user1");
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }

    private User createUser(String username, String password, String position) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setPosition(position);
        return userRepository.saveAndFlush(newUser);
    }
}

