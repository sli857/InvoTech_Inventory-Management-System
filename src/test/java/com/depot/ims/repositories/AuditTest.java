package com.depot.ims.repositories;

import com.depot.ims.models.Audit;
import com.depot.ims.models.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})

public class AuditTest {
    @Autowired
    AuditRepository auditRepository;
    @Autowired
    UserRepository userRepository;


    @BeforeEach
    void testSaveAudit(){
        auditRepository.deleteAll();
        Audit audit1 = Audit.builder()
                //.auditId(1L)
                .tableName("sites")
                .fieldName("siteName")
                .rowKey("1")
                .oldValue("oldName")
                .newValue("newName")
                .action("UPDATE")
                .actionTimestamp(Timestamp.valueOf("2024-04-16 11:22:33"))
                .build();

        Audit audit2 = Audit.builder()
                //.auditId(2L)
                .tableName("sites")
                .rowKey("1")
                .newValue("newSite")
                .action("INSERT")
                .actionTimestamp(Timestamp.valueOf("2024-02-16 11:22:33"))
                .build();

        Audit audit3 = Audit.builder()
                //.auditId(3L)
                .tableName("items")
                .fieldName("itemName")
                .rowKey("1")
                .oldValue("oldName")
                .newValue("newName")
                .action("UPDATE")
                .actionTimestamp(Timestamp.valueOf("2024-03-16 11:22:33"))
                .build();

        auditRepository.saveAndFlush(audit1);
        auditRepository.saveAndFlush(audit2);
        auditRepository.saveAndFlush(audit3);
    }

    @Test
    void testFindAll(){
        List<Audit> result = auditRepository.findAll();
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testFindByTableName(){
        List<Audit> result = auditRepository.findByTableName("sites");
        assertNotNull(result);
        assertEquals(2, result.size());
        result.forEach(x->assertEquals("sites", x.getTableName()));
    }

    @Test
    void testFindBetweenPeriod(){
        Timestamp start = Timestamp.valueOf("2024-2-28 11:22:33");
        Timestamp end = Timestamp.valueOf("2024-3-28 11:22:33");
        List<Audit> result = auditRepository.findBetweenPeriod(start,end);
        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals("2024-03-16 11:22:33.0", result.get(0).getActionTimestamp().toString());
    }
}
