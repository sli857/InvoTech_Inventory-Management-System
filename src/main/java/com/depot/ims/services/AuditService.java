package com.depot.ims.services;

import com.depot.ims.dto.AuditResponse;
import com.depot.ims.models.Audit;
import com.depot.ims.repositories.AuditRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;


/**
 * This class provides methods for business level logic operations on Audits
 */
@Service
public class AuditService {

    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    /**
     * this method convert an Audit object to AuditResponse object to screen out sensitive user's
     * information, such as password.
     *
     * @param audit the Audit object to convert
     * @return the converted AuditResponse
     */
    private static AuditResponse convertToAuditResponse(Audit audit) {

        return AuditResponse.builder()
                .auditId(audit.getAuditId())
                .tableName(audit.getTableName())
                .fieldName(audit.getFieldName())
                .rowKey(audit.getRowKey())
                .oldValue(audit.getOldValue())
                .newValue(audit.getNewValue())
                .action(audit.getAction())
                .actionTimestamp(audit.getActionTimestamp())
                .build();
    }


    /**
     * find all audits from table Audits. Apply convertToAuditResponse() on each audit.
     *
     * @return ResponseEntity OK with a list of AuditResponses; or InternalServerError if
     * exception happens
     */
    public ResponseEntity<?> findAll() {
        try {
            List<Audit> result = auditRepository.findAll();

            // convert each Audit from the result to AuditResponse
            List<AuditResponse> responses = result.stream().map(AuditService::convertToAuditResponse).toList();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * find all audits that relates to a specific table. Apply convertToAuditResponse() on each
     * audit.
     *
     * @param tableName the name of a table that user want to use to search by for audits
     * @return ResponseEntity OK with a list of AuditResponses; or InternalServerError if
     * * exception happens
     */
    public ResponseEntity<?> findAuditsOnTable(String tableName) {
        if (tableName == null) {
            return ResponseEntity.badRequest().body("tableName should not be null");
        }
        try {
            List<Audit> result = auditRepository.findByTableName(tableName);

            // convert each Audit from the result to AuditResponse
            List<AuditResponse> responses = result.stream().map(AuditService::convertToAuditResponse).toList();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * find all audits that are recorded between a specific time period. Apply convertToAuditResponse() on each row.
     *
     * @param start the start of the time period. Format: YYYY-MM-DD
     * @param end the end of the time period. Format: YYYY-MM-DD
     * @return ResponseEntity OK with a list of AuditResponses;
     *         ResponseEntity badRequest if the client sends incorrect time format;
     *         InternalServerError if other exception happens
     */
    public ResponseEntity<?> findAuditsBetweenPeriod(String start, String end) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            List<Audit> result = auditRepository.findBetweenPeriod(Timestamp.valueOf(startDate.atStartOfDay()),
                    Timestamp.valueOf(endDate.atStartOfDay()));
            // convert each Audit from the result to AuditResponse
            List<AuditResponse> responses = result.stream().map(AuditService::convertToAuditResponse).toList();
            return ResponseEntity.ok(responses);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Datetime format should be:\nYYYY-MM-DD");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * helper method for other service classes to save an audit into database
     * @param tableName the table that has been changed
     * @param fieldName the field of the table that has been changed. Null if the operation is
     *                  INSERT or DELETE
     * @param rowKey    the value of the primary key of the row that has been changed
     * @param oldValue  old value. Null if the operation is INSERT
     * @param newValue  new value. Null if the operation is DELETE
     * @param action    Enum from INSERT, UPDATE, DELETE
     */
    public void saveAudit(String tableName, String fieldName, String rowKey,
                          String oldValue, String newValue, String action) {
        auditRepository.save(
                Audit.builder()
                        .tableName(tableName)
                        .fieldName(fieldName)
                        .rowKey(rowKey)
                        .oldValue(oldValue)
                        .newValue(newValue)
                        .action(action)
                        .actionTimestamp(Timestamp.from(Instant.now()))
                        .build()
        );
    }

}
