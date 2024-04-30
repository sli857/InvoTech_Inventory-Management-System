package com.depot.ims.repositories;

import com.depot.ims.models.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** Site Repository interface extends JpaRepository for performing CRUD on table Sites. */
public interface SiteRepository extends JpaRepository<Site, Long> {

  /**
   * Find a site that has a given site name.
   *
   * @param siteName siteName
   * @return a site entity
   */
  @Query("select s from Site s where s.siteName = ?1")
  Site findBySiteName(String siteName);

  /**
   * Find a site that has a given site ID.
   *
   * @param siteId siteId
   * @return a site entity
   */
  @Query("SELECT s FROM Site s WHERE s.siteId=?1")
  Site findBySiteId(Long siteId);

  /**
   * Find the status of a site that has the given site ID.
   *
   * @param siteId siteId
   * @return site Status in String format
   */
  @Query("SELECT s.siteStatus FROM Site s WHERE s.siteId=?1")
  String findSiteStatusBySiteId(Long siteId);
}
