package com.depot.ims.repositories;

import com.depot.ims.models.Ship;
import com.depot.ims.models.compositeKeys.ShipKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship, ShipKey> {

    @Query("select s from Ship s where s.itemId.itemId = ?1")
    List<Ship> findByItemId(Long itemId);

    @Query("select s from Ship s where s.shipmentId.shipmentId = ?1")
    List<Ship> findByShipmentId(Long shipmentId);

    @Query("select s from Ship s where s.itemId.itemId = ?1 and s.shipmentId.shipmentId = ?2")
    List<Ship> findByItemIdAndShipmentId(Long itemId, Long shipmentId);

}
