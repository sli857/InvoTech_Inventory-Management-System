package com.depot.ims.repositories;

import com.depot.ims.models.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SiteRepository extends JpaRepository<Site, Long> {

    @Query("select s from Site s where s.siteName = ?1")
    Site findBySiteName(String siteName);

    @Query("SELECT s FROM Site s WHERE s.siteId=?1")
    Site findBySiteId(Long siteId);

    @Query("SELECT s.siteStatus FROM Site s WHERE s.siteId=?1")
    String findSiteStatusBySiteId(Long siteId);
}
