package com.depot.ims.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents an audit entity Encapsulates user details and credentials for access
 * control and identification. Annotations from JPA are used for ORM (Object Relational Mapping) to
 * a database table, while Lombok annotations reduce boilerplate code for standard Java
 * functionalities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Audits")
public class Audit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PK_audits", updatable = false, nullable = false)
  private Long auditId;

  @Column(name = "table_name", nullable = false)
  private String tableName;

  @Column(name = "field_name")
  private String fieldName;

  @Column(name = "row_key", nullable = false)
  private String rowKey;

  @Column(name = "old_value")
  private String oldValue;

  @Column(name = "new_value")
  private String newValue;

  @Column(name = "action", nullable = false)
  private String action;

  @Column(name = "action_timestamp", nullable = false)
  private Timestamp actionTimestamp;
}
