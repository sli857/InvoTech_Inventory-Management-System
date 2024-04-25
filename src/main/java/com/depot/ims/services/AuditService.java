package com.depot.ims.services;

import com.depot.ims.dto.AuditResponse;
import com.depot.ims.models.Audit;
import com.depot.ims.repositories.AuditRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
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
      LocalDate startTmp = LocalDate.parse(start);
      LocalDate endTmp = LocalDate.parse(end);
      List<Audit> result =
          auditRepository.findBetweenPeriod(
              Timestamp.valueOf(startTmp.atStartOfDay()), Timestamp.valueOf(endTmp.atStartOfDay()));
      List<AuditResponse> responses =
          result.stream().map(AuditService::convertToAuditResponse).toList();
      return ResponseEntity.ok(responses);
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
