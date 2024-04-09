package com.depot.ims.repositories;

import com.depot.ims.models.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for CRUD operations and custom queries on the Shipment entity.
 * Extends JpaRepository to provide basic CRUD operations and includes additional queries
 * to find shipments based on various attributes like source, destination, and status.
 */
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    /**
     * Finds a shipment by its unique ID.
     *
     * @param shipmentId The ID of the shipment.
     * @return The found Shipment entity or null if not found.
     */
    @Query("select s from Shipment s where s.shipmentId = ?1")
    Shipment findByShipmentId(Long shipmentId);

    /**
     * Finds shipments originating from a specific source.
     *
     * @param source The ID of the source location.
     * @return A list of Shipment entities with the given source.
     */
    @Query("select s from Shipment s where s.source = ?1")
    List<Shipment> findByShipmentSource(Long source);

    /**
     * Finds the first shipment originating from a specific source.
     *
     * @param source The ID of the source location.
     * @return The first found Shipment entity with the given source or null if not found.
     */
    @Query("select s from Shipment s where s.source = ?1")
    Shipment findBySource(Long source);

    /**
     * Finds shipments destined for a specific location.
     *
     * @param destination The ID of the destination location.
     * @return A list of Shipment entities with the given destination.
     */
    @Query("select s from Shipment s where s.destination = ?1")
    List<Shipment> findByShipmentDestination(Long destination);

    /**
     * Finds shipments by their current location.
     *
     * @param currentLocation The current location of the shipments.
     * @return A list of Shipment entities at the given current location.
     */
    @Query("select s from Shipment s where s.currentLocation = ?1")
    List<Shipment> findByCurrentLocation(String currentLocation);

    /**
     * Finds shipments by their departure time.
     *
     * @param departureTime The departure time of the shipments.
     * @return A list of Shipment entities with the given departure time.
     */
    @Query("select s from Shipment s where s.departureTime = ?1")
    List<Shipment> findByDepartureTime(String departureTime);

    /**
     * Finds shipments by their estimated arrival time.
     *
     * @param estimatedArrivalTime The estimated arrival time of the shipments.
     * @return A list of Shipment entities with the given estimated arrival time.
     */
    @Query("select s from Shipment s where s.estimatedArrivalTime = ?1")
    List<Shipment> findByEstimatedArrivalTime(String estimatedArrivalTime);

    /**
     * Finds shipments by their actual arrival time.
     *
     * @param actualArrivalTime The actual arrival time of the shipments.
     * @return A list of Shipment entities with the given actual arrival time.
     */
    @Query("select s from Shipment s where s.actualArrivalTime = ?1")
    List<Shipment> findByActualArrivalTime(String actualArrivalTime);

    /**
     * Finds shipments by their status.
     *
     * @param shipmentStatus The status of the shipments (e.g., "Delivered", "In Transit").
     * @return A list of Shipment entities with the given status.
     */
    @Query("select s from Shipment s where s.shipmentStatus = ?1")
    List<Shipment> findByShipmentStatus(String shipmentStatus);

}
