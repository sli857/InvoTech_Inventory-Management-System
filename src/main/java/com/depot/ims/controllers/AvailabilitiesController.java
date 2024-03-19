package com.depot.ims.controllers;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import com.depot.ims.repositories.AvailabilitiesRepository;
import com.depot.ims.repositories.SitesRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
public class AvailabilitiesController {
    private final AvailabilitiesRepository availabilitiesRepository;

    public AvailabilitiesController(AvailabilitiesRepository availabilitiesRepository) {
        this.availabilitiesRepository = availabilitiesRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllAvailabilities() {
        return ResponseEntity.ok(this.availabilitiesRepository.findAll());
    }

    //get by siteID or itemID or siteID and itemID
    //TODO: fix below method
    @GetMapping("/availability")
    public ResponseEntity<?> getAvailability(
            @RequestParam(value = "siteId", required = false) Long siteID,
            @RequestParam(value = "itemId", required = false) String itemID) {

        return null;
    }

    @GetMapping("/site={siteId}")
    public List<Availability> getAvailabilitiesBySiteId(@RequestBody Site siteId) {
        return availabilitiesRepository.findBySiteId(siteId);
    }

    @GetMapping("/item={itemId}")
    public List<Availability> getAvailabilitiesByItemId(@RequestBody Item itemId) {
        return availabilitiesRepository.findByItemId(itemId);
    }

    @GetMapping("/site={siteId}/item={itemId}")
    public Availability getAvailabilityBySiteIdAndItemId(@RequestBody Site siteId, @RequestBody Item itemId) {
        return availabilitiesRepository.findBySiteIdAndItemId(siteId, itemId);
    }




}
