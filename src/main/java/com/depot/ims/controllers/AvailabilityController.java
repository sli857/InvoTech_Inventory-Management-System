package com.depot.ims.controllers;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import com.depot.ims.repositories.AvailabilityRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.SiteRepository;
import com.depot.ims.services.AvailabilityService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/availabilities", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173/")
public class AvailabilityController {
    private final AvailabilityRepository availabilityRepository;
    private final SiteRepository siteRepository;
    private final ItemRepository itemRepository;

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService, AvailabilityRepository availabilityRepository, SiteRepository siteRepository, ItemRepository itemRepository) {
        this.availabilityRepository = availabilityRepository;
        this.siteRepository = siteRepository;
        this.itemRepository = itemRepository;
        this.availabilityService = availabilityService;
    }

    @GetMapping
    public ResponseEntity<?> getAllAvailabilities() {
        return ResponseEntity.ok(this.availabilityRepository.findAll());
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAvailabilities(@RequestBody Availability availability) {
        return this.availabilityService.addAvailabilities(availability);

    }

    //given the siteID, I want to see a list of availabilities
    @GetMapping("/site")
    public ResponseEntity<?> getAvailability(
            @RequestParam(value = "siteId",required = false) Long siteID) {
        //make sure the given ID is valid

        return this.availabilityService.getAvailability((siteID));
    }

    //As a corporate manager, I want to see all sites that contain the given items.
    //provides me with a list of items id
    @GetMapping(value = "/searchByItems")
    public ResponseEntity<?> getSitesByItems(@RequestParam MultiValueMap<String, String> items) {

        return this.availabilityService.getSitesByItems(items);
    }

    @GetMapping("/item")
    public  ResponseEntity<?>  getAvailabilitiesByItemId(@RequestParam(value = "itemId") Long itemId) {

        return this.availabilityService.getAvailabilitiesByItemId(itemId);

    }

    @GetMapping("/site/item")
    public ResponseEntity<?> getAvailabilityBySiteIdAndItemId(
            @RequestParam(value = "siteId") Long siteId,
            @RequestParam(value = "itemId") Long itemId) {

        return this.availabilityService.getAvailabilityBySiteIdAndItemId(siteId, itemId);

    }


}
