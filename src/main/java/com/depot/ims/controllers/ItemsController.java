package com.depot.ims.controllers;
import com.depot.ims.models.Item;
import com.depot.ims.repositories.ItemsRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemsController {
    private final ItemsRepository itemsRepository;

    public ItemsController(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @GetMapping
    public List<Item> getItems() {
        return this.itemsRepository.findAll();
    }

    @GetMapping("/itemName={itemName}")
    public List<Item> getItemsByName(@PathVariable String itemName) {
        return this.itemsRepository.findByItemName(itemName);
    }
    @GetMapping("/itemId={itemID}")
    public Item getItemById(@PathVariable Integer itemID){
        return this.itemsRepository.findByItemId(itemID);
    }

    @PostMapping(value = "/addItem", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Item addItem(@RequestBody Item item){
        return this.itemsRepository.save(item);
    }
}
