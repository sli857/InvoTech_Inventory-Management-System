package com.depot.ims.services;

import com.depot.ims.models.Audit;
import com.depot.ims.repositories.AuditRepository;
import com.depot.ims.response.AuditResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
  private final AuditRepository auditRepository;

  public AuditService(AuditRepository auditRepository) {
    this.auditRepository = auditRepository;
  }

  private static AuditResponse convertToAuditResponse(Audit audit) {

    return AuditResponse.builder()
        .auditId(audit.getAuditId())
        .tableName(audit.getTableName())
        .fieldName(audit.getFieldName())
        .rowKey(audit.getRowKey())
        .oldValue(audit.getOldValue())
        .newValue(audit.getNewValue())
        .action(audit.getAction())
        .actionTimestamp(audit.getActionTimestamp())
        .build();
  }

  public ResponseEntity<?> findAll() {
    try {
      List<Audit> result = auditRepository.findAll();
      List<AuditResponse> responses =
          result.stream().map(AuditService::convertToAuditResponse).toList();
      return ResponseEntity.ok(responses);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  public ResponseEntity<?> findAuditsOnTable(String tableName) {
    if (tableName == null) {
      return ResponseEntity.badRequest().body("tableName should not be null");
    }
    try {
      List<Audit> result = auditRepository.findByTableName(tableName);
      List<AuditResponse> responses =
          result.stream().map(AuditService::convertToAuditResponse).toList();
      return ResponseEntity.ok(responses);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  public ResponseEntity<?> findAuditsBetweenPeriod(String start, String end) {
    try {
      LocalDate startDate = LocalDate.parse(start);
      LocalDate endDate = LocalDate.parse(end);
      List<Audit> result =
          auditRepository.findBetweenPeriod(
              Timestamp.valueOf(startDate.atStartOfDay()),
              Timestamp.valueOf(endDate.atStartOfDay()));
      List<AuditResponse> responses =
          result.stream().map(AuditService::convertToAuditResponse).toList();
      return ResponseEntity.ok(responses);
    } catch (DateTimeParseException e) {
      return ResponseEntity.badRequest().body("Datetime format should be:\nYYYY-MM-DD");
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  public void saveAudit(
      String tableName,
      String fieldName,
      String rowKey,
      String oldValue,
      String newValue,
      String action) {
    auditRepository.save(
        Audit.builder()
            .tableName(tableName)
            .fieldName(fieldName)
            .rowKey(rowKey)
            .oldValue(oldValue)
            .newValue(newValue)
            .action(action)
            .actionTimestamp(Timestamp.from(Instant.now()))
            .build());
  }
}
