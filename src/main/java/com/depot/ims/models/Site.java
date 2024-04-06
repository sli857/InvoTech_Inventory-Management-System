package com.depot.ims.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

}
