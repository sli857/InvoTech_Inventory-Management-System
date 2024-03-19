package com.depot.ims.controllers;

import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import com.depot.ims.repositories.ItemsRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping("/items")
public class ItemsController {
    private final ItemsRepository itemsRepository;
    public ItemsController(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @GetMapping
    public ResponseEntity<?> getItems() {return ResponseEntity.ok(this.itemsRepository.findAll());}

    @PostMapping(value = "/addItem", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Item addItem(@RequestBody Item item) {
        return this.itemsRepository.save(item);
    }

    @GetMapping("/item")
    public ResponseEntity<?> getItem(
            @RequestParam(value = "itemId", required = false) Long itemID,
            @RequestParam(value = "itemName", required = false) String itemName) {
        if (itemID != null) {
            return ResponseEntity.ok(itemsRepository.findByItemId(itemID));
        } else if (itemName != null) {
            return ResponseEntity.ok(itemsRepository.findByItemName(itemName));
        } else {
            return ResponseEntity.badRequest().body("Either itemId or itemName must be provided");
        }
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

        if (!itemsRepository.existsById(itemID)) {
            return ResponseEntity.badRequest().body("item not found by itemId");
        }
        if (Stream.of( newName, newPrice).allMatch(Objects::isNull)) {
            return ResponseEntity.badRequest().body("No value for this update is specified.");
        }
        Item item = itemsRepository.findByItemId(itemID);
        if (newName != null) item.setItemName(newName);
        if (newPrice != null) item.setItemPrice(newPrice);

        Item updatedItem = itemsRepository.save(item);
        return ResponseEntity.ok(updatedItem);

    }

}
