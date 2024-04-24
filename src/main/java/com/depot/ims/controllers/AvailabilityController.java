// Package declaration
package com.depot.ims.controllers;

// Import statements

import com.depot.ims.models.Availability;
import com.depot.ims.repositories.AvailabilityRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.SiteRepository;
import com.depot.ims.services.AvailabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AvailabilityController class provides API endpoints for managing availabilities within
 * the Inventory Management System.
 * This controller supports operations such as creating, updating,
 * and fetching availabilities details, Or using availabilities table to fetch
 * sites or items details
 */
@RestController
@RequestMapping(value = "/availabilities", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173/")
public class AvailabilityController {

    // Fields for the availability repository, siteRepository, itemRepository and availability service
    private final AvailabilityRepository availabilityRepository;
    private final SiteRepository siteRepository;
    private final ItemRepository itemRepository;
    private final AvailabilityService availabilityService;

    /**
     * Constructor for Availability Controller.
     *
     * @param availabilityService    service for shipment related operations
     * @param availabilityRepository Repository for item data access.
     * @param siteRepository         Repository for site data access.
     * @param itemRepository         Repository for item data access.
     */
    public AvailabilityController(AvailabilityService availabilityService, AvailabilityRepository
            availabilityRepository, SiteRepository siteRepository, ItemRepository itemRepository) {
        this.availabilityRepository = availabilityRepository;
        this.siteRepository = siteRepository;
        this.itemRepository = itemRepository;
        this.availabilityService = availabilityService;
    }

    /**
     * Endpoint to retrieve all availabilities
     *
     * @return ResponseEntity with the list of all availabilities.
     */
    @GetMapping
    public ResponseEntity<?> getAllAvailabilities() {
        return ResponseEntity.ok(this.availabilityRepository.findAll());
    }

    /**
     * Endpoint to add a new shipment.
     * Accepts availability details in the form of a JSON object.
     *
     * @param availability Availability object containing details of the new availability.
     * @return The saved availability entity.
     */
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAvailabilities(@RequestBody Availability availability) {
        return this.availabilityService.addAvailabilities(availability);
    }

    /**
     * Change the quantity of the item of the site
     * @param info an object that contains siteid, itemid, operation, and quantity information
     * @return ResponseEntity containing the result of the updated availability
     */
    @PostMapping(value="/quantity", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeQuantity( @RequestBody Map<String, Object> info) {
        try {
            Long siteId = Long.parseLong(info.get("siteId").toString());
            Long itemId = Long.parseLong(info.get("itemId").toString());
            String operation = info.get("operation").toString();
            Integer quantity = Integer.parseInt(info.get("quantity").toString());
            return availabilityService.changeQuantity(siteId, itemId, operation, quantity);

        } catch (Exception e) {
            return new ResponseEntity<>("Invalid input data", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to fetch a list of availability specific by site id.
     *
     * @param siteID The ID of the site
     * @return ResponseEntity with the details of the list of availabilities.
     */
    @GetMapping("/site")
    public ResponseEntity<?> getAvailability(
            @RequestParam(value = "siteId", required = false) Long siteID) {
        return this.availabilityService.getAvailability((siteID));
    }

    /**
     * Endpoint to fetch a list of sites that contains the items with the given item id.
     *
     * @param items a list of item ids
     * @return ResponseEntity with the details of the list of availabilities.
     */
    @GetMapping(value = "/searchByItems")
    public ResponseEntity<?> getSitesByItems(@RequestParam MultiValueMap<String, String> items) {

        return this.availabilityService.getSitesByItems(items);
    }

    /**
     * Endpoint to fetch a list of availability specific by item id.
     *
     * @param itemId The ID of the item
     * @return ResponseEntity with the details of the list of availabilities.
     */
    @GetMapping("/item")
    public ResponseEntity<?> getAvailabilitiesByItemId(@RequestParam(value = "itemId") Long itemId) {

        return this.availabilityService.getAvailabilitiesByItemId(itemId);

    }

    /**
     * Endpoint to fetch an availability specific by site id and item id
     *
     * @param siteId The ID of the site
     * @param itemId The ID of the site
     * @return ResponseEntity with the details of the specific availability.
     */
    @GetMapping("/site/item")
    public ResponseEntity<?> getAvailabilityBySiteIdAndItemId(
            @RequestParam(value = "siteId") Long siteId,
            @RequestParam(value = "itemId") Long itemId) {

        return this.availabilityService.getAvailabilityBySiteIdAndItemId(siteId, itemId);

    }


}
