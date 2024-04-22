package com.depot.ims.controllers;

import com.depot.ims.models.Audit;
import com.depot.ims.models.User;
import com.depot.ims.repositories.AuditRepository;
import com.depot.ims.services.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuditControllerTest {
    @InjectMocks
    AuditController auditControllerMock;
    @Mock
    AuditService auditServiceMock;
    @Mock
    AuditRepository auditRepositoryMock;
    private MockMvc mockMvc;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(auditControllerMock).build();
    }
    @Test
    void testGetAllAudits() throws Exception {
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
        List<Audit> result = new ArrayList<>(Arrays.asList(audit1, audit2, audit3));

        doReturn(ResponseEntity.ok(result)).when(auditServiceMock).findAll();

        mockMvc.perform(get("/audits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }


    @Test
    void testFindAuditsOnTable() throws Exception{
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
        List<Audit> result = new ArrayList<>(Arrays.asList(audit1, audit2));
        doReturn(ResponseEntity.ok(result)).when(auditServiceMock).findAuditsOnTable("sites");
        mockMvc.perform(get("/audits/onTable?tableName=sites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testFindAuditsBetweenDates() throws Exception{
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
        List<Audit> result = new ArrayList<>(Arrays.asList(audit2, audit3));
        doReturn(ResponseEntity.ok(result)).when(auditServiceMock)
                .findAuditsBetweenPeriod("2024-02-01","2024-04-01");
        mockMvc.perform(get("/audits/betweenPeriod?start=2024-02-01&end=2024-04-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
