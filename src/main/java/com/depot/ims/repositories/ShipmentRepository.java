package com.depot.ims.repositories;

import com.depot.ims.models.Shipment;
import com.depot.ims.models.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

    List<Shipment> findAll();

    @Query("select s from Shipment s where s.shipmentId = ?1")
    List<Site> findByShipmentId(Integer shipmentId);

    @Query("select s from Shipment s where s.shipmentSource = ?1")
    List<Site> findByShipmentSource(Integer shipmentSource);

    @Query("select s from Shipment s where s.shipmentDestination = ?1")
    List<Site> findByShipmentDestination(Integer shipmentDestination);

    @Query("select s from Shipment s where s.currentLocation = ?1")
    List<Site> findByCurrentLocation(String currentLocation);

    @Query("select s from Shipment s where s.departureTime = ?1")
    List<Site> findByDepartureTime(String departureTime);

    @Query("select s from Shipment s where s.estimatedArrivalTime = ?1")
    List<Site> findByEstimatedArrivalTime(String estimatedArrivalTime);

    @Query("select s from Shipment s where s.actualArrivalTime = ?1")
    List<Site> findByActualArrivalTime(String actualArrivalTime);

    @Query("select s from Shipment s where s.shipmentStatus = ?1")
    List<Site> findByShipmentStatus(String shipmentStatus);

}
