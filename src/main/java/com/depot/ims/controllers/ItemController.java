package com.depot.ims.controllers;

import com.depot.ims.models.Item;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.services.ItemService;
import com.depot.ims.services.ShipService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173/")
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;

    public ItemController(ItemRepository itemRepository,
                          ItemService itemService) {
        this.itemRepository = itemRepository;
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<?> getItems() {
        return ResponseEntity.ok(this.itemRepository.findAll());
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addItem(@RequestBody Item item) {
        return this.itemService.addItem(item);
    }

    @GetMapping("/item")
    public ResponseEntity<?> getItem(
            @RequestParam(value = "itemId", required = false) Long itemID,
            @RequestParam(value = "itemName", required = false) String itemName) {
        return this.itemService.getItem(itemID, itemName);
    }

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