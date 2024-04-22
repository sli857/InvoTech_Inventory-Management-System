package com.depot.ims.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AuditorAware;

import java.sql.Timestamp;
import java.util.Optional;

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

