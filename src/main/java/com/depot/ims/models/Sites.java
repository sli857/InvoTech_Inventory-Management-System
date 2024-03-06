package com.depot.ims.models;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sites {

    @Id
    @GeneratedValue
    @Column(name = "PK_sites", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "site_name", unique = true, nullable = false)
    private String siteName;

    @Column(name = "site_location", nullable = false)
    private String siteLocation;

    @Column(name = "site_status", nullable = false)
    private String siteStatus;

    @Column(name = "cease_date")
    private Timestamp ceaseDate;

    @Column(name = "internal_site")
    private Boolean internalSite;

}
