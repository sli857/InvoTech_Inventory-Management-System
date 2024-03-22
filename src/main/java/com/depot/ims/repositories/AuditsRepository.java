package com.depot.ims.repositories;

import com.depot.ims.models.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditsRepository extends JpaRepository<Audit, Long> {
    List<Audit> findAll();

    @Query("SELECT a FROM Audit a WHERE a.auditId=?1")
    Audit findByAuditId(Integer auditId);

    @Query("SELECT a from Audit a WHERE a.userId=?1")
    List<Audit> findByUserId(Integer userId);
}
