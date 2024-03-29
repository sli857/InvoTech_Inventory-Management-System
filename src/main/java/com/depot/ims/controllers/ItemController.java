package com.depot.ims.controllers;

import com.depot.ims.models.Item;
import com.depot.ims.repositories.ItemRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public List<Item> getItems() {
        return this.itemRepository.findAll();
    }

    @GetMapping("/itemName={itemName}")
    public List<Item> getItemsByName(@PathVariable String itemName) {
        return this.itemRepository.findByItemName(itemName);
    }

    @GetMapping("/itemId={itemID}")
    public Item getItemById(@PathVariable Integer itemID) {
        return this.itemRepository.findByItemId(itemID);
    }

    @PostMapping(value = "/addItem", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Item addItem(@RequestBody Item item) {
        return this.itemRepository.save(item);
    }
}
