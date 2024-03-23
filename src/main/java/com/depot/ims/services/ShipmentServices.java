package com.depot.ims.services;

import com.depot.ims.models.request.ShipmentRequest;
import com.depot.ims.models.tables.Shipment;
import com.depot.ims.repositories.AvailabilitiesRepository;
import com.depot.ims.repositories.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class ShipmentServices {
    private AvailabilitiesRepository availabilitiesRepository;
    private ShipmentRepository shipmentRepository;
    private static final Logger logger = LoggerFactory.getLogger(ShipmentServices.class);

    @Autowired
    public void shipmentService(AvailabilitiesRepository availabilitiesRepository, ShipmentRepository shipmentRepository) {
        this.availabilitiesRepository = availabilitiesRepository;
        this.shipmentRepository = shipmentRepository;
    }

    /**
     * Adds a new shipment and updates item quantities in availabilities.
     *
     * @param shipmentRequest The request containing shipment details and item quantities.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
    @Transactional
    public ResponseEntity<?> addShipmentAndUpdateQuantities(ShipmentRequest shipmentRequest) {
        logger.debug("Adding a new shipment...");
        Shipment shipment = Shipment.builder()
                .source(shipmentRequest.getSource())
                .destination(shipmentRequest.getDestination())
                .currentLocation("on the moon")
                .departureTime(new java.sql.Timestamp(System.currentTimeMillis()))
                .estimatedArrivalTime(new java.sql.Timestamp(System.currentTimeMillis()))
                .actualArrivalTime(new java.sql.Timestamp(System.currentTimeMillis())) // how should we handle arrival time?
                .shipmentStatus("In Transit")
                .build();

        shipmentRepository.save(shipment);

        logger.debug("Updating item quantities in availabilities...");
        for (Map.Entry<Integer, Integer> entry : shipmentRequest.getItemsWithQuantities().entrySet()) {
            Integer itemId = entry.getKey();
            Integer quantity = entry.getValue();

            // Update quantities in source site
            availabilitiesRepository.updateQuantitiesSource(-quantity, shipmentRequest.getSource(), itemId);
            logger.debug("Quantity updated for item {} at source site {} (-{})", itemId, shipmentRequest.getSource(), quantity);

            // Update quantities in destination site
            availabilitiesRepository.updateQuantitiesDestination(quantity, shipmentRequest.getDestination(), itemId);
            logger.debug("Quantity updated for item {} at destination site {} (+{})", itemId, shipmentRequest.getDestination(), quantity);
        }
        logger.debug("Shipment and quantity updates completed successfully.");
        return null; // TODO: Change return value to indicate success/failure
    }

    // TODO: validate shipment siteIds match availability siteIds

}
