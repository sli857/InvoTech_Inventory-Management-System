package com.depot.ims.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Represents a site entity in the system Encapsulates user details and credentials for access
 * control and identification. Annotations from JPA are used for ORM (Object Relational Mapping) to
 * a database table, while Lombok annotations reduce boilerplate code for standard Java
 * functionalities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "Sites")
public class Site {

  @Id()
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PK_sites", updatable = false, nullable = false)
  private Long siteId;

  @Column(name = "site_name", unique = true, nullable = false)
  private String siteName;

  @Column(name = "site_location", nullable = false)
  private String siteLocation;

  @Column(name = "site_status", nullable = false)
  private String siteStatus;

  @Column(name = "cease_date")
  @DateTimeFormat(style = "YYYY-MM-DD")
  private Date ceaseDate;

  @Column(name = "internal_site")
  private Boolean internalSite;

  /**
   * Constructor with parameters.
   *
   * @param siteName siteName
   * @param siteLocation siteLocation
   * @param siteStatus siteStatus
   * @param ceaseDate ceaseDate
   * @param internalSite internalSite
   */
  public Site(
      String siteName,
      String siteLocation,
      String siteStatus,
      Date ceaseDate,
      boolean internalSite) {
    this.siteName = siteName;
    this.siteLocation = siteLocation;
    this.siteStatus = siteStatus;
    this.ceaseDate = ceaseDate;
    this.internalSite = internalSite;
  }
}
