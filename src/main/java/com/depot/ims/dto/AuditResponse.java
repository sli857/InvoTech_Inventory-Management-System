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
    Long userId;
    String userName;
    String userPosition;
    String tableName;
    String fieldName;
    Integer rowKey;
    String oldValue;
    String newValue;
    String action;
    Timestamp actionTimestamp;

}
