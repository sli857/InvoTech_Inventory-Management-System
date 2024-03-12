package com.depot.ims.repositories;

import com.depot.ims.models.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {

    List<Availability> findAll();

    @Query("select a from Availability a where a.siteId = ?1")
    List<Availability> findBySiteId(Integer siteId);

    @Query("select a from Availability a where a.itemId = ?1")
    List<Availability> findByItemId(Integer itemId);

}
