package com.depot.ims.controllers;

import com.depot.ims.models.Site;
import com.depot.ims.repositories.SiteRepository;
import com.depot.ims.services.AuditService;
import com.depot.ims.services.SiteService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SiteController class provides API endpoints for managing site within the Inventory Management
 * System. This controller supports operations such as creating, updating, and fetching site
 * details.
 */
@RestController
@RequestMapping(value = "/sites", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = {"http://cs506-team-35.cs.wisc.edu", "http://localhost:5173/"})
public class SiteController {

  private final SiteRepository siteRepository;
  private final SiteService siteService;

  public SiteController(
      SiteRepository siteRepository, SiteService siteService, AuditService auditService) {
    this.siteRepository = siteRepository;
    this.siteService = siteService;
  }

  /**
   * Get all sites in table Sites.
   *
   * @return ResponseEntity that contains a list of sites in its body
   */
  @GetMapping
  public ResponseEntity<?> getSites() {
    return ResponseEntity.ok(this.siteRepository.findAll());
  }

  /**
   * Find a site either by siteId or siteName.
   *
   * @param siteId siteId
   * @param siteName siteName
   * @return the result of SiteService.getSite()
   */
  @GetMapping("/site")
  public ResponseEntity<?> getSite(
      @RequestParam(value = "siteId", required = false) Long siteId,
      @RequestParam(value = "siteName", required = false) String siteName) {
    return siteService.getSite(siteId, siteName);
  }

  /**
   * Get the status of a site by siteId.
   *
   * @param siteId siteId
   * @return the result of SiteService.getStatusBySiteId()
   */
  @GetMapping("/status")
  public ResponseEntity<?> getStatusBySiteId(@RequestParam(value = "siteId") Long siteId) {
    return siteService.getStatusBySiteId(siteId);
  }

  /**
   * Add a site to table Sites.
   *
   * @param site a site entity
   * @return result of siteService.addSite()
   */
  @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addSite(@RequestBody Site site) {
    return siteService.addSite(site);
  }

  /**
   * Update the fields a particular site that has the provided siteId.
   *
   * @param siteId primary key to find the site to modify
   * @param newStatus newStatus
   * @param newName newName
   * @param newLocation newLocation
   * @param newInternalSite newInternalSite
   * @param newCeaseDate newCeaseDate
   * @return result of SiteService.updateSite()
   */
  @Modifying
  @PostMapping("/update")
  public ResponseEntity<?> updateSite(
      @RequestParam(value = "siteId") Long siteId,
      @RequestParam(value = "siteStatus", required = false) String newStatus,
      @RequestParam(value = "siteName", required = false) String newName,
      @RequestParam(value = "siteLocation", required = false) String newLocation,
      @RequestParam(value = "internalSite", required = false) Boolean newInternalSite,
      @RequestParam(value = "ceaseDate", required = false) @DateTimeFormat(style = "YYYY-MM-DD")
          String newCeaseDate) {

    return siteService.updateSite(
        siteId, newStatus, newName, newLocation, newCeaseDate, newInternalSite);
  }

  /**
   * Delete a site that has the provided siteId.
   *
   * @param siteId primary key to find the site to delete
   * @param ceaseDate date when the site was ceased if provided; set to current date if not provided
   * @return result of SiteService.deleteSite()
   */
  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteSite(
      @RequestParam("siteId") Long siteId,
      @RequestParam(value = "ceaseDate", required = false) String ceaseDate) {
    return siteService.deleteSite(siteId, ceaseDate);
  }
}
