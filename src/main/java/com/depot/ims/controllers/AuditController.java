package com.depot.ims.controllers;

import com.depot.ims.models.Audit;
import com.depot.ims.repositories.AuditRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audits")
public class AuditController {

    private final AuditRepository auditRepository;

    public AuditController(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @GetMapping
    public ResponseEntity<List<Audit>> getAllAudits() {
        return ResponseEntity.ok(auditRepository.findAll());
    }

    @GetMapping("/auditId={auditId}")
    public Audit getAuditById(@PathVariable Long auditId) {
        return auditRepository.findByAuditId(auditId);
    }

    @GetMapping("/userId={userId}")
    public List<Audit> getAuditsByUserId(@PathVariable Long userId) {
        return auditRepository.findByUserId(userId);
    }

}
