package com.depot.ims.repositories;

import com.depot.ims.models.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface AuditRepository extends JpaRepository<Audit, Long> {

    List<Audit> findAll();
    @Query("SELECT a FROM Audit a WHERE a.auditId=?1")
    Audit findByAuditId(Long auditId);

    @Query("SELECT a from Audit a WHERE a.tableName=?1")
    List<Audit> findByTableName(String tableName);

    @Query("SELECT a FROM Audit a WHERE a.actionTimestamp >= ?1 AND a.actionTimestamp <= ?2")
    List<Audit> findBetweenPeriod(Timestamp start, Timestamp end);
}
