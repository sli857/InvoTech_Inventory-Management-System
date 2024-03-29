package com.depot.ims.controllers;

import com.depot.ims.models.Ship;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.services.ShipService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Ship> getShipsByItemId(@PathVariable Integer itemId) {
        return shipRepository.findByItemId(shipService.getItemById(itemId));
    }

    @GetMapping("/shipment={shipmentId}")
    public List<Ship> getShipsByShipmentId(@PathVariable Integer shipmentId) {
        return shipRepository.findByShipmentId(shipService.getShipmentById(shipmentId));
    }

    @GetMapping("/item={itemId}/shipment={shipmentId}")
    public List<Ship> getShipsByItemIdAndShipmentId(@PathVariable Integer itemId, @PathVariable Integer shipmentId) {
        return shipRepository.findByItemIdAndShipmentId(
                shipService.getItemById(itemId),
                shipService.getShipmentById(shipmentId)
        );
    }
}
