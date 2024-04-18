package com.depot.ims.controllers;

import com.depot.ims.models.Ship;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.requests.ShipRequest;
import com.depot.ims.services.ShipService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ShipController class provides API endpoints for managing ships within
 * the Inventory Management System.
 * This controller supports operations such as creating, updating,
 * and fetching ship details.
 */
@RestController
@RequestMapping("/ships")
@CrossOrigin(origins = "http://localhost:5173/")
public class ShipController {

    // Fields for the ship repository and ship service
    private final ShipRepository shipRepository;
    private final ShipService shipService;

    /**
     * Constructor for ShipController.
     *
     * @param shipRepository Repository for ship data access.
     * @param shipService Service for ship related operations.
     */
    public ShipController(ShipRepository shipRepository, ShipService shipService) {
        this.shipRepository = shipRepository;
        this.shipService = shipService;
    }

    /**
     * Endpoint to retrieve all ships.
     *
     * @return List of all ships.
     */
    @GetMapping
    public List<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    /**
     * Endpoint to retrieve all ships by item ID.
     *
     * @param itemId The ID of the item.
     * @return List of all ships with the specified item ID.
     */
    @GetMapping("/item={itemId}")
    public List<Ship> getShipsByItemId(@PathVariable Long itemId) {
        return shipRepository.findByItemId(itemId);
    }

    /**
     * Endpoint to retrieve all ships by shipment ID.
     *
     * @param shipmentId The ID of the shipment.
     * @return List of all ships with the specified shipment ID.
     */
    @GetMapping("/shipment={shipmentId}")
    public List<Ship> getShipsByShipmentId(@PathVariable Long shipmentId) {
        return shipRepository.findByShipmentId(shipmentId);
    }

    /**
     * Endpoint to retrieve all ships by item ID and shipment ID.
     *
     * @param itemId The ID of the item.
     * @param shipmentId The ID of the shipment.
     * @return List of all ships with the specified item ID and shipment ID.
     */
    @GetMapping("/item={itemId}/shipment={shipmentId}")
    public List<Ship> getShipsByItemIdAndShipmentId(@PathVariable Long itemId, @PathVariable Long shipmentId) {
        return shipRepository.findByItemIdAndShipmentId(itemId, shipmentId);
    }

    /**
     * Endpoint to add a new ship.
     * Accepts ship details in the form of a JSON object.
     *
     * @param shipRequest Ship object containing details of the new ship.
     * @return ResponseEntity with the result of the ship addition operation.
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addShip(@RequestBody ShipRequest shipRequest) {
        return shipService.addShip(shipRequest);
    }

}
