package com.depot.ims.repositories;

import com.depot.ims.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.itemName = ?1")
    List<Item> findByItemName(String itemName);

    @Query("SELECT i FROM Item i WHERE i.itemId = ?1")
    Item findByItemId(Long itemId);

}
