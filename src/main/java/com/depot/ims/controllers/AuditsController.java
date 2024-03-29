package com.depot.ims.controllers;

import com.depot.ims.models.Audit;
import com.depot.ims.repositories.AuditsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audits")
public class AuditsController {

    private final AuditsRepository auditsRepository;

    public AuditsController(AuditsRepository auditsRepository) {
        this.auditsRepository = auditsRepository;
    }

    @GetMapping
    public ResponseEntity<List<Audit>> getAllAudits() {
        return ResponseEntity.ok(auditsRepository.findAll());
    }

    @GetMapping("/auditId={auditId}")
    public Audit getAuditById(@PathVariable Integer auditId) {
        return auditsRepository.findByAuditId(auditId);
    }

    @GetMapping("/userId={userId}")
    public List<Audit> getAuditsByUserId(@PathVariable Integer userId) {
        return auditsRepository.findByUserId(userId);
    }

}
