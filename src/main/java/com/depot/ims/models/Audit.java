package com.depot.ims.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Audits")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_audits", updatable = false, nullable = false)
    private Long auditId;

    @ManyToOne
    @JoinColumn(name = "FK_audits_users", referencedColumnName = "PK_users", nullable = false)
    private User userId;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Column(name = "row_key", nullable = false)
    private Integer rowKey;

    @Column(name = "old_value", nullable = false)
    private String oldValue;

    @Column(name = "new_value", nullable = false)
    private String newValue;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "action_timestamp", nullable = false)
    private Timestamp actionTimestamp;
}
