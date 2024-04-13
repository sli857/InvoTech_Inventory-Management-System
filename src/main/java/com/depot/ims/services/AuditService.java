package com.depot.ims.services;

import com.depot.ims.dto.AuditResponse;
import com.depot.ims.models.Audit;
import com.depot.ims.repositories.AuditRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
