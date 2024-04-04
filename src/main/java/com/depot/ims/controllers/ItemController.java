package com.depot.ims.controllers;

import com.depot.ims.models.Item;
import com.depot.ims.repositories.ItemRepository;
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
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ResponseEntity<?> getItems() {return ResponseEntity.ok(this.itemRepository.findAll());}

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<?> addItem(@RequestBody Item item) {
        try{
            this.itemRepository.save(item);
            return ResponseEntity.ok(item);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @GetMapping("/item")
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

    @Modifying
    @PostMapping("/update")
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
