package com.depot.ims.controllers;

import com.depot.ims.models.Audit;
import com.depot.ims.repositories.AuditRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/audit")
    public ResponseEntity<Audit> getAuditById(@RequestParam Long auditId) {
        return ResponseEntity.ok(auditRepository.findByAuditId(auditId));
    }

    //@GetMapping("/audit")
    //public  ResponseEntity<List<Audit>> getAuditsByUserId(@RequestParam Long userId) {
    //    return ResponseEntity.ok(auditRepository.findByUserId(userId));
    //}

}
