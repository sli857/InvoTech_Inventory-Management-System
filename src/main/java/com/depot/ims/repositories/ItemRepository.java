package com.depot.ims.repositories;

import com.depot.ims.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Item Repository interface for accessing and manipulating item entity data.
 * Extends JpaRepository to provide standard CRUD operations and includes custom
 * queries for finding item by their ID.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * find all the items with the given itemName
     * Uses a custom JPQL query to retrieve the user.
     *
     * @param itemName The name of the site
     * @return a list of item entity if found, or null otherwise.
     */
    @Query("SELECT i FROM Item i WHERE i.itemName = ?1")
    List<Item> findByItemName(String itemName);

    /**
     * find the item with the given item id
     * Uses a custom JPQL query to retrieve the user.
     *
     * @param itemId The id of the item
     * @return item entity if found, or null otherwise.
     */
    @Query("SELECT i FROM Item i WHERE i.itemId = ?1")
    Item findByItemId(Long itemId);

}
