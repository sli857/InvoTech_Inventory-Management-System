package com.depot.ims.repositories;

import com.depot.ims.models.Audit;
import com.depot.ims.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        User user1 = User.builder()
                .username("user1")
                .password("pass")
                .position("admin")
                .build();
        User user2 = User.builder()
                .username("user2")
                .password("pass")
                .position("admin")
                .build();
        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);
        Audit audit1 = Audit.builder()
                .userId(user1)
                .tableName("sites")
                .fieldName("siteName")
                .rowKey(1)
                .oldValue("oldName")
                .newValue("newName")
                .action("UPDATE")
                .actionTimestamp(Timestamp.valueOf("2024-04-16 11:22:33"))
                .build();

        Audit audit2 = Audit.builder()
                .userId(user2)
                .tableName("sites")
                .rowKey(1)
                .newValue("newSite")
                .action("INSERT")
                .actionTimestamp(Timestamp.valueOf("2024-02-16 11:22:33"))
                .build();

        Audit audit3 = Audit.builder()
                .userId(user1)
                .tableName("items")
                .fieldName("itemName")
                .rowKey(1)
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
    void testFindByUserId(){
        List<Audit> result = auditRepository.findByUserId(1L);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getAuditId());
        assertEquals(3L, result.get(1).getAuditId());
    }

    @Test
    void testFindByTableName(){
        List<Audit> result = auditRepository.findByTableName("sites");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(7L, result.get(0).getAuditId());
        assertEquals(8L, result.get(1).getAuditId());
    }
}
