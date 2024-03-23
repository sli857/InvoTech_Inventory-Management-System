package com.depot.ims.controllers;

import com.depot.ims.models.tables.Ship;
import com.depot.ims.repositories.ShipsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ships")
public class ShipsController {

    private final ShipsRepository shipsRepository;

    public ShipsController(ShipsRepository shipsRepository) {
        this.shipsRepository = shipsRepository;
    }

    @GetMapping
    public List<Ship> getAllShips() {
        return shipsRepository.findAll();
    }

    @GetMapping("/item={itemId}")
    public List<Ship> getShipsByItemId(@PathVariable Integer itemId) {
        return shipsRepository.findByItemId(itemId);
    }

    @GetMapping("/shipment={shipmentId}")
    public List<Ship> getShipsByShipmentId(@PathVariable Integer shipmentId) {
        return shipsRepository.findByShipmentId(shipmentId);
    }

    @GetMapping("/item={itemId}/shipment={shipmentId}")
    public List<Ship> getShipsByItemIdAndShipmentId(@PathVariable Integer itemId, @PathVariable Integer shipmentId) {
        return shipsRepository.findByItemIdAndShipmentId(itemId, shipmentId);
    }

}
