package com.depot.ims.repositories;

import com.depot.ims.models.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuditRepository extends JpaRepository<Audit, Long> {

    @Query("SELECT a FROM Audit a WHERE a.auditId=?1")
    Audit findByAuditId(Long auditId);

    @Query("SELECT a from Audit a WHERE a.userId=?1")
    List<Audit> findByUserId(Long userId);

}
