package com.depot.ims.repositories;

import com.depot.ims.models.tables.Availability;
import com.depot.ims.models.tables.compositeKeys.AvailabilityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvailabilitiesRepository extends JpaRepository<Availability, AvailabilityKey> {

    List<Availability> findAll();

    @Query("select a from Availability a where a.siteId = ?1")
    List<Availability> findBySiteId(Integer siteId);

    @Query("select a from Availability a where a.itemId = ?1")
    List<Availability> findByItemId(Integer itemId);

    @Query("select a from Availability a where a.siteId = ?1 and a.itemId = ?2")
    Availability findBySiteIdAndItemId(Integer siteId, Integer itemId);

    @Modifying
    @Query("UPDATE Availability source_avail " +
            "SET source_avail.quantity = source_avail.quantity - ?1 " +
            "WHERE source_avail.siteId = ?2 " +
            "AND source_avail.itemId = ?3 " +
            "AND source_avail.quantity >= ?1")
    void updateQuantitiesSource(Integer quantity, Integer siteId, Integer itemId);

    @Modifying
    @Query("UPDATE Availability dest_avail " +
            "SET dest_avail.quantity = dest_avail.quantity + ?1 " +
            "WHERE dest_avail.siteId = ?2 " +
            "AND dest_avail.itemId = ?3")
    void updateQuantitiesDestination(Integer quantity, Integer siteId, Integer itemId);

}
