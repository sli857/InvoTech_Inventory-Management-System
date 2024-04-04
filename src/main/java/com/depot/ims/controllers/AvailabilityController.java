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

//        try {
//            this.availabilityRepository.save(availability);
//            return ResponseEntity.ok(availability);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
    }

    //given the siteID, I want to see a list of availabilities
    @GetMapping("/site")
    public ResponseEntity<?> getAvailability(
            @RequestParam(value = "siteId",required = false) Long siteID) {
        //make sure the given ID is valid

        return this.availabilityService.getAvailability((siteID));
//        if (siteID != null) {
//            //make sure the given ID exist in our inventory
//            if(!siteRepository.existsById(siteID)){
//                return ResponseEntity.badRequest().body("Site not found by siteId");
//            }
//            try{
//                return ResponseEntity.ok(availabilityRepository.findBySiteId(siteID));
//            } catch (Exception e) {
//                return ResponseEntity.badRequest().body(e);
//            }
//        }
//
//        return ResponseEntity.badRequest().body("please provide a valid siteID");
    }

    //As a corporate manager, I want to see all sites that contain the given items.
    //provides me with a list of items id
    @GetMapping(value = "/searchByItems")
    public ResponseEntity<?> getSitesByItems(@RequestParam MultiValueMap<String, String> items) {

        return this.availabilityService.getSitesByItems(items);
//        System.out.println(items);

//        List<Item> itemList = new ArrayList<>();
//        for (Map.Entry<String, List<String>> entry : items.entrySet()) {
//            List<String> values = entry.getValue();
//            for (String value : values) {
//                Long id = Long.parseLong(value);
//                Item item = this.itemRepository.findByItemId(id);
//                itemList.add(item);
//            }
//        }

        //case one, there is only item
//        if (itemList.size() == 1) {
//            List<Site> sites = this.availabilityRepository.findSitesByItems(itemList);
//            return ResponseEntity.ok(sites);
//        } else if (itemList.size() == 2) {
//            List<Site> results = new ArrayList<>();
//            List<Site> sitesWithItem1 = this.availabilityRepository.findSitesByOneItem(itemList.get(0));
//            List<Site> sitesWithItem2 = this.availabilityRepository.findSitesByOneItem(itemList.get(1));
//            for (Site site: sitesWithItem1) {
//                if(sitesWithItem2.contains(site)) {
//                    results.add(site);
//                }
//            }
//            return ResponseEntity.ok(results);
//        } else if (itemList.size() > 2) {

//            List<Site> results = this.availabilityRepository.findSitesByOneItem(itemList.get(0));
//
//            // Iterate through the remaining lists and retain only the common elements
//            for (int i = 1; i < itemList.size(); i++) {
//                results.retainAll(this.availabilityRepository.findSitesByOneItem(itemList.get(i)));
//            }
//
//            return ResponseEntity.ok(results);
    //}

        //return ResponseEntity.badRequest().body("expecting JSON array");
    }


    @GetMapping("/item")
    public  ResponseEntity<?>  getAvailabilitiesByItemId(@RequestParam(value = "itemId") Long itemId) {

        return this.availabilityService.getAvailabilitiesByItemId(itemId);
//        if (itemId != null) {
//            if(!itemRepository.existsById(itemId)){
//                return ResponseEntity.badRequest().body("item not found by itemId");
//            }
//            Item item = this.itemRepository.findByItemId(itemId);
//            List<Site> sites = this.availabilityRepository.findSitesByOneItem(item);
//            return  ResponseEntity.ok(sites);
//        } else {
//            return ResponseEntity.badRequest().body("expecting JSON array");
//        }
    }

    @GetMapping("/site/item")
    public ResponseEntity<?> getAvailabilityBySiteIdAndItemId(
            @RequestParam(value = "siteId") Long siteId,
            @RequestParam(value = "itemId") Long itemId) {

        return this.availabilityService.getAvailabilityBySiteIdAndItemId(siteId, itemId);

//        if(!itemRepository.existsById(itemId)){
//            return ResponseEntity.badRequest().body("item not found by itemId");
//        }
//        if(!siteRepository.existsById(siteId)){
//            return ResponseEntity.badRequest().body("Site not found by siteId");
//        }
//
//        return  ResponseEntity.ok(availabilityRepository.findBySiteIdAndItemId(siteId, itemId));
    }


}
