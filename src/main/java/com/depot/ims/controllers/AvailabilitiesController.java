package com.depot.ims.controllers;

import com.depot.ims.models.tables.Availability;
import com.depot.ims.repositories.AvailabilitiesRepository;
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
    public List<Availability> getAllAvailabilities() {
        return availabilitiesRepository.findAll();
    }

    @GetMapping("/site={siteId}")
    public List<Availability> getAvailabilitiesBySiteId(@PathVariable Integer siteId) {
        return availabilitiesRepository.findBySiteId(siteId);
    }

    @GetMapping("/item={itemId}")
    public List<Availability> getAvailabilitiesByItemId(@PathVariable Integer itemId) {
        return availabilitiesRepository.findByItemId(itemId);
    }

    @GetMapping("/site={siteId}/item={itemId}")
    public Availability getAvailabilityBySiteIdAndItemId(@PathVariable Integer siteId, @PathVariable Integer itemId) {
        return availabilitiesRepository.findBySiteIdAndItemId(siteId, itemId);
    }

}
