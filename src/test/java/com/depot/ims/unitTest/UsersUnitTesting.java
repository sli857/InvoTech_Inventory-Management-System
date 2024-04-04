package com.depot.ims.unitTest;
import static org.hamcrest.Matchers.hasSize;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import com.depot.ims.controllers.UserController;
import com.depot.ims.models.User;
import com.depot.ims.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(UserController.class)
public class UsersUnitTesting {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    // test for the getUsers endpoint
    @Test
    public void whenGetUsers_thenReturns200() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    // test for getting all users
    @Test
    public void testUsers() throws Exception {
        // Setup mock users
        List<User> users = Arrays.asList(
                new User(1L, "user1", "password1", "role1"),
                new User(2L, "user2", "password2", "role2"));
        when(userRepository.findAll()).thenReturn(users);

        MvcResult result = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", org.hamcrest.Matchers.is("user1")))
                .andReturn(); // Capture the response
    }


    // test to get one user by id or name
    @Test
    public void testGetUser() throws Exception {
        // Setup mock user
        User user = new User(1L, "user1", "password1", "role1");
        when(userRepository.findByUserId(1L)).thenReturn(user);

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

    // test for add an user
    @Test
    public void testAddUser() throws Exception {
        User user = new User(null, "user1", "password1", "role1");
        User savedUser = new User(1L, "user1", "password1", "role1");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.password", is("password1")))
                .andExpect(jsonPath("$.position", is("role1")));
    }


    // test when user exists and password matches
    @Test
    public void whenUserExistsAndPasswordMatches_thenReturnOk() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password"); // Assume the password is not hashed for this simple test

        when(userRepository.findByUsername("user")).thenReturn(user);

        mockMvc.perform(get("/users/confirm")
                        .param("username", "user")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(content().string("User exists and password matches"));
    }

    // test when user exists and password does not match
    @Test
    public void whenUserExistsAndPasswordDoesNotMatch_thenReturnBadRequest() throws Exception {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");

        when(userRepository.findByUsername("user")).thenReturn(user);

        mockMvc.perform(get("/users/confirm")
                        .param("username", "user")
                        .param("password", "wrongPassword"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Password does not match"));
    }

    // when user does not exist
    @Test
    public void whenUserDoesNotExist_thenReturnBadRequest() throws Exception {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        mockMvc.perform(get("/users/confirm")
                        .param("username", "nonexistent")
                        .param("password", "anyPassword"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This username does not exist"));
    }


    // test for update user information
    @Test
    public void testUpdateUser() throws Exception {
        Long userId = 1L;
        User existingUser = new User(userId, "oldUsername", "oldPassword", "role1");
        User updatedUser = new User(userId, "newUsername", "newPassword", "role1");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findByUserId(userId)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/users/update")
                        .param("userId", userId.toString())
                        .param("username", "newUsername")
                        .param("password", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("newUsername")))
                .andExpect(jsonPath("$.password", is("newPassword")));
    }

    // successful deletion case and user exist
    @Test
    public void testDeleteUser() throws Exception {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        mockMvc.perform(delete("/users/delete")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));

        // Verify deleteUser was called
        verify(userRepository).deleteById(userId);
    }

    // user not found deletion case
    @Test
    public void testDeleteNotFoundUser() throws Exception {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        mockMvc.perform(delete("/users/delete")
                        .param("userId", userId.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found by user Id"));

        // Ensure deleteUser was never called
        verify(userRepository, never()).deleteById(any());
    }




}
