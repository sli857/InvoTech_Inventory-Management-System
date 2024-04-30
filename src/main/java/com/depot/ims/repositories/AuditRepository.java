package com.depot.ims.repositories;

import com.depot.ims.models.Audit;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** Audit Repository interface extends JpaRepository for performing CRUD on table Audits. */
public interface AuditRepository extends JpaRepository<Audit, Long> {

  List<Audit> findAll();

  /**
   * Find an audit by audit ID.
   *
   * @param auditId auditID
   * @return the audit entity that has the given audit ID
   */
  @Query("SELECT a FROM Audit a WHERE a.auditId=?1")
  Audit findByAuditId(Long auditId);

  /**
   * Find a list of audits that record changes performed on a specific table.
   *
   * @param tableName tableName
   * @return a list of audits
   */
  @Query("SELECT a from Audit a WHERE a.tableName=?1")
  List<Audit> findByTableName(String tableName);

  /**
   * Find a list of audits that were recorded between a time period.
   *
   * @param start start of the time period
   * @param end end of the time period
   * @return a list of audits
   */
  @Query("SELECT a FROM Audit a WHERE a.actionTimestamp >= ?1 AND a.actionTimestamp <= ?2")
  List<Audit> findBetweenPeriod(Timestamp start, Timestamp end);
}
