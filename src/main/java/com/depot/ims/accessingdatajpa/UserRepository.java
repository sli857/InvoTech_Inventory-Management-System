package com.depot.ims.accessingdatajpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByUsername(String username);

    User findById(int id);
}
