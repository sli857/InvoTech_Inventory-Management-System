package com.depot.ims.services;

import com.depot.ims.models.Site;
import com.depot.ims.repositories.SiteRepository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/** This class provides methods for business level logic operations for managing sites. */
@Service
public class SiteService {

  private final SiteRepository siteRepository;
  private final AuditService auditService;

  public SiteService(SiteRepository siteRepository, AuditService auditService) {
    this.siteRepository = siteRepository;
    this.auditService = auditService;
  }

  /**
   * Try to find a single site by siteId or siteName. If siteId is provided, siteName is ignored; if
   * siteId is not provided then look up this site by siteName; if both are not provided, badRequest
   * will be returned.
   *
   * @param siteId siteId
   * @param siteName siteName
   * @return ResponseEntity that contains the site in its body, or BadRequest if site cannot be
   *     found.
   */
  public ResponseEntity<?> getSite(Long siteId, String siteName) {
    if (siteId != null) {
      var res = siteRepository.findBySiteId(siteId);
      return res == null
          ? ResponseEntity.ok("Site not found by siteId: " + siteId)
          : ResponseEntity.ok(res);
    } else if (siteName != null) {
      var res = siteRepository.findBySiteName(siteName);
      return res == null
          ? ResponseEntity.ok("Site not found by siteName: " + siteName)
          : ResponseEntity.ok(res);
    } else {
      return ResponseEntity.badRequest().body("Either siteId or siteName must be provided");
    }
  }

  /**
   * Get the status of a site.
   *
   * @param siteId primary key to find the site
   * @return ResponseEntity that contains the status of the site in its body, or BadRequest if site
   *     cannot be found.
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
   * Add a site to table Sites.
   *
   * @param site site entity
   * @return ResponseEntity that contains the successfully added site in its body, or BadRequest if
   *     site cannot be added to database
   */
  public ResponseEntity<?> addSite(Site site) {
    try {
      var res = siteRepository.save(site);
      System.out.println(site);
      auditService.saveAudit(
          "sites", null, res.getSiteId().toString(), null, res.toString(), "INSERT");
      return ResponseEntity.ok(res);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * Update any of the fields of a site entity found by siteId in table Site.
   *
   * @param siteId siteId
   * @param newStatus newStatus
   * @param newName newName
   * @param newLocation newLocation
   * @param newCeaseDate newCeaseDate
   * @param newInternalSite newInternalSite
   * @return ResponseEntity that contains the modified site entity in its body; return corresponding
   *     error code and message necessarily
   */
  public ResponseEntity<?> updateSite(
      Long siteId,
      String newStatus,
      String newName,
      String newLocation,
      String newCeaseDate,
      Boolean newInternalSite) {
    if (siteId == null) {
      return ResponseEntity.badRequest().body("siteId cannot be null");
    }
    if (!siteRepository.existsById(siteId)) {
      return ResponseEntity.badRequest().body("Site not found by siteId");
    }
    if (Stream.of(newStatus, newName, newLocation, newInternalSite, newCeaseDate)
        .allMatch(Objects::isNull)) {
      return ResponseEntity.badRequest().body("No value for this update is specified.");
    }

    Site site = siteRepository.findBySiteId(siteId);
    if (newStatus != null) {
      auditService.saveAudit(
          "sites", "siteStatus", siteId.toString(), site.getSiteStatus(), newStatus, "UPDATE");
      site.setSiteStatus(newStatus);
    }
    if (newName != null) {
      auditService.saveAudit(
          "sites", "siteName", siteId.toString(), site.getSiteName(), newName, "UPDATE");
      site.setSiteName(newName);
    }
    if (newLocation != null) {
      auditService.saveAudit(
          "sites",
          "siteLocation",
          siteId.toString(),
          site.getSiteLocation(),
          newLocation,
          "UPDATE");
      site.setSiteLocation(newLocation);
    }
    if (newInternalSite != null) {
      auditService.saveAudit(
          "sites",
          "internalSite",
          siteId.toString(),
          site.getInternalSite().toString(),
          newInternalSite.toString(),
          "UPDATE");
      site.setInternalSite(newInternalSite);
    }
    if (newCeaseDate != null) {
      try {
        Date date = Date.valueOf(newCeaseDate);
        auditService.saveAudit(
            "sites",
            "ceaseDate",
            siteId.toString(),
            site.getCeaseDate().toString(),
            newCeaseDate,
            "UPDATE");
        site.setCeaseDate(date);
      } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Date format is illegal.");
      }
    }
    Site updatedSite = siteRepository.save(site);
    return ResponseEntity.ok(updatedSite);
  }

  /**
   * Invalidate a site, set status to "closed" and set ceaseDate accordingly if ceaseDate is
   * provided, otherwise set ceaseDate to current date.
   *
   * @param siteId siteId
   * @param ceaseDate ceaseDate
   * @return ResponseEntity that contains the deleted site entity in its body; return corresponding
   *     error code and message necessarily
   */
  public ResponseEntity<?> deleteSite(Long siteId, String ceaseDate) {
    if (siteId == null) {
      return ResponseEntity.badRequest().body("siteId cannot be null");
    }
    if (!siteRepository.existsById(siteId)) {
      return ResponseEntity.badRequest().body("Site not " + "found by siteId");
    }
    Site site = siteRepository.findBySiteId(siteId);
    Date date = ceaseDate == null ? Date.valueOf(LocalDate.now()) : Date.valueOf(ceaseDate);
    auditService.saveAudit(
        "Sites", "internalSite", site.getSiteId().toString(), "open", "closed", "UPDATE");
    auditService.saveAudit(
        "Sites",
        "ceaseDate",
        site.getSiteId().toString(),
        site.getCeaseDate() == null ? null : site.getCeaseDate().toString(),
        date.toString(),
        "UPDATE");
    site.setCeaseDate(date);
    site.setSiteStatus("closed");

    return ResponseEntity.ok(siteRepository.saveAndFlush(site));
  }
}
