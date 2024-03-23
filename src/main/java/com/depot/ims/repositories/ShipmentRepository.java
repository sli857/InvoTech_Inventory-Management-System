package com.depot.ims.repositories;

import com.depot.ims.models.tables.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findAll();

    @Query("select s from Shipment s where s.shipmentId = ?1")
    List<Shipment> findByShipmentId(Integer shipmentId);

    @Query("select s from Shipment s where s.source = ?1")
    List<Shipment> findByShipmentSource(Integer source);

    @Query("select s from Shipment s where s.destination = ?1")
    List<Shipment> findByShipmentDestination(Integer destination);

    @Query("select s from Shipment s where s.currentLocation = ?1")
    List<Shipment> findByCurrentLocation(String currentLocation);

    @Query("select s from Shipment s where s.departureTime = ?1")
    List<Shipment> findByDepartureTime(String departureTime);

    @Query("select s from Shipment s where s.estimatedArrivalTime = ?1")
    List<Shipment> findByEstimatedArrivalTime(String estimatedArrivalTime);

    @Query("select s from Shipment s where s.actualArrivalTime = ?1")
    List<Shipment> findByActualArrivalTime(String actualArrivalTime);

    @Query("select s from Shipment s where s.shipmentStatus = ?1")
    List<Shipment> findByShipmentStatus(String shipmentStatus);

}
