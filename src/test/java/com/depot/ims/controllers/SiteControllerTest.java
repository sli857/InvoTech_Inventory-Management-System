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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.xml.transform.Result;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Junit tests for siteController, siteRepository and siteService are mocked
 */
public class SiteControllerTest {
    @InjectMocks
    SiteController siteControllerMock;
    @Mock
    SiteRepository siteRepositoryMock;
    @Mock
    SiteService siteServiceMock;

    private MockMvc mockMvc;

    /**
     * before each test, instantiate mockMvc
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(siteControllerMock).build();
    }

    /**
     * test findAll() by mocking the return value of siteRepository
     * @throws Exception
     */
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

    /**
     * test getStatusBySiteId
     */
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

    /**
     * test get a site by siteId and get a site by siteName
     */
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

    /**
     * test delete a site
     */
    @Test
    void testDelete() throws Exception {
        Site site1 = new Site(1L, "site1","location1","closed", Date.valueOf("2024-4-5"),true);
        Site site2 = new Site(2L, "site2","location2","closed", Date.valueOf(LocalDate.now()),true);

        doReturn(ResponseEntity.ok(site1)).when(siteServiceMock).deleteSite(1L,"2024-4-5");
        doReturn(ResponseEntity.ok(site2)).when(siteServiceMock).deleteSite(2L,null);

        mockMvc.perform(delete("/sites/delete?siteId=1&ceaseDate=2024-4-5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.siteStatus").value("closed"));

        mockMvc.perform(delete("/sites/delete?siteId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.siteStatus").value("closed"));
    }
}

