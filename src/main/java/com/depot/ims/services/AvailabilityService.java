package com.depot.ims.services;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import com.depot.ims.repositories.AvailabilityRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.SiteRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/** Service class for managing Availability operations. */
@Service
public class AvailabilityService {

  private final SiteRepository siteRepository;
  private final ItemRepository itemRepository;
  private final AvailabilityRepository availabilityRepository;
  private final AuditService auditService;

  /**
   * Constructor for availability Service.
   *
   * @param siteRepository The ShipRepository instance.
   * @param itemRepository The ItemRepository instance.
   * @param availabilityRepository The AvailabilityRepository instance.
   */
  public AvailabilityService(
      SiteRepository siteRepository,
      ItemRepository itemRepository,
      AvailabilityRepository availabilityRepository,
      AuditService auditService) {
    this.siteRepository = siteRepository;
    this.itemRepository = itemRepository;
    this.availabilityRepository = availabilityRepository;
    this.auditService = auditService;
  }

  /**
   * Adds a new Availability.
   *
   * @param availability The availability object to be added.
   * @return ResponseEntity containing the result of the availability addition operation.
   */
  public ResponseEntity<?> addAvailabilities(@RequestBody Availability availability) {
    try {
      var res = availabilityRepository.save(availability);
      String rowKey =
          "itemId: " + res.getItemId().getItemId() + ", siteId: " + res.getSiteId().getSiteId();
      this.auditService.saveAudit("Availabilities", null, rowKey, null, res.toString(), "INSERT");
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * Change the quantity of the item of the site.
   *
   * @param itemId the id of item
   * @param siteId the id of site
   * @param operation one of the three operation(+,- or direct modification)
   * @param quantity quantity to change
   * @return ResponseEntity containing the result of the updated availability
   */
  public ResponseEntity<?> changeQuantity(
      Long siteId, Long itemId, String operation, Integer quantity) {

    if (!this.siteRepository.existsById(siteId)) {
      return ResponseEntity.badRequest().body("Site not found by siteId");
    }
    if (!this.itemRepository.existsById(itemId)) {
      return ResponseEntity.badRequest().body("Item not found by item Id");
    }

    Availability availability = this.availabilityRepository.findBySiteIdAndItemId(siteId, itemId);
    // modify the quantity depending on the operation
    Integer currentQuantity = availability.getQuantity();
    Integer newQuantity = currentQuantity;

    if (operation.equals("+")) {
      newQuantity = currentQuantity + quantity;
    } else if (operation.equals("-")) {
      if (quantity >= currentQuantity) {
        newQuantity = 0;
      } else {
        newQuantity = currentQuantity - quantity;
      }
    } else {
      newQuantity = quantity;
    }
    availability.setQuantity(newQuantity);
    Availability updatedAvailability = this.availabilityRepository.save(availability);

    // construct rowKey as: "itemId: *** ,siteId: ***"
    String rowKey =
        "itemId: "
            + updatedAvailability.getItemId().getItemId()
            + ", siteId: "
            + updatedAvailability.getSiteId().getSiteId();

    // record audit
    auditService.saveAudit(
        "Availabilities",
        "quantity",
        rowKey,
        currentQuantity.toString(),
        newQuantity.toString(),
        "UPDATE");

    return ResponseEntity.ok(updatedAvailability);
  }

  /**
   * Gets all availabilities of the site with the given siteId.
   *
   * @param siteId id of site.
   * @return ResponseEntity containing all availabilities of the site with the given siteId.
   */
  public ResponseEntity<?> getAvailability(
      @RequestParam(value = "siteId", required = false) Long siteId) {
    // make sure the given ID is valid
    if (siteId != null) {
      // make sure the given ID exist in our inventory
      if (!siteRepository.existsById(siteId)) {
        return ResponseEntity.badRequest().body("Site not found by siteId");
      }
      try {
        return ResponseEntity.ok(availabilityRepository.findBySiteId(siteId));
      } catch (Exception e) {
        return ResponseEntity.badRequest().body(e);
      }
    }

    return ResponseEntity.badRequest().body("please provide a valid siteId");
  }

  /**
   * Get sites that contain all the items with the given item id.
   *
   * @param items contains multiple item id
   * @return ResponseEntity containing sites that contains all the items with the given item id.
   */
  public ResponseEntity<?> getSitesByItems(@RequestParam MultiValueMap<String, String> items) {
    //        System.out.println(items);

    List<Item> itemList = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : items.entrySet()) {
      List<String> values = entry.getValue();
      for (String value : values) {
        Long id = Long.parseLong(value);
        Item item = this.itemRepository.findByItemId(id);
        itemList.add(item);
      }
    }

    // case one, there is only item
    if (itemList.size() == 1) {
      List<Site> sites = this.availabilityRepository.findSitesByItems(itemList);
      return ResponseEntity.ok(sites);
    } else if (itemList.size() == 2) {
      List<Site> results = new ArrayList<>();
      List<Site> sitesWithItem1 = this.availabilityRepository.findSitesByOneItem(itemList.get(0));
      List<Site> sitesWithItem2 = this.availabilityRepository.findSitesByOneItem(itemList.get(1));
      for (Site site : sitesWithItem1) {
        if (sitesWithItem2.contains(site)) {
          results.add(site);
        }
      }
      return ResponseEntity.ok(results);
    } else if (itemList.size() > 2) {

      List<Site> results = this.availabilityRepository.findSitesByOneItem(itemList.get(0));

      // Iterate through the remaining lists and retain only the common elements
      for (int i = 1; i < itemList.size(); i++) {
        results.retainAll(this.availabilityRepository.findSitesByOneItem(itemList.get(i)));
      }

      return ResponseEntity.ok(results);
    }

    List<Site> allSite = this.siteRepository.findAll();
    return ResponseEntity.ok(allSite);
  }

  /**
   * Get sites that contain the item with the given item id.
   *
   * @param itemId id of item
   * @return ResponseEntity containing sites that contains the item with the given item id.
   */
  public ResponseEntity<?> getAvailabilitiesByItemId(@RequestParam(value = "itemId") Long itemId) {
    if (itemId != null) {
      if (!itemRepository.existsById(itemId)) {
        return ResponseEntity.badRequest().body("item not found by itemId");
      }
      Item item = this.itemRepository.findByItemId(itemId);
      List<Site> sites = this.availabilityRepository.findSitesByOneItem(item);
      return ResponseEntity.ok(sites);
    } else {
      return ResponseEntity.badRequest().body("expecting JSON array");
    }
  }

  /**
   * Get availability with given site id and item id.
   *
   * @param itemId id of item
   * @param siteId id of site
   * @return ResponseEntity containing availability with given site id and item id.
   */
  public ResponseEntity<?> getAvailabilityBySiteIdAndItemId(
      @RequestParam(value = "siteId") Long siteId, @RequestParam(value = "itemId") Long itemId) {
    if (!itemRepository.existsById(itemId)) {
      return ResponseEntity.badRequest().body("item not found by itemId");
    }
    if (!siteRepository.existsById(siteId)) {
      return ResponseEntity.badRequest().body("Site not found by siteId");
    }

    return ResponseEntity.ok(availabilityRepository.findBySiteIdAndItemId(siteId, itemId));
  }
}
