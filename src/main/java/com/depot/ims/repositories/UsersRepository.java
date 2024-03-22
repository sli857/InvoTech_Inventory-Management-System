package com.depot.ims.repositories;

import com.depot.ims.models.User;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    List<User> findAll();

    @Query("SELECT u FROM User u WHERE u.userId=?1")
    User findByUserId(Integer userId);

    @Query("SELECT u FROM User u WHERE u.username=?1")
    User findByUsername(String username);

}
