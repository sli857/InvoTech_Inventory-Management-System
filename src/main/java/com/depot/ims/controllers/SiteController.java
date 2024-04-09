package com.depot.ims.controllers;

import com.depot.ims.models.Site;
import com.depot.ims.repositories.SiteRepository;
import com.depot.ims.services.SiteService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/sites", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173/")

public class SiteController {

    private final SiteRepository siteRepository;
    private final SiteService siteService;

    public SiteController(SiteRepository siteRepository, SiteService siteService) {
        this.siteRepository = siteRepository;
        this.siteService = siteService;
    }

    @GetMapping

    public ResponseEntity<?> getSites() {
        return ResponseEntity.ok(this.siteRepository.findAll());
    }

    @GetMapping("/site")
    public ResponseEntity<?> getSite(
            @RequestParam(value = "siteId", required = false) Long siteId,
            @RequestParam(value = "siteName", required = false) String siteName) {
        return siteService.getSite(siteId, siteName);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatusBySiteId(
            @RequestParam(value = "siteId") Long siteId) {
        return siteService.getStatusBySiteId(siteId);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addSite(@RequestBody Site site) {
        return siteService.addSite(site);
    }

    @Modifying
    @PostMapping("/update")
    public ResponseEntity<?> updateSite(
            @RequestParam(value = "siteId")
            Long siteId,
            @RequestParam(value = "siteStatus", required = false)
            String newStatus,
            @RequestParam(value = "siteName", required = false)
            String newName,
            @RequestParam(value = "siteLocation", required = false)
            String newLocation,
            @RequestParam(value = "internalSite", required = false)
            Boolean newInternalSite,
            @RequestParam(value = "ceaseDate", required = false)
            @DateTimeFormat(style = "YYYY-MM-DD")
            String newCeaseDate) {

        return siteService.updateSite(siteId, newStatus, newName, newLocation, newCeaseDate,
                newInternalSite);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSite(
            @RequestParam("siteId") Long siteId,
            @RequestParam(value = "ceaseDate", required = false) String ceaseDate) {
        return siteService.deleteSite(siteId, ceaseDate);
    }
}
