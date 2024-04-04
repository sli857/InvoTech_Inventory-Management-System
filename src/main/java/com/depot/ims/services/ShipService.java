package com.depot.ims.services;

import com.depot.ims.models.Item;
import com.depot.ims.models.Ship;
import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.AvailabilityRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.repositories.ShipmentRepository;
import org.springframework.stereotype.Service;

@Service
public class ShipService {
    private final ShipRepository shipRepository;
    private final ShipmentRepository shipmentRepository;
    private final ItemRepository itemRepository;
    private final AvailabilityRepository availabilityRepository;

    public ShipService(ShipRepository shipRepository,
                       ShipmentRepository shipmentRepository,
                       ItemRepository itemRepository,
                       AvailabilityRepository availabilityRepository) {
        this.shipRepository = shipRepository;
        this.shipmentRepository = shipmentRepository;
        this.itemRepository = itemRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public Ship addShip(Ship ship) {
        // Validate
        // Check if the shipment, source, destination, and item exist in the database
        Shipment source = shipmentRepository.findByShipmentId(ship.getShipmentId().getSource());
        Shipment destination = shipmentRepository.findByShipmentId(ship.getShipmentId().getDestination());
        Item item = itemRepository.findByItemId(ship.getItemId().getItemId());

        if (source == null || destination == null || item == null) {
            return null;
        }

        // Check if the quantity of the item in the source site is enough
        if (availabilityRepository.findBySiteIdAndItemId(
                ship.getShipmentId().getSource(),
                ship.getItemId().getItemId()
        ).getQuantity() < ship.getQuantity()) {
            return null;
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
        return shipRepository.save(ship);
    }

}
