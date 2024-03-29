package com.depot.ims.services;

import com.depot.ims.models.Item;
import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.ItemsRepository;
import com.depot.ims.repositories.ShipmentsRepository;
import org.springframework.stereotype.Service;

@Service
public class ShipService {

    private final ShipmentsRepository shipmentsRepository;
    private final ItemsRepository itemsRepository;

    public ShipService(ShipmentsRepository shipmentsRepository, ItemsRepository itemsRepository) {
        this.shipmentsRepository = shipmentsRepository;
        this.itemsRepository = itemsRepository;
    }

    public Shipment getShipmentById(Integer shipmentId) {
        return shipmentsRepository.findByShipmentId(shipmentId);
    }

    public Item getItemById(Integer itemId) {
        return itemsRepository.findByItemId(itemId);
    }
}
