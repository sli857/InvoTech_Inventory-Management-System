package com.depot.ims.repositories;

import com.depot.ims.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Item,Integer> {
    List<Item> findAll();

    @Query("SELECT i FROM Item i WHERE i.itemName = ?1")
    List<Item> findItemsByItemName(String item_name);
}
