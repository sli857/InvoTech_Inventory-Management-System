package com.depot.ims.services;

import com.depot.ims.models.Item;
import com.depot.ims.models.Ship;
import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.AvailabilitiesRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.repositories.ShipmentRepository;
import org.springframework.stereotype.Service;

@Service
public class ShipService {
    private final ShipRepository shipRepository;
    private final ShipmentRepository shipmentRepository;
    private final ItemRepository itemRepository;
    private final AvailabilitiesRepository availabilitiesRepository;

    public ShipService(ShipRepository shipRepository,
                       ShipmentRepository shipmentRepository,
                       ItemRepository itemRepository,
                       AvailabilitiesRepository availabilitiesRepository) {
        this.shipRepository = shipRepository;
        this.shipmentRepository = shipmentRepository;
        this.itemRepository = itemRepository;
        this.availabilitiesRepository = availabilitiesRepository;
    }

    public Shipment getShipmentById(Integer shipmentId) {
        return shipmentRepository.findByShipmentId(shipmentId);
    }

    public Item getItemById(Integer itemId) {
        return itemRepository.findByItemId(itemId);
    }

    public Ship addShip(Ship ship) {
        Shipment source = shipmentRepository.findByShipmentId(ship.getShipmentId().getSource());
        Shipment destination = shipmentRepository.findByShipmentId(ship.getShipmentId().getDestination());
        Item item = itemRepository.findByItemId(ship.getItemId());
        Integer quantity = ship.getQuantity();

        // Validate
        if (source == null || destination == null || item == null) {
            return null;
        }

        // Check if the quantity of the item in the source site is enough
        if (availabilitiesRepository.findBySiteIdAndItemId(
                ship.getShipmentId().getSource(),
                ship.getItemId()
        ).getQuantity() < quantity) {
            return null;
        }

        // Update the quantity of the item in the source site
        availabilitiesRepository.findBySiteIdAndItemId(
                ship.getShipmentId().getSource(),
                ship.getItemId()
        ).setQuantity(
                availabilitiesRepository.findBySiteIdAndItemId(
                        ship.getShipmentId().getSource(),
                        ship.getItemId()
                ).getQuantity() - quantity
        );

        // Update the quantity of the item in the destination site
        availabilitiesRepository.findBySiteIdAndItemId(
                ship.getShipmentId().getDestination(),
                ship.getItemId()
        ).setQuantity(
                availabilitiesRepository.findBySiteIdAndItemId(
                        ship.getShipmentId().getDestination(),
                        ship.getItemId()
                ).getQuantity() + quantity
        );

        // Save the changes
        shipRepository.save(ship);
        return ship;
    }
}
