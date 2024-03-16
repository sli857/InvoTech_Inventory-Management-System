package com.depot.ims.controllers;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.depot.ims.repositories.SitesRepository;

import java.sql.Date;
import java.util.Objects;
import java.util.stream.Stream;

import com.depot.ims.models.Site;

@RestController
@RequestMapping(value = "/sites", produces = MediaType.APPLICATION_JSON_VALUE)
public class SitesController {

    private final SitesRepository sitesRepository;

    public SitesController(SitesRepository sitesRepository) {
        this.sitesRepository = sitesRepository;
    }

    @GetMapping
    public ResponseEntity<?> getSites() {
        return ResponseEntity.ok(this.sitesRepository.findAll());
    }

    @GetMapping("/site")
    public ResponseEntity<?> getSite(
            @RequestParam(value = "siteId", required = false) Long siteID,
            @RequestParam(value = "siteName", required = false) String siteName) {
        if (siteID != null) {
            return ResponseEntity.ok(sitesRepository.findBySiteId(siteID));
        } else if (siteName != null) {
            return ResponseEntity.ok(sitesRepository.findBySiteName(siteName));
        } else {
            return ResponseEntity.badRequest().body("Either siteId or siteName must be provided");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatusBySiteId(
            @RequestParam(value = "siteId") Long siteID) {
        Site site = sitesRepository.findBySiteId(siteID);
        if (site == null) {
            return ResponseEntity.badRequest().body("Site not found by siteId");
        } else {
            return ResponseEntity.ok(site.getSiteStatus());
        }
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Site addSite(@RequestBody Site site) {
        return sitesRepository.save(site);
    }

    @Modifying
    @PostMapping("/update")
    public ResponseEntity<?> updateSite(
            @RequestParam(value = "siteId")
            Long siteID,
            @RequestParam(value = "siteStatus", required = false)
            String newStatus,
            @RequestParam(value = "siteName", required = false)
            String newName,
            @RequestParam(value = "siteLocation", required = false)
            String newSiteLocation,
            @RequestParam(value = "internalSite", required = false)
            Boolean newInternalSite,
            @RequestParam(value = "ceaseDate", required = false)
            @DateTimeFormat(style = "YYYY-MM-DD")
            String newCeaseDate) {

        if(!sitesRepository.existsById(siteID)){
            return ResponseEntity.badRequest().body("Site not found by siteId");
        }
        if (Stream.of(newStatus,newName,newSiteLocation,newInternalSite,newCeaseDate).allMatch(Objects::isNull)) {
            return ResponseEntity.badRequest().body("No value for this update is specified.");
        }

        Site site = sitesRepository.findBySiteId(siteID);
        if(newStatus != null) site.setSiteStatus(newStatus);
        if(newName != null) site.setSiteName(newName);
        if(newSiteLocation != null) site.setSiteLocation(newSiteLocation);
        if(newInternalSite != null) site.setInternalSite(newInternalSite);
        if (newCeaseDate != null) {
            try {
                Date date = Date.valueOf(newCeaseDate);
                site.setCeaseDate(date);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Date format is illegal.");
            }
        }

        Site updatedSite = sitesRepository.save(site);
        return ResponseEntity.ok(updatedSite);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSite(@RequestParam("siteId") Long siteID) {
        try {
            boolean isFound = sitesRepository.existsById(siteID);
            if (isFound) {
                sitesRepository.deleteById(siteID);
                return ResponseEntity.ok().body("Successfully deleted");
            }
            return ResponseEntity.badRequest().body("Site not found by siteId");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("siteId cannot be null");
        }
    }
}
