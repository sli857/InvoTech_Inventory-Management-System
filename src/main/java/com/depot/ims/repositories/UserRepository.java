package com.depot.ims.repositories;

import com.depot.ims.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * UserRepository interface for accessing and manipulating User entity data.
 * Extends JpaRepository to provide standard CRUD operations and includes custom
 * queries for finding users by their ID and username.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique user ID.
     * Uses a custom JPQL query to retrieve the user.
     *
     * @param userId The unique ID of the user to be found.
     * @return User entity if found, or null otherwise.
     */
    @Query("SELECT u FROM User u WHERE u.userId=?1")
    User findByUserId(Long userId);

    /**
     * Finds a user by their username.
     * Uses a custom JPQL query to retrieve the user, allowing for username-based searches.
     *
     * @param username The username of the user to be found.
     * @return User entity if found, or null otherwise.
     */
    @Query("SELECT u FROM User u WHERE u.username=?1")
    User findByUsername(String username);

}
