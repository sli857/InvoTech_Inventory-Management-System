package com.depot.ims.repositories;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Repository
public interface AvailabilitiesRepository extends JpaRepository<Availability, Integer> {

    List<Availability> findAll();

    //find all the availabilities with the given siteId
    @Query("select a from Availability a where a.siteId = ?1")
    List<Availability> findBySiteId(Site siteId);

    //find all the availabilities with the given itemId
    @Query("select a from Availability a where a.itemId = ?1")
    List<Availability> findByItemId(Item itemId);

    //find the availability with the given itemId and given siteId
    @Query("select a from Availability a where a.siteId = ?1 and a.itemId = ?2")
    Availability findBySiteIdAndItemId(Site siteId, Item itemId);

    @Query("SELECT a.siteId from Availability a where a.itemId IN :itemList")
    List<Site> findSitesByItems(@Param("itemList") List<Item> itemList);

    @Query("SELECT a.siteId from Availability a where a.itemId = :item")
    List<Site> findSitesByOneItem(@Param("item") Item item);



}
