package com.depot.ims.repositories;

import com.depot.ims.models.Site;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SitesRepository extends JpaRepository<Site, Integer>{

//    @Query("select s from Sites s where s.siteName = ?1")
//    List<Site> findBySiteName(String siteName);
//
//    List<Site> findAll();
}
