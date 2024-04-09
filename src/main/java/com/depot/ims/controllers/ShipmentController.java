// Package declaration
package com.depot.ims.controllers;

// Import statements
import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.ShipmentRepository;
import com.depot.ims.services.ShipmentService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;


/**
 * ShipmentController class provides API endpoints for managing shipments within
 * the Inventory Management System.
 * This controller supports operations such as creating, updating, deleting,
 * and fetching shipment details.
 */

@RestController
@RequestMapping(value = "/shipments", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173/")
public class ShipmentController {

    // Fields for the shipment repository and shipment service
    private final ShipmentRepository shipmentRepository;
    private final ShipmentService shipmentService;

    /**
     * Constructor for ShipmentController.
     *
     * @param shipmentsRepository Repository for shipment data access.
     * @param shipmentService Service for shipment related operations.
     */
    public ShipmentController(ShipmentRepository shipmentsRepository,
                              ShipmentService shipmentService) {
        this.shipmentRepository = shipmentsRepository;
        this.shipmentService = shipmentService;
    }

    /**
     * Endpoint to retrieve all shipments.
     *
     * @return ResponseEntity with the list of all shipments.
     */
    @GetMapping
    public ResponseEntity<?> getAllShipments() {

        return ResponseEntity.ok(this.shipmentRepository.findAll());
    }

    /**
     * Endpoint to fetch a specific shipment by its ID.
     *
     * @param shipmentId The ID of the shipment to retrieve.
     * @return ResponseEntity with the details of the specified shipment.
     */
    @GetMapping("/shipment")
    public ResponseEntity<?> getShipment(
            @RequestParam(value = "shipmentId", required = false) Long shipmentId) {
        return this.shipmentService.getShipment(shipmentId);
    }

    /**
     * Endpoint to update the details of an existing shipment.
     * This method allows partial updates to shipment properties.
     *
     * @param shipmentId ID of the shipment to update.
     * @param newSource New source location ID (optional).
     * @param newDestination New destination location ID (optional).
     * @param newCurrentLocation New current location (optional).
     * @param newDepartureTime New departure time (optional).
     * @param newEstimatedArrivalTime New estimated arrival time (optional).
     * @param newActualArrivalTime New actual arrival time (optional).
     * @param newShipmentStatus New status of the shipment (optional).
     * @return ResponseEntity indicating the result of the update operation.
     */
    @Modifying
    @PostMapping("/update")
    public ResponseEntity<?> updateShipment(
            @RequestParam(value = "shipmentId")
            Long shipmentId,
            @RequestParam(value = "source", required = false)
            Long newSource,
            @RequestParam(value = "destination", required = false)
            Long newDestination,
            @RequestParam(value = "currentLocation", required = false)
            String newCurrentLocation,
            @RequestParam(value = "departureTime", required = false)
            Timestamp newDepartureTime,
            @RequestParam(value = "estimatedArrivalTime", required = false)
            Timestamp newEstimatedArrivalTime,
            @RequestParam(value = "actualArrivalTime", required = false)
            Timestamp newActualArrivalTime,
            @RequestParam(value = "shipmentStatus", required = false)
            String newShipmentStatus
    ) {
        return this.shipmentService.updateShipment(shipmentId, newSource, newDestination,
                newCurrentLocation, newDepartureTime,
                newEstimatedArrivalTime, newActualArrivalTime,
                newShipmentStatus);
    }

    /**
     * Endpoint to add a new shipment.
     * Accepts shipment details in the form of a JSON object.
     *
     * @param shipment Shipment object containing details of the new shipment.
     * @return The saved Shipment entity.
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Shipment addShipment(@RequestBody Shipment shipment) {

        return this.shipmentRepository.save(shipment);
    }

    /**
     * Endpoint to delete a shipment by its ID.
     *
     * @param shipmentId The ID of the shipment to delete.
     * @return ResponseEntity indicating the result of the delete operation.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteShipment(@RequestParam("shipmentId") Long shipmentId) {
        return this.shipmentService.deleteShipment(shipmentId);
    }

}
