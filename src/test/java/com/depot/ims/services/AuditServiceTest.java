package com.depot.ims.services;

import com.depot.ims.dto.AuditResponse;
import com.depot.ims.models.Audit;
import com.depot.ims.repositories.AuditRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AuditServiceTest {
    @Mock
    AuditRepository auditRepositoryMock = mock(AuditRepository.class);
    @InjectMocks
    AuditService auditService = new AuditService(auditRepositoryMock);

    @Test
    void testFindAll(){
        Audit audit1 = Audit.builder()
                .tableName("sites")
                .fieldName("siteName")
                .rowKey("1")
                .oldValue("oldName")
                .newValue("newName")
                .action("UPDATE")
                .actionTimestamp(Timestamp.valueOf("2024-04-16 11:22:33"))
                .build();

        Audit audit2 = Audit.builder()
                .tableName("sites")
                .rowKey("1")
                .newValue("newSite")
                .action("INSERT")
                .actionTimestamp(Timestamp.valueOf("2024-02-16 11:22:33"))
                .build();

        Audit audit3 = Audit.builder()
                .tableName("items")
                .fieldName("itemName")
                .rowKey("1")
                .oldValue("oldName")
                .newValue("newName")
                .action("UPDATE")
                .actionTimestamp(Timestamp.valueOf("2024-03-16 11:22:33"))
                .build();
        List<Audit> expected = Stream.of(audit1,audit2,audit3).toList();
        when(auditRepositoryMock.findAll()).thenReturn(expected);
        ResponseEntity<?> res = auditService.findAll();
        assertNotNull(res);
        assertTrue(res.getStatusCode().is2xxSuccessful());
        assertTrue(res.hasBody());
    }

    @Test
    void testFindAuditsOnTable(){
        Audit audit1 = Audit.builder()
                .tableName("sites")
                .fieldName("siteName")
                .rowKey("1")
                .oldValue("oldName")
                .newValue("newName")
                .action("UPDATE")
                .actionTimestamp(Timestamp.valueOf("2024-04-16 11:22:33"))
                .build();

        Audit audit2 = Audit.builder()
                .tableName("sites")
                .rowKey("1")
                .newValue("newSite")
                .action("INSERT")
                .actionTimestamp(Timestamp.valueOf("2024-02-16 11:22:33"))
                .build();

        Audit audit3 = Audit.builder()
                .tableName("items")
                .fieldName("itemName")
                .rowKey("1")
                .oldValue("oldName")
                .newValue("newName")
                .action("UPDATE")
                .actionTimestamp(Timestamp.valueOf("2024-03-16 11:22:33"))
                .build();

        List<Audit> expected = Stream.of(audit1, audit2).toList();
        when(auditRepositoryMock.findByTableName("sites")).thenReturn(expected);
        ResponseEntity<?> res1 = auditService.findAuditsOnTable("sites");
        assertNotNull(res1);
        assertTrue(res1.getStatusCode().is2xxSuccessful());
        assertTrue(res1.hasBody());

        ResponseEntity<?> res2 = auditService.findAuditsOnTable(null);
        assertNotNull(res2);
        assertTrue(res2.getStatusCode().is4xxClientError());
        assertEquals("tableName should not be null", res2.getBody());
    }

    @Test
    void testFindAuditsBetweenPeriod(){
        Audit audit1 = Audit.builder()
                .tableName("sites")
                .fieldName("siteName")
                .rowKey("1")
                .oldValue("oldName")
                .newValue("newName")
                .action("UPDATE")
                .actionTimestamp(Timestamp.valueOf("2024-04-16 11:22:33"))
                .build();

        Audit audit2 = Audit.builder()
                .tableName("sites")
                .rowKey("1")
                .newValue("newSite")
                .action("INSERT")
                .actionTimestamp(Timestamp.valueOf("2024-02-16 11:22:33"))
                .build();

        Audit audit3 = Audit.builder()
                .tableName("items")
                .fieldName("itemName")
                .rowKey("1")
                .oldValue("oldName")
                .newValue("newName")
                .action("UPDATE")
                .actionTimestamp(Timestamp.valueOf("2024-03-16 11:22:33"))
                .build();
        List<Audit> expected = Stream.of(audit1,audit2,audit3).toList();

        Timestamp start = Timestamp.valueOf(LocalDate.parse("2023-09-01").atStartOfDay());
        Timestamp end = Timestamp.valueOf(LocalDate.parse("2024-09-01").atStartOfDay());

        when(auditRepositoryMock.findBetweenPeriod(start,end))
                .thenReturn(expected);

        ResponseEntity<?> res1 = auditService.findAuditsBetweenPeriod("May 19th 2023","Jun 30th " +
                "2024");
        assertNotNull(res1);
        assertTrue(res1.getStatusCode().is4xxClientError());
        assertTrue(res1.hasBody());

        ResponseEntity<?> res2 = auditService.findAuditsBetweenPeriod("2023-09-01","2024-09-01");
        assertNotNull(res2);
        assertTrue(res2.getStatusCode().is2xxSuccessful());
        assertTrue(res2.hasBody());
    }
}
