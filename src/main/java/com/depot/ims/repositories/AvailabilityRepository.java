package com.depot.ims.repositories;

import com.depot.ims.models.Availability;
import com.depot.ims.models.compositeKeys.AvailabilityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, AvailabilityKey> {
    @Query("select a from Availability a where a.siteId = ?1")
    List<Availability> findBySiteId(Long siteId);

    @Query("select a from Availability a where a.itemId = ?1")
    List<Availability> findByItemId(Long itemId);

    @Query("select a from Availability a where a.siteId = ?1 and a.itemId = ?2")
    Availability findBySiteIdAndItemId(Long siteId, Long itemId);

}
