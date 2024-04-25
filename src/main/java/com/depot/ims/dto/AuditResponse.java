package com.depot.ims.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
