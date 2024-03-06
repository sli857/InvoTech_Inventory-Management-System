package com.depot.ims.repositories;

import com.depot.ims.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {

}
