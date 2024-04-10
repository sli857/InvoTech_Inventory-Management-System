package com.depot.ims.repositories;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Availability Repository interface for accessing and manipulating Availability entity data.
 * Extends JpaRepository to provide standard CRUD operations and includes custom
 * queries for finding availabilities by their ID.
 */
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    /**
     * find all the availabilities with the given siteId
     * Uses a custom JPQL query to retrieve a list of availability.
     *
     * @param siteId The ID of the site
     * @return a list of availability entity if found, or null otherwise
     */
    @Query("select a from Availability a where a.siteId.siteId = ?1")
    List<Availability> findBySiteId(Long siteId);

    /**
     * find all the availabilities with the given itemId
     * Uses a custom JPQL query to retrieve a list of availability.
     *
     * @param itemId The ID of the item
     * @return a list of availability entity if found, or null otherwise
     */
    @Query("select a from Availability a where a.itemId.itemId = ?1")
    List<Availability> findByItemId(Long itemId);

    /**
     * find the availability with the given itemId and given siteId
     * Uses a custom JPQL query to retrieve a specific availability.
     *
     * @param siteId The ID of the site
     * @param itemId the id of the item
     * @return availability entity if found, or null otherwise.
     */
    @Query("select a from Availability a where a.siteId.siteId = ?1 and a.itemId.itemId = ?2")
    Availability findBySiteIdAndItemId(Long siteId, Long itemId);

    /**
     * find list of sites that contains all the items in the item list
     * Uses a custom JPQL query to retrieve a specific availability.
     *
     * @param itemList The list of item entity
     * @return a list of site entity if found, or null otherwise.
     */
    @Query("SELECT a.siteId from Availability a where a.itemId IN :itemList")
    List<Site> findSitesByItems(@Param("itemList") List<Item> itemList);

    /**
     * find list of sites that contains the item
     * Uses a custom JPQL query to retrieve a specific availability.
     *
     * @param item The item entity
     * @return a list of site entity if found, or null otherwise.
     */
    @Query("SELECT a.siteId from Availability a where a.itemId = :item")
    List<Site> findSitesByOneItem(@Param("item") Item item);

}
