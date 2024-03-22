package com.depot.ims.repositories;

import com.depot.ims.models.Ship;
import com.depot.ims.models.compositeKeys.ShipKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipsRepository extends JpaRepository<Ship, ShipKey> {

    List<Ship> findAll();

    @Query("select s from Ship s where s.itemId = ?1")
    List<Ship> findByItemId(Integer itemId);

    @Query("select s from Ship s where s.shipmentId = ?1")
    List<Ship> findByShipmentId(Integer shipmentId);

    @Query("select s from Ship s where s.itemId = ?1 and s.shipmentId = ?2")
    List<Ship> findByItemIdAndShipmentId(Integer itemId, Integer shipmentId);

}
