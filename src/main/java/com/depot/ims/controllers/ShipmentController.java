package com.depot.ims.controllers;

import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.ShipmentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentRepository shipmentRepository;

    public ShipmentController(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @GetMapping("/shipmentId={shipmentId}")
    public Shipment getShipmentById(@PathVariable Integer shipmentId) {
        return shipmentRepository.findByShipmentId(shipmentId);
    }

    @GetMapping("/source={source}")
    public List<Shipment> getShipmentsBySource(@PathVariable Integer source) {
        return shipmentRepository.findByShipmentSource(source);
    }

    @GetMapping("/destination={destination}")
    public List<Shipment> getShipmentsByDestination(@PathVariable Integer destination) {
        return shipmentRepository.findByShipmentDestination(destination);
    }

    @GetMapping("/currentLocation={currentLocation}")
    public List<Shipment> getShipmentsByCurrentLocation(@PathVariable String currentLocation) {
        return shipmentRepository.findByCurrentLocation(currentLocation);
    }

    @GetMapping("/departureTime={departureTime}")
    public List<Shipment> getShipmentsByDepartureTime(@PathVariable String departureTime) {
        return shipmentRepository.findByDepartureTime(departureTime);
    }

    @GetMapping("/estimatedArrivalTime={estimatedArrivalTime}")
    public List<Shipment> getShipmentsByEstimatedArrivalTime(@PathVariable String estimatedArrivalTime) {
        return shipmentRepository.findByEstimatedArrivalTime(estimatedArrivalTime);
    }

    @GetMapping("/actualArrivalTime={actualArrivalTime}")
    public List<Shipment> getShipmentsByActualArrivalTime(@PathVariable String actualArrivalTime) {
        return shipmentRepository.findByActualArrivalTime(actualArrivalTime);
    }

    @GetMapping("/status={shipmentStatus}")
    public List<Shipment> getShipmentsByStatus(@PathVariable String shipmentStatus) {
        return shipmentRepository.findByShipmentStatus(shipmentStatus);
    }

}
