package com.depot.ims.controllers;

import com.depot.ims.models.Availability;
import com.depot.ims.repositories.AvailabilityRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
public class AvailabilitiesController {
    private final AvailabilityRepository availabilityRepository;

    public AvailabilitiesController(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    @GetMapping
    public List<Availability> getAllAvailabilities() {
        return availabilityRepository.findAll();
    }

    @GetMapping("/site={siteId}")
    public List<Availability> getAvailabilitiesBySiteId(@PathVariable Long siteId) {
        return availabilityRepository.findBySiteId(siteId);
    }

    @GetMapping("/item={itemId}")
    public List<Availability> getAvailabilitiesByItemId(@PathVariable Long itemId) {
        return availabilityRepository.findByItemId(itemId);
    }

    @GetMapping("/site={siteId}/item={itemId}")
    public Availability getAvailabilityBySiteIdAndItemId(@PathVariable Long siteId, @PathVariable Long itemId) {
        return availabilityRepository.findBySiteIdAndItemId(siteId, itemId);
    }

}
