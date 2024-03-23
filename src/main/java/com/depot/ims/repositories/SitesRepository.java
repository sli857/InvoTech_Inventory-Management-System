package com.depot.ims.repositories;

import com.depot.ims.models.tables.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SitesRepository extends JpaRepository<Site, Long> {

    @Query("select s from Site s where s.siteName = ?1")
    Site findBySiteName(String siteName);

    List<Site> findAll();

    @Query("SELECT s FROM Site s WHERE s.siteId=?1")
    Site findBySiteId(long siteId);

}
