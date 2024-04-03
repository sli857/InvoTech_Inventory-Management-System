package com.depot.ims.controllers;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import com.depot.ims.repositories.AvailabilitiesRepository;
import com.depot.ims.repositories.ItemsRepository;
import com.depot.ims.repositories.SitesRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.repository.query.Param;
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
public class AvailabilitiesController {
    private final AvailabilitiesRepository availabilitiesRepository;
    private final SitesRepository sitesRepository;
    private final ItemsRepository itemsRepository;

    public AvailabilitiesController(AvailabilitiesRepository availabilitiesRepository, SitesRepository sitesRepository, ItemsRepository itemsRepository) {
        this.availabilitiesRepository = availabilitiesRepository;
        this.sitesRepository = sitesRepository;
        this.itemsRepository = itemsRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllAvailabilities() {
        return ResponseEntity.ok(this.availabilitiesRepository.findAll());
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAvailabilities(@RequestBody Availability availability) {
        try {
            this.availabilitiesRepository.save(availability);
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //given the siteID, I want to see a list of availabilities
    @GetMapping("/site")
    public ResponseEntity<?> getAvailability(
            @RequestParam(value = "siteId",required = false) Long siteID) {
        //make sure the given ID is valid
        if (siteID != null) {
            //make sure the given ID exist in our inventory
            if(!sitesRepository.existsById(siteID)){
                return ResponseEntity.badRequest().body("Site not found by siteId");
            }
            //get the site object
            Site site = sitesRepository.findBySiteId(siteID);
            try{
                List<Availability> availabilityList = this.availabilitiesRepository.findBySiteId(site);
                return ResponseEntity.ok(availabilityList);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("exceptions");
            }
        }

        return ResponseEntity.badRequest().body("please provide a valid siteID");
    }

    //As a corporate manager, I want to see all sites that contain the given items.
    //provides me with a list of items id
    @GetMapping(value = "/searchByItems")
    public ResponseEntity<?> getSitesByItems(@RequestParam MultiValueMap<String, String> items) {
        System.out.println(items);

        List<Item> itemList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : items.entrySet()) {
            List<String> values = entry.getValue();
            for (String value : values) {
                Long id = Long.parseLong(value);
                Item item = this.itemsRepository.findByItemId(id);
                itemList.add(item);
            }
        }

        //case one, there is only item
        if (itemList.size() == 1) {
            List<Site> sites = this.availabilitiesRepository.findSitesByItems(itemList);
            return ResponseEntity.ok(sites);
        } else if (itemList.size() == 2) {
            List<Site> results = new ArrayList<>();
            List<Site> sitesWithItem1 = this.availabilitiesRepository.findSitesByOneItem(itemList.get(0));
            List<Site> sitesWithItem2 = this.availabilitiesRepository.findSitesByOneItem(itemList.get(1));
            for (Site site: sitesWithItem1) {
                if(sitesWithItem2.contains(site)) {
                    results.add(site);
                }
            }
            return ResponseEntity.ok(results);
        } else if (itemList.size() > 2) {

            List<Site> results = this.availabilitiesRepository.findSitesByOneItem(itemList.get(0));

            // Iterate through the remaining lists and retain only the common elements
            for (int i = 1; i < itemList.size(); i++) {
                results.retainAll(this.availabilitiesRepository.findSitesByOneItem(itemList.get(i)));
            }

            return ResponseEntity.ok(results);

        }

        return ResponseEntity.badRequest().body("expecting JSON array");
    }


    @GetMapping("/item")
    public  ResponseEntity<?>  getAvailabilitiesByItemId(@RequestParam(value = "itemId") Long itemId) {
        if (itemId != null) {
            if(!itemsRepository.existsById(itemId)){
                return ResponseEntity.badRequest().body("item not found by itemId");
            }
            Item item = this.itemsRepository.findByItemId(itemId);
            List<Site> sites = this.availabilitiesRepository.findSitesByOneItem(item);
            return  ResponseEntity.ok(sites);
        } else {
            return ResponseEntity.badRequest().body("expecting JSON array");
        }
    }

    @GetMapping("/site/item")
    public ResponseEntity<?> getAvailabilityBySiteIdAndItemId(
            @RequestParam(value = "siteId") Long siteId,
            @RequestParam(value = "itemId") Long itemId) {
        if(!itemsRepository.existsById(itemId)){
            return ResponseEntity.badRequest().body("item not found by itemId");
        }
        if(!sitesRepository.existsById(siteId)){
            return ResponseEntity.badRequest().body("Site not found by siteId");
        }
        Item item = this.itemsRepository.findByItemId(itemId);
        Site site = this.sitesRepository.findBySiteId(siteId);
        return  ResponseEntity.ok(availabilitiesRepository.findBySiteIdAndItemId(site, item));
    }


}
