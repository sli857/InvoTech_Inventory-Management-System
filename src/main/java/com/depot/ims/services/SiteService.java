package com.depot.ims.services;

import com.depot.ims.models.Site;
import com.depot.ims.repositories.AvailabilityRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.SiteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class SiteService {

    private final SiteRepository siteRepository;

    private final AvailabilityRepository availabilityRepository;
    private final ItemRepository itemRepository;


    public SiteService(SiteRepository siteRepository, AvailabilityRepository availabilityRepository, ItemRepository itemRepository) {
        this.siteRepository = siteRepository;
        this.availabilityRepository = availabilityRepository;
        this.itemRepository = itemRepository;
    }

    public ResponseEntity<?> getSite(Long siteId, String siteName) {
        if (siteId != null) {
            return ResponseEntity.ok(siteRepository.findBySiteId(siteId));
        } else if (siteName != null) {
            return ResponseEntity.ok(siteRepository.findBySiteName(siteName));
        } else {
            return ResponseEntity.badRequest().body("Either siteId or siteName must be provided");
        }
    }

    public ResponseEntity<?> getStatusBySiteId(Long siteId) {
        String status = siteRepository.findSiteStatusBySiteId(siteId);
        if (status == null) {
            return ResponseEntity.badRequest().body("Site not found by siteId");
        } else {
            return ResponseEntity.ok(status);
        }
    }

    public ResponseEntity<?> addSite(Site site) {
        try {
            return ResponseEntity.ok(siteRepository.save(site));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> updateSite(Long siteId, String newStatus, String newName,
                                        String newLocation,String newCeaseDate,
                                        Boolean newInternalSite
                                        ) {
        if (siteId == null) {
            return ResponseEntity.badRequest().body("siteId cannot be null");
        }
        if (!siteRepository.existsById(siteId)) {
            return ResponseEntity.badRequest().body("Site not found by siteId");
        }
        if (Stream.of(newStatus, newName, newLocation, newInternalSite, newCeaseDate).allMatch(Objects::isNull)) {
            return ResponseEntity.badRequest().body("No value for this update is specified.");
        }

        Site site = siteRepository.findBySiteId(siteId);
        if (newStatus != null) site.setSiteStatus(newStatus);
        if (newName != null) site.setSiteName(newName);
        if (newLocation != null) site.setSiteLocation(newLocation);
        if (newInternalSite != null) site.setInternalSite(newInternalSite);
        if (newCeaseDate != null) {
            try {
                Date date = Date.valueOf(newCeaseDate);
                site.setCeaseDate(date);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Date format is illegal.");
            }
        }

        Site updatedSite = siteRepository.save(site);
        return ResponseEntity.ok(updatedSite);
    }


    // TODO: update delete logic, this current fails test.
    public ResponseEntity<?> deleteSite(Long siteId) {
        try {
            boolean isFound = siteRepository.existsById(siteId);
            if (isFound) {
                siteRepository.deleteById(siteId);
                return ResponseEntity.ok().body("Successfully deleted");
            }
            return ResponseEntity.badRequest().body("Site not found by siteId");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("siteId cannot be null");
        }
    }
}
