package com.depot.ims.controllers;

import com.depot.ims.models.Site;
import com.depot.ims.repositories.SiteRepository;
import com.depot.ims.services.SiteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SiteControllerTest {
    @InjectMocks
    SiteController siteControllerMock;
    @Mock
    SiteRepository siteRepositoryMock;
    @Mock
    SiteService siteServiceMock;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(siteControllerMock).build();
    }

    @Test
    void testGetAllSites() throws Exception {
        // Given
        List<Site> sites = new ArrayList<>();
        sites.add(new Site());
        sites.add(new Site());

        // When
        when(siteRepositoryMock.findAll()).thenReturn(sites);

        mockMvc.perform(get("/sites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetSiteStatus() throws Exception {

        ResponseEntity<?> res = new ResponseEntity<>(
                "open",
                null,
                HttpStatus.OK
        );
        doReturn(res).when(siteServiceMock).getStatusBySiteId(any());
        mockMvc.perform(get("/sites/status?siteId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("open"));
    }

    @Test
    void testGetSiteBySiteId() throws Exception {
        Long site1Id = 1L;
        String site2Name = "site2";
        Site site1 = Site.builder()
                .siteId(site1Id)
                .siteName("site1")
                .siteLocation("location1")
                .siteStatus("open")
                .internalSite(true)
                .build();

        Site site2 = Site.builder()
                .siteId(2L)
                .siteName(site2Name)
                .siteLocation("location2")
                .siteStatus("open")
                .internalSite(true)
                .build();

        ResponseEntity<Site> res1 = new ResponseEntity<>(
                site1,
                null,
                HttpStatus.OK
        );
        ResponseEntity<Site> res2 = new ResponseEntity<>(
                site2,
                null,
                HttpStatus.OK
        );

        doReturn(res1).when(siteServiceMock).getSite(site1Id, null);
        doReturn(res2).when(siteServiceMock).getSite(null, site2Name);

        mockMvc.perform(get("/sites/site?siteId=" + site1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(site1));

        mockMvc.perform(get("/sites/site?siteName=" + site2Name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(site2));

    }

    // TODO use full test after updating deleteSite() in siteService
    @Test
    void testDelete() throws Exception {
        Site site = new Site(1L, "site1","location1","closed", Date.valueOf("2024-4-5"),true);

        doReturn(ResponseEntity.ok(site)).when(siteServiceMock).deleteSite(1L);

        mockMvc.perform(delete("/sites/delete?siteId=1"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$").value(site))
        ;
    }
}

