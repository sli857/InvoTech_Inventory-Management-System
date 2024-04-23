package com.depot.ims.controllers;

import com.depot.ims.repositories.AuditRepository;
import com.depot.ims.services.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audits")
@CrossOrigin(origins = "http://localhost:5173/")
public class AuditController {

    private final AuditRepository auditRepository;
    private final AuditService auditService;

    public AuditController(AuditRepository auditRepository, AuditService auditService) {
        this.auditRepository = auditRepository;
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<?> getAllAudits() {
        return auditService.findAll();
    }

    @GetMapping("/onTable")
    public ResponseEntity<?> getAuditOnTable(@RequestParam String tableName) {
        return auditService.findAuditsOnTable(tableName);
    }

    @GetMapping("/betweenPeriod")
    public ResponseEntity<?> getAuditsBetweenPeriod(@RequestParam String start,
                                                    @RequestParam String end) {
        return auditService.findAuditsBetweenPeriod(start, end);

    }


}
