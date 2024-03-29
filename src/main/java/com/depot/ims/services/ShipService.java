package com.depot.ims.services;

import com.depot.ims.models.Item;
import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.ShipmentRepository;
import org.springframework.stereotype.Service;

@Service
public class ShipService {

    private final ShipmentRepository shipmentRepository;
    private final ItemRepository itemRepository;

    public ShipService(ShipmentRepository shipmentRepository, ItemRepository itemRepository) {
        this.shipmentRepository = shipmentRepository;
        this.itemRepository = itemRepository;
    }

    public Shipment getShipmentById(Integer shipmentId) {
        return shipmentRepository.findByShipmentId(shipmentId);
    }

    public Item getItemById(Integer itemId) {
        return itemRepository.findByItemId(itemId);
    }
}
