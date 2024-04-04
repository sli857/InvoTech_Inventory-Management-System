package com.depot.ims.controllers;

import com.depot.ims.models.Ship;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.services.ShipService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ships")
public class ShipController {

    private final ShipRepository shipRepository;
    private final ShipService shipService;

    public ShipController(ShipRepository shipRepository, ShipService shipService) {
        this.shipRepository = shipRepository;
        this.shipService = shipService;
    }

    @GetMapping
    public List<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    @GetMapping("/item={itemId}")
    public List<Ship> getShipsByItemId(@PathVariable Long itemId) {
        return shipRepository.findByItemId(itemId);
    }

    @GetMapping("/shipment={shipmentId}")
    public List<Ship> getShipsByShipmentId(@PathVariable Long shipmentId) {
        return shipRepository.findByShipmentId(shipmentId);
    }

    @GetMapping("/item={itemId}/shipment={shipmentId}")
    public List<Ship> getShipsByItemIdAndShipmentId(@PathVariable Long itemId, @PathVariable Long shipmentId) {
        return shipRepository.findByItemIdAndShipmentId(itemId, shipmentId);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addShip(@RequestBody Ship ship) {
        return shipService.addShip(ship);
    }

}
