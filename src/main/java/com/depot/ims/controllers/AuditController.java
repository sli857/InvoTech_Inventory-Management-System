package com.depot.ims.controllers;

import com.depot.ims.repositories.AuditRepository;
import com.depot.ims.services.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Audit controller class provides APIs that perform READs on table Audits
 */
@RestController
@RequestMapping("/audits")
@CrossOrigin(origins = {"http://cs506-team-35.cs.wisc.edu", "http://localhost:5173/"})
public class AuditController {

    private final AuditRepository auditRepository;
    private final AuditService auditService;

    public AuditController(AuditRepository auditRepository, AuditService auditService) {
        this.auditRepository = auditRepository;
        this.auditService = auditService;
    }

    /**
     * get all audits by using the corresponding method in auditService
     *
     * @return ResponseEntity that contains the result of this operation
     */
    @GetMapping
    public ResponseEntity<?> getAllAudits() {
        return auditService.findAll();
    }

    /**
     * get all audits that relate to a specific table by using the corresponding method in
     * auditService class
     *
     * @param tableName the name of the table that the user want to search by for audits
     * @return ResponseEntity that contains the result of this operation
     */
    @GetMapping("/onTable")
    public ResponseEntity<?> getAuditsOnTable(@RequestParam String tableName) {
        return auditService.findAuditsOnTable(tableName);
    }

    /**
     * get all audits that were recorded between a specific time period by call the corresponding
     * method in auditService class
     *
     * @param start the start of the time period. Format: YYYY-MM-DD
     * @param end   the end of the time period. Format: YYYY-MM-DD
     * @return ResponseEntity that contains the result of this operation
     */
    @GetMapping("/betweenPeriod")
    public ResponseEntity<?> getAuditsBetweenPeriod(@RequestParam String start,
                                                    @RequestParam String end) {
        return auditService.findAuditsBetweenPeriod(start, end);

    }


}
