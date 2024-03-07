package com.depot.ims.repositories;

import com.depot.ims.models.Sites;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SitesRepository extends JpaRepository<Sites, Integer>{

    @Query("select s from Sites s where s.siteName = ?1")
    List<Sites> findBySiteName(String siteName);

    List<Sites> findAll();
}
