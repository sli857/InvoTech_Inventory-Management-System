package com.depot.ims.response;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * An auditResponse is a DTO for audit entity that screens out user information, so the frontend
 * won't
 * receive these
 * sensitive data.
 */
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
