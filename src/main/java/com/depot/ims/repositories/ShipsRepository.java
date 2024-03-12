package com.depot.ims.repositories;

import com.depot.ims.models.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShipsRepository extends JpaRepository<Ship, Integer> {

    List<Ship> findAll();

    @Query("select s from Ship s where s.itemId = ?1")
    List<Ship> findByItemId(Integer itemId);

    @Query("select s from Ship s where s.shipmentId = ?1")
    List<Ship> findByShipmentId(Integer shipmentId);

    @Query("select s from Ship s where s.itemId = ?1 and s.shipmentId = ?2")
    List<Ship> findByItemIdAndShipmentId(Integer itemId, Integer shipmentId);

}
