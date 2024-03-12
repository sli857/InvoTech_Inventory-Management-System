package com.depot.ims.controllers;

import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.ShipmentsRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipments")
public class ShipmentsController {

    private final ShipmentsRepository shipmentsRepository;

    public ShipmentsController(ShipmentsRepository shipmentsRepository) {
        this.shipmentsRepository = shipmentsRepository;
    }

    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentsRepository.findAll();
    }

    @GetMapping("/shipmentId={shipmentId}")
    public List<Shipment> getShipmentById(@PathVariable Integer shipmentId) {
        return shipmentsRepository.findByShipmentId(shipmentId);
    }

    @GetMapping("/source={source}")
    public List<Shipment> getShipmentsBySource(@PathVariable Integer source) {
        return shipmentsRepository.findByShipmentSource(source);
    }

    @GetMapping("/destination={destination}")
    public List<Shipment> getShipmentsByDestination(@PathVariable Integer destination) {
        return shipmentsRepository.findByShipmentDestination(destination);
    }

    @GetMapping("/currentLocation={currentLocation}")
    public List<Shipment> getShipmentsByCurrentLocation(@PathVariable String currentLocation) {
        return shipmentsRepository.findByCurrentLocation(currentLocation);
    }

    @GetMapping("/departureTime={departureTime}")
    public List<Shipment> getShipmentsByDepartureTime(@PathVariable String departureTime) {
        return shipmentsRepository.findByDepartureTime(departureTime);
    }

    @GetMapping("/estimatedArrivalTime={estimatedArrivalTime}")
    public List<Shipment> getShipmentsByEstimatedArrivalTime(@PathVariable String estimatedArrivalTime) {
        return shipmentsRepository.findByEstimatedArrivalTime(estimatedArrivalTime);
    }

    @GetMapping("/actualArrivalTime={actualArrivalTime}")
    public List<Shipment> getShipmentsByActualArrivalTime(@PathVariable String actualArrivalTime) {
        return shipmentsRepository.findByActualArrivalTime(actualArrivalTime);
    }

    @GetMapping("/status={shipmentStatus}")
    public List<Shipment> getShipmentsByStatus(@PathVariable String shipmentStatus) {
        return shipmentsRepository.findByShipmentStatus(shipmentStatus);
    }

}
