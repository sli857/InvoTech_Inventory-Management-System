package com.depot.ims.services;

import com.depot.ims.models.Site;
import com.depot.ims.repositories.AvailabilityRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.SiteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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

    /**
     * Try to find a single site based on siteId first. If siteId is not provided, find a single
     * site by siteName.
     * @param siteId siteId
     * @param siteName siteName
     * @return ResponseEntity that contains the site in its body, or BadRequest if site cannot
     * be found.
     */
    public ResponseEntity<?> getSite(Long siteId, String siteName) {
        if (siteId != null) {
            return ResponseEntity.ok(siteRepository.findBySiteId(siteId));
        } else if (siteName != null) {
            return ResponseEntity.ok(siteRepository.findBySiteName(siteName));
        } else {
            return ResponseEntity.badRequest().body("Either siteId or siteName must be provided");
        }
    }

    /**
     * get the status of a site
     * @param siteId primary key to find the site
     * @return ResponseEntity that contains the status of the site in its body, or BadRequest if
     * site cannot be found.
     */
    public ResponseEntity<?> getStatusBySiteId(Long siteId) {
        String status = siteRepository.findSiteStatusBySiteId(siteId);
        if (status == null) {
            return ResponseEntity.badRequest().body("Site not found by siteId");
        } else {
            return ResponseEntity.ok(status);
        }
    }

    /**
     * add a site to table Sites
     * @param site site entity
     * @return ResponseEntity that contains the successfully added site in its body, or BadRequest
     * if site cannot be added to database
     */
    public ResponseEntity<?> addSite(Site site) {
        try {
            return ResponseEntity.ok(siteRepository.save(site));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * update any of the fields of a site entity found by siteId in table Site
     * @param siteId siteId
     * @param newStatus newStatus
     * @param newName newName
     * @param newLocation newLocation
     * @param newCeaseDate newCeaseDate
     * @param newInternalSite newInternalSite
     * @return ResponseEntity that contains the modified site entity in its body; return
     * corresponding error code and message necessarily
     */
    public ResponseEntity<?> updateSite(Long siteId, String newStatus, String newName,
                                        String newLocation, String newCeaseDate,
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


    /**
     * invalidate a site, set status to "closed" and set ceaseDate accordingly if ceaseDate is
     * provided, otherwise set ceaseDate to current moment.
     * @param siteId siteId
     * @param ceaseDate ceaseDate
     * @return ResponseEntity that contains the deleted site entity in its body; return
     * corresponding error code and message necessarily
     */
    public ResponseEntity<?> deleteSite(Long siteId, String ceaseDate) {
        if (siteId == null) return ResponseEntity.badRequest().body("siteId cannot be null");
        if (!siteRepository.existsById(siteId))
            return ResponseEntity.badRequest().body("Site not " +
                    "found by siteId");
        Site site = siteRepository.findBySiteId(siteId);
        Date date = ceaseDate == null ? Date.valueOf(LocalDate.now()) : Date.valueOf(ceaseDate);
        site.setCeaseDate(date);
        site.setSiteStatus("closed");
        return ResponseEntity.ok(siteRepository.saveAndFlush(site));
    }
}
