package com.depot.ims.dto;

import com.depot.ims.models.Audit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@Builder
public class AuditResponse {
    Long auditId;
    String tableName;
    String fieldName;
    Long rowKey;
    String oldValue;
    String newValue;
    String action;
    Timestamp actionTimestamp;
}
