package com.depot.ims.services;

import com.depot.ims.dto.AuditResponse;
import com.depot.ims.models.Audit;
import com.depot.ims.repositories.AuditRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
public class AuditService {
    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public ResponseEntity<?> findAll(){
        List<Audit> result = auditRepository.findAll();
        List<AuditResponse> responses =result.stream().map(AuditService::convertToAuditResponse).toList();
        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<?> findAuditsByUser(Long userId){
        List<Audit> result = auditRepository.findByUserId(userId);
        List<AuditResponse> responses =result.stream().map(AuditService::convertToAuditResponse).toList();
        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<?> findAuditsOnTable(String tableName){
        List<Audit> result = auditRepository.findByTableName(tableName);
        List<AuditResponse> responses =result.stream().map(AuditService::convertToAuditResponse).toList();
        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<?> findAuditsBetweenPeriod(String start, String end){
        LocalDate startTmp = LocalDate.parse(start);
        LocalDate endTmp = LocalDate.parse(end);
        List<Audit> result = auditRepository.findBetweenPeriod(Timestamp.valueOf(startTmp.atStartOfDay()),
                Timestamp.valueOf(endTmp.atStartOfDay()));
        List<AuditResponse> responses =result.stream().map(AuditService::convertToAuditResponse).toList();
        return ResponseEntity.ok(responses);
    }

    private static AuditResponse convertToAuditResponse(Audit audit){
        return AuditResponse.builder()
                .auditId(audit.getAuditId())
                .userId(audit.getUserId().getUserId())
                .userName(audit.getUserId().getUsername())
                .userPosition(audit.getUserId().getPosition())
                .tableName(audit.getTableName())
                .fieldName(audit.getFieldName())
                .rowKey(audit.getRowKey())
                .oldValue(audit.getOldValue())
                .newValue(audit.getNewValue())
                .action(audit.getAction())
                .actionTimestamp(audit.getActionTimestamp())
                .build();
    }

}
