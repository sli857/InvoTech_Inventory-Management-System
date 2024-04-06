package com.depot.ims.services;

import com.depot.ims.models.Item;
import com.depot.ims.repositories.AvailabilityRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.repositories.ShipmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * item class for managing Ship operations
 */
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * Constructor for itemService.
     *
     * @param itemRepository         The ItemRepository instance.
     */
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Adds a new item.
     *
     * @param item The Item object to be added.
     * @return ResponseEntity containing the result of the item addition operation.
     */
    public  ResponseEntity<?> addItem(@RequestBody Item item) {
        try{
            this.itemRepository.save(item);
            return ResponseEntity.ok(item);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    /**
     * Gets an item with the given id or name
     *
     * @Param item name or item id
     * @return ResponseEntity containing the result of the item with the given id or name.
     */
    public ResponseEntity<?> getItem(
            @RequestParam(value = "itemId", required = false) Long itemID,
            @RequestParam(value = "itemName", required = false) String itemName) {
        if (itemID != null) {
            return ResponseEntity.ok(itemRepository.findByItemId(itemID));
        } else if (itemName != null) {
            return ResponseEntity.ok(itemRepository.findByItemName(itemName));
        } else {
            return ResponseEntity.badRequest().body("Either itemId or itemName must be provided");
        }
    }

    /**
     * Updates an item with the given attributes
     *
     * @Param item name or item id
     * @return ResponseEntity containing the result of the item with the given id or name.
     */
    public ResponseEntity<?> updateItem(
            @RequestParam(value = "itemId")
            Long itemID,
            @RequestParam(value = "itemName", required = false)
            String newName,
            @RequestParam(value = "itemPrice", required = false)
            Double newPrice) {

        if (!itemRepository.existsById(itemID)) {
            return ResponseEntity.badRequest().body("item not found by itemId");
        }
        if (Stream.of(newName, newPrice).allMatch(Objects::isNull)) {
            return ResponseEntity.badRequest().body("No value for this update is specified.");
        }
        Item item = itemRepository.findByItemId(itemID);
        if (newName != null) item.setItemName(newName);
        if (newPrice != null) item.setItemPrice(newPrice);

        Item updatedItem = itemRepository.save(item);
        return ResponseEntity.ok(updatedItem);

    }





}
