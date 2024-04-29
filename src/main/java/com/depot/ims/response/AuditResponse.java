package com.depot.ims.response;

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
    String rowKey;
    String oldValue;
    String newValue;
    String action;
    Timestamp actionTimestamp;
}
