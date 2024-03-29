package com.depot.ims.controllers;

import com.depot.ims.models.Item;
import com.depot.ims.models.Ship;
import com.depot.ims.models.Shipment;
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
    public List<Ship> getShipsByItemId(@PathVariable Item itemId) {
        // insert service call to find by item based on passed in Long itemId
        return shipsRepository.findByItemId(itemId);
    }

    @GetMapping("/shipment={shipmentId}")
    public List<Ship> getShipsByShipmentId(@PathVariable Shipment shipmentId) {
        // insert service call to find by shipment based on passed in Long shipmentId
        return shipsRepository.findByShipmentId(shipmentId);
    }

    @GetMapping("/item={itemId}/shipment={shipmentId}")
    public List<Ship> getShipsByItemIdAndShipmentId(@PathVariable Item itemId, @PathVariable Shipment shipmentId) {
        // insert service call to find by item and shipment based on passed in Long itemId and Long shipmentId
        return shipsRepository.findByItemIdAndShipmentId(itemId, shipmentId);
    }

}
