package com.depot.ims.controllers;

import com.depot.ims.models.request.ShipmentRequest;
import com.depot.ims.models.tables.Shipment;
import com.depot.ims.repositories.ShipmentRepository;
import com.depot.ims.services.ShipmentServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentServices shipmentServices;

    public ShipmentController(ShipmentRepository shipmentRepository, ShipmentServices shipmentServices) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentServices = shipmentServices;
    }

    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @GetMapping("/shipmentId={shipmentId}")
    public List<Shipment> getShipmentById(@PathVariable Integer shipmentId) {
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

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addShipment(@RequestBody ShipmentRequest shipmentRequest) {
        ResponseEntity<?> responseEntity = shipmentServices.addShipmentAndUpdateQuantities(shipmentRequest);
        return Objects.requireNonNullElseGet(responseEntity, () -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add shipment and update quantities."));
    }

}
