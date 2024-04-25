package com.depot.ims.services;

import com.depot.ims.models.Item;
import com.depot.ims.repositories.ItemRepository;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/** item class for managing Ship operations */
@Service
public class ItemService {

  private final ItemRepository itemRepository;
  private final AuditService auditService;

  /**
   * Constructor for itemService.
   *
   * @param itemRepository The ItemRepository instance.
   */
  public ItemService(ItemRepository itemRepository, AuditService auditService) {
    this.itemRepository = itemRepository;
    this.auditService = auditService;
  }

  /**
   * Adds a new item.
   *
   * @param item The Item object to be added.
   * @return ResponseEntity containing the result of the item addition operation.
   */
  public ResponseEntity<?> addItem(@RequestBody Item item) {
    try {
      var res = this.itemRepository.save(item);
      this.auditService.saveAudit(
          "Items", null, res.getItemId().toString(), null, res.toString(), "INSERT");
      return ResponseEntity.ok(item);

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * Gets an item with the given id or name
   *
   * @return ResponseEntity containing the result of the item with the given id or name. @Param item
   *     name or item id
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
   * @return ResponseEntity containing the result of the item with the given id or name. @Param item
   *     name or item id
   */
  public ResponseEntity<?> updateItem(
      @RequestParam(value = "itemId") Long itemID,
      @RequestParam(value = "itemName", required = false) String newName,
      @RequestParam(value = "itemPrice", required = false) Double newPrice) {

    if (!itemRepository.existsById(itemID)) {
      return ResponseEntity.badRequest().body("item not found by itemId");
    }
    if (Stream.of(newName, newPrice).allMatch(Objects::isNull)) {
      return ResponseEntity.badRequest().body("No value for this update is specified.");
    }
    Item item = itemRepository.findByItemId(itemID);
    if (newName != null) {
      auditService.saveAudit(
          "Items", "itemName", item.getItemId().toString(), item.getItemName(), newName, "UPDATE");
      item.setItemName(newName);
    }
    if (newPrice != null) {
      auditService.saveAudit(
          "Items",
          "itemPrice",
          item.getItemId().toString(),
          item.getItemPrice().toString(),
          newPrice.toString(),
          "UPDATE");
      item.setItemPrice(newPrice);
    }

    Item updatedItem = itemRepository.save(item);
    return ResponseEntity.ok(updatedItem);
  }
}
