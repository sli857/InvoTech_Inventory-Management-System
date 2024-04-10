package com.depot.ims.services;

import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import com.depot.ims.models.Availability;
import com.depot.ims.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing Availability operations
 */
@Service
public class AvailabilityService {

    private final SiteRepository siteRepository;
    private final ItemRepository itemRepository;
    private final AvailabilityRepository availabilityRepository;


    /**
     * Constructor for availability Service.
     *
     * @param siteRepository         The ShipRepository instance.
     * @param itemRepository         The ItemRepository instance.
     * @param availabilityRepository The AvailabilityRepository instance.
     */
    public AvailabilityService(SiteRepository siteRepository,
                               ItemRepository itemRepository,
                               AvailabilityRepository availabilityRepository) {
        this.siteRepository = siteRepository;
        this.itemRepository = itemRepository;
        this.availabilityRepository = availabilityRepository;
    }

    /**
     * Adds a new Availability
     *
     * @param availability The availability object to be added.
     * @return ResponseEntity containing the result of the availability addition operation.
     */
    public ResponseEntity<?> addAvailabilities(@RequestBody Availability availability) {
        try {
            this.availabilityRepository.save(availability);
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Gets all availabilities of the site with the given siteID
     *
     * @param siteID id of site.
     * @return ResponseEntity containing all availabilities of the site with the given siteID.
     */
    public ResponseEntity<?> getAvailability(
            @RequestParam(value = "siteId",required = false) Long siteID) {
        //make sure the given ID is valid
        if (siteID != null) {
            //make sure the given ID exist in our inventory
            if(!siteRepository.existsById(siteID)){
                return ResponseEntity.badRequest().body("Site not found by siteId");
            }
            try{
                return ResponseEntity.ok(availabilityRepository.findBySiteId(siteID));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e);
            }
        }

        return ResponseEntity.badRequest().body("please provide a valid siteID");
    }


    /**
     * Get sites that contains all the items with the given item id
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

        //case one, there is only item
        if (itemList.size() == 1) {
            List<Site> sites = this.availabilityRepository.findSitesByItems(itemList);
            return ResponseEntity.ok(sites);
        } else if (itemList.size() == 2) {
            List<Site> results = new ArrayList<>();
            List<Site> sitesWithItem1 = this.availabilityRepository.findSitesByOneItem(itemList.get(0));
            List<Site> sitesWithItem2 = this.availabilityRepository.findSitesByOneItem(itemList.get(1));
            for (Site site: sitesWithItem1) {
                if(sitesWithItem2.contains(site)) {
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
     * Get sites that contains the item with the given item id
     *
     * @param itemId id of item
     * @return ResponseEntity containing sites that contains the item with the given item id.
     */
    public  ResponseEntity<?>  getAvailabilitiesByItemId(@RequestParam(value = "itemId") Long itemId) {
        if (itemId != null) {
            if(!itemRepository.existsById(itemId)){
                return ResponseEntity.badRequest().body("item not found by itemId");
            }
            Item item = this.itemRepository.findByItemId(itemId);
            List<Site> sites = this.availabilityRepository.findSitesByOneItem(item);
            return  ResponseEntity.ok(sites);
        } else {
            return ResponseEntity.badRequest().body("expecting JSON array");
        }
    }

    /**
     * Get availability with given site id and item id
     *
     * @param itemId id of item
     * @param siteId id of site
     * @return ResponseEntity containing availability with given site id and item id.
     */
    public ResponseEntity<?> getAvailabilityBySiteIdAndItemId(
            @RequestParam(value = "siteId") Long siteId,
            @RequestParam(value = "itemId") Long itemId) {
        if(!itemRepository.existsById(itemId)){
            return ResponseEntity.badRequest().body("item not found by itemId");
        }
        if(!siteRepository.existsById(siteId)){
            return ResponseEntity.badRequest().body("Site not found by siteId");
        }

        return  ResponseEntity.ok(availabilityRepository.findBySiteIdAndItemId(siteId, itemId));
    }



}
