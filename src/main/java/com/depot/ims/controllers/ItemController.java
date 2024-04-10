// Package declaration
package com.depot.ims.controllers;

// Import statements
import com.depot.ims.models.Item;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.services.ItemService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ItemController class provides API endpoints for managing items within
 * the Inventory Management System.
 * This controller supports operations such as creating, updating,
 * and fetching item details.
 */
@RestController
@RequestMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173/")
public class ItemController {

    // Fields for the item repository and item service
    private final ItemRepository itemRepository;
    private final ItemService itemService;

    /**
     * Constructor for ShipmentController.
     *
     * @param itemRepository Repository for item data access.
     * @param itemService Service for item related operations.
     */
    public ItemController(ItemRepository itemRepository,
                          ItemService itemService) {
        this.itemRepository = itemRepository;
        this.itemService = itemService;
    }

    /**
     * Endpoint to retrieve all items.
     *
     * @return ResponseEntity with the list of all items.
     */
    @GetMapping
    public ResponseEntity<?> getItems() {
        return ResponseEntity.ok(this.itemRepository.findAll());
    }

    /**
     * Endpoint to add a new item.
     * Accepts item details in the form of a JSON object.
     *
     * @param item item object containing details of the new item.
     * @return The saved item entity.
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addItem(@RequestBody Item item) {
        return this.itemService.addItem(item);
    }

    /**
     * Endpoint to fetch a specific item by its ID or name.
     *
     * @param itemID The ID of the item to retrieve.
     * @param itemName the name of the item to retrieve
     * @return ResponseEntity with the details of the specified item.
     */
    @GetMapping("/item")
    public ResponseEntity<?> getItem(
            @RequestParam(value = "itemId", required = false) Long itemID,
            @RequestParam(value = "itemName", required = false) String itemName) {
        return this.itemService.getItem(itemID, itemName);
    }

    /**
     * Endpoint to update the details of an existing item.
     * This method allows partial updates to item properties.
     *
     * @param itemID ID of the item to update.
     * @param newName New item name (optional).
     * @param newPrice New item price (optional).
     * @return ResponseEntity indicating the result of the update operation.
     */
    @Modifying
    @PostMapping("/update")
    public ResponseEntity<?> updateItem(
            @RequestParam(value = "itemId")
            Long itemID,
            @RequestParam(value = "itemName", required = false)
            String newName,
            @RequestParam(value = "itemPrice", required = false)
            Double newPrice) {

        return this.itemService.updateItem(itemID, newName, newPrice);
    }

}