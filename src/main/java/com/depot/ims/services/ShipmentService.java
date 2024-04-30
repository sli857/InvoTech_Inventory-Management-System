package com.depot.ims.services;

import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.ShipmentRepository;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * ShipmentService provides business logic for handling shipments within the Inventory Management
 * System. This includes operations like updating, retrieving, and deleting shipment records.
 */
@Service
public class ShipmentService {
  private final ShipmentRepository shipmentRepository;
  private final AuditService auditService;

  /**
   * Constructs a ShipmentService with a repository for data access.
   *
   * @param shipmentRepository The repository providing data access operations for shipments.
   */
  public ShipmentService(ShipmentRepository shipmentRepository, AuditService auditService) {

    this.shipmentRepository = shipmentRepository;
    this.auditService = auditService;
  }

  /**
   * Updates the details of an existing shipment using its ID. Allows for partial updates where only
   * specified fields are updated.
   *
   * @param shipmentId The ID of the shipment to update.
   * @param newSource The new source location ID (nullable).
   * @param newDestination The new destination location ID (nullable).
   * @param newCurrentLocation The new current location (nullable).
   * @param newDepartureTime The new departure time (nullable).
   * @param newEstimatedArrivalTime The new estimated arrival time (nullable).
   * @param newActualArrivalTime The new actual arrival time (nullable).
   * @param newShipmentStatus The new shipment status (nullable).
   * @return ResponseEntity<?> Returns OK with the updated shipment or Bad Request on error.
   */
  public ResponseEntity<?> updateShipment(
      Long shipmentId,
      Long newSource,
      Long newDestination,
      String newCurrentLocation,
      Timestamp newDepartureTime,
      Timestamp newEstimatedArrivalTime,
      Timestamp newActualArrivalTime,
      String newShipmentStatus) {

    if (!shipmentRepository.existsById(shipmentId)) {
      return ResponseEntity.badRequest().body("Shipment not found by shipment id!");
    }

    // Check if any update parameter is provided
    if (Stream.of(
            newSource,
            newDestination,
            newCurrentLocation,
            newDepartureTime,
            newEstimatedArrivalTime,
            newActualArrivalTime,
            newShipmentStatus)
        .allMatch(Objects::isNull)) {
      return ResponseEntity.badRequest().body("No value for this update is specified");
    }

    // Apply non-null updates to the shipment
    Shipment shipment = shipmentRepository.findByShipmentId(shipmentId);
    if (newSource != null) {
      auditService.saveAudit(
          "Shipments",
          "source",
          shipment.getShipmentId().toString(),
          shipment.getSource().toString(),
          newSource.toString(),
          "UPDATE");
      shipment.setSource(newSource);
    }
    if (newDestination != null) {
      auditService.saveAudit(
          "Shipments",
          "destination",
          shipment.getShipmentId().toString(),
          shipment.getDestination().toString(),
          newDestination.toString(),
          "UPDATE");
      shipment.setDestination(newDestination);
    }
    if (newCurrentLocation != null) {
      auditService.saveAudit(
          "Shipments",
          "currentLocation",
          shipment.getShipmentId().toString(),
          shipment.getCurrentLocation(),
          newCurrentLocation,
          "UPDATE");

      shipment.setCurrentLocation(newCurrentLocation);
    }
    if (newDepartureTime != null) {
      auditService.saveAudit(
          "Shipments",
          "departureTime",
          shipment.getShipmentId().toString(),
          shipment.getDepartureTime() == null ? null : shipment.getDepartureTime().toString(),
          newDepartureTime.toString(),
          "UPDATE");
      shipment.setDepartureTime(newDepartureTime);
    }
    if (newEstimatedArrivalTime != null) {
      auditService.saveAudit(
          "Shipments",
          "estimatedArrivalTime",
          shipment.getShipmentId().toString(),
          shipment.getEstimatedArrivalTime() == null
              ? null
              : shipment.getEstimatedArrivalTime().toString(),
          newEstimatedArrivalTime.toString(),
          "UPDATE");
      shipment.setEstimatedArrivalTime(newEstimatedArrivalTime);
    }
    if (newActualArrivalTime != null) {
      auditService.saveAudit(
          "Shipments",
          "actualArrivalTime",
          shipment.getShipmentId().toString(),
          shipment.getActualArrivalTime() == null
              ? null
              : shipment.getActualArrivalTime().toString(),
          newActualArrivalTime.toString(),
          "UPDATE");

      shipment.setActualArrivalTime(newActualArrivalTime);
    }
    if (newShipmentStatus != null) {
      auditService.saveAudit(
          "Shipments",
          "shipmentStatus",
          shipment.getShipmentId().toString(),
          shipment.getShipmentStatus(),
          newDestination.toString(),
          "UPDATE");

      shipment.setShipmentStatus(newShipmentStatus);
    }

    Shipment updatedShipment = shipmentRepository.save(shipment);
    return ResponseEntity.ok(updatedShipment);
  }

  /**
   * Retrieves the details of a shipment by its ID.
   *
   * @param shipmentId The ID of the shipment to find.
   * @return ResponseEntity<?> Returns OK with the shipment details or Bad Request if ID not
   *     provided.
   */
  public ResponseEntity<?> getShipment(Long shipmentId) {
    // shipment id has to be unique!!
    if (shipmentId != null) {
      return ResponseEntity.ok(shipmentRepository.findByShipmentId(shipmentId));
    } else {
      return ResponseEntity.badRequest().body("You have to provide the shipment Id");
    }
  }

  /**
   * Deletes a shipment using its ID.
   *
   * @param shipmentId The ID of the shipment to delete.
   * @return ResponseEntity<?> Returns OK if deletion was successful, or Bad Request on error.
   */
  public ResponseEntity<?> deleteShipment(Long shipmentId) {
    try {
      boolean isFound = shipmentRepository.existsById(shipmentId);
      if (isFound) {
        var res = shipmentRepository.findByShipmentId(shipmentId);
        auditService.saveAudit(
            "Shipments", null, shipmentId.toString(), res.toString(), null, "DELETE");
        shipmentRepository.deleteById(shipmentId);
        return ResponseEntity.ok().body("Successfully deleted");
      }
      return ResponseEntity.badRequest().body("Shipment not found by shipment Id");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("Shipment Id cannot be null");
    }
  }
}
