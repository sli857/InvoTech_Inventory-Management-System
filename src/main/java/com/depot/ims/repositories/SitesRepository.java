package com.depot.ims.repositories;

import com.depot.ims.models.Site;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

@Repository
public interface SitesRepository extends JpaRepository<Site, Long> {

    @Query("select s from Site s where s.siteName = ?1")
    Site findBySiteName(String siteName);

    List<Site> findAll();

    @Query("SELECT s FROM Site s WHERE s.siteId=?1")
    Site findBySiteId(long siteId);

    @Transactional
    @Modifying
    @Query("UPDATE Site s SET s.ceaseDate = :ceaseDate, s.siteStatus = 'close' WHERE s.siteId = :id")
        //delete the site by updating the ceaseDate and the status to close
    void deleteSite(@Param("id") Long id, @Param("ceaseDate") Date ceaseDate);

    //updateSiteStatus(id:integer, status:String)
    @Transactional
    @Modifying
    @Query("UPDATE Site s SET s.siteStatus = :siteStatus WHERE s.siteId = :id")
    void updateSiteStatus(@Param("id") Long id, @Param("siteStatus") String siteStatus);

    //updateSiteName(id:integer, name:String)
    @Transactional
    @Modifying
    @Query("UPDATE Site s SET s.siteName = :siteName WHERE s.siteId = :id")
    void updateSiteName(@Param("id") Long id, @Param("siteName") String siteName);


    //updateCeaseDate(id: Long, date: Timestamp)
    @Transactional
    @Modifying
    @Query("UPDATE Site s SET s.ceaseDate = :ceaseDate WHERE s.siteId = :id")
    void updateCeaseDate(@Param("id") Long id, @Param("ceaseDate") Date ceaseDate);





}
