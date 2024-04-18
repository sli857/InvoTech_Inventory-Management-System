package com.depot.ims.services;

import com.depot.ims.models.Item;
import com.depot.ims.models.Ship;
import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.AvailabilityRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.repositories.ShipmentRepository;
import com.depot.ims.requests.ShipRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service class for managing Ship operations
 */
@Service
public class ShipService {
    private final ShipRepository shipRepository;
    private final ShipmentRepository shipmentRepository;
    private final ItemRepository itemRepository;
    private final AvailabilityRepository availabilityRepository;

    /**
     * Constructor for ShipService.
     *
     * @param shipRepository         The ShipRepository instance.
     * @param shipmentRepository     The ShipmentRepository instance.
     * @param itemRepository         The ItemRepository instance.
     * @param availabilityRepository The AvailabilityRepository instance.
     */
    public ShipService(ShipRepository shipRepository,
                       ShipmentRepository shipmentRepository,
                       ItemRepository itemRepository,
                       AvailabilityRepository availabilityRepository) {
        this.shipRepository = shipRepository;
        this.shipmentRepository = shipmentRepository;
        this.itemRepository = itemRepository;
        this.availabilityRepository = availabilityRepository;
    }

    /**
     * Adds a new Ship object to the database and updates the quantity of the item in the source and destination sites.
     *
     * @param shipRequest The ShipRequest object representing the Ship object to be added.
     * @return ResponseEntity containing the result of the ship addition operation.
     */
    public ResponseEntity<?> addShip(ShipRequest shipRequest) {
        // Validate
        if (shipRequest == null || shipRequest.getShipmentId() == null || shipRequest.getItemId() == null || shipRequest.getQuantity() == null) {
            return ResponseEntity.badRequest().body("Invalid shipRequest");
        }
        // Check if the shipment, source, destination, and item exist in the database
        Shipment shipment = shipmentRepository.findByShipmentId(shipRequest.getShipmentId());
        Item item = itemRepository.findByItemId(shipRequest.getItemId());

        if (shipment == null || item == null) {
            return ResponseEntity.badRequest().body(
                    String.format("Shipment or item do not exist%nShipment: %s%nItem: %s", shipment, item)
            );
        }

        // Create a new Ship object
        Ship ship = Ship.builder()
                .shipmentId(shipment)
                .itemId(item)
                .quantity(shipRequest.getQuantity())
                .build();

        // Check if the quantity of the item in the source site is enough
        if (availabilityRepository.findBySiteIdAndItemId(
                ship.getShipmentId().getSource(),
                ship.getItemId().getItemId()
        ).getQuantity() < ship.getQuantity()) {
            return ResponseEntity.badRequest().body(
                    String.format("Not enough quantity for %d in the source site", shipRequest.getQuantity())
            );
        }

        // Update the quantity of the item in the source site
        availabilityRepository.findBySiteIdAndItemId(
                ship.getShipmentId().getSource(),
                ship.getItemId().getItemId()
        ).setQuantity(
                availabilityRepository.findBySiteIdAndItemId(
                        ship.getShipmentId().getSource(),
                        ship.getItemId().getItemId()
                ).getQuantity() - ship.getQuantity()
        );

        // Update the quantity of the item in the destination site
        availabilityRepository.findBySiteIdAndItemId(
                ship.getShipmentId().getDestination(),
                ship.getItemId().getItemId()
        ).setQuantity(
                availabilityRepository.findBySiteIdAndItemId(
                        ship.getShipmentId().getDestination(),
                        ship.getItemId().getItemId()
                ).getQuantity() + ship.getQuantity()
        );

        // Save the changes
        return ResponseEntity.ok().body(shipRepository.save(ship));
    }

}
