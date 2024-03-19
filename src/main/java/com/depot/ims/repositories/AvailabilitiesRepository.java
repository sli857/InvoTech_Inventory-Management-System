package com.depot.ims.repositories;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public interface AvailabilitiesRepository extends JpaRepository<Availability, Integer> {

    List<Availability> findAll();

    //display everything in the inventory
    //need to change to Site siteId
    @Query("select a from Availability a where a.siteId = ?1")
    List<Availability> findBySiteId(Site siteId);

    //gathering all inventory of a single site and returning to the front end.
    @Query("select a from Availability a where a.itemId = ?1")
    List<Availability> findByItemId(Item itemId);

    @Query("select a from Availability a where a.siteId = ?1 and a.itemId = ?2")
    Availability findBySiteIdAndItemId(Site siteId, Item itemId);

    //This could be 1, 2, 3, ... N number of sites and the inventory should be aggregated.
    @Query("SELECT SUM(a.quantity) FROM Availability a WHERE a.siteId IN :siteList AND a.itemId = :itemId")
    int findCombineLevel(@Param("siteList") List<Site> siteList, @Param("itemId") Item itemId);

    //a facility/corporate manager should be able to choose 2 sites and view the difference in.
    @Query("SELECT (a.quantity - a1.quantity) FROM Availability a, Availability a1 WHERE a.siteId IN :site1 AND a.itemId = :item AND a1.siteId IN :site2 AND a1.itemId = :item")
    int findDiffLevel(Site site1, Site site2, Item item);













}
