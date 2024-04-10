package com.depot.ims.services;

import com.depot.ims.models.Site;
import com.depot.ims.repositories.SiteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * SiteServiceTest tests the SiteService class, with mocking the siteRepository
 */
public class SiteServiceTest {
    @Mock
    private SiteRepository siteRepositoryMock = mock(SiteRepository.class);
    private final SiteService siteService = new SiteService(siteRepositoryMock, null, null);


    @BeforeEach
    void setup() {
    }

    /**
     * Test get a site by siteId or siteName. Test that two parameter cannot be both null
     */
    @Test
    void testGetSite() {
        Site site1 = new Site(1L, "site1", "location1", "open", null, true);
        Site site2 = new Site(2L, "site2", "location2", "open", null, true);

        when(siteRepositoryMock.findBySiteId(1L)).thenReturn(site1);
        when(siteRepositoryMock.findBySiteName("site2")).thenReturn(site2);

        ResponseEntity<?> res1 = siteService.getSite(1L, null);
        ResponseEntity<?> res2 = siteService.getSite(null, "site2");
        ResponseEntity<?> res3 = siteService.getSite(null, null);

        Stream.of(res1, res2, res3).forEach(Assertions::assertNotNull);
        assertEquals(ResponseEntity.ok(site1), res1);
        assertEquals(ResponseEntity.ok(site2), res2);
        assertTrue(res3.getStatusCode().is4xxClientError());
    }

    /**
     * test get status by siteId. Test siteId cannot be null
     */
    @Test
    void testGetStatusBySiteId() {
        Site site1 = new Site(1L, "site1", "location1", "open", null, true);

        when(siteRepositoryMock.findSiteStatusBySiteId(1L)).thenReturn(site1.getSiteStatus());
        when(siteRepositoryMock.findSiteStatusBySiteId(2L)).thenReturn(null);

        assertEquals(ResponseEntity.ok("open"), siteService.getStatusBySiteId(1L));
        assertTrue(siteService.getStatusBySiteId(2L).getStatusCode().is4xxClientError());

    }

    /**
     * test add a site, examine the return responseEntity has the correct site in its body
     */
    @Test
    void testAdd(){
        Site site1 = new Site(1L, "site1", "location1", "open", null, true);

        when(siteRepositoryMock.save(any())).thenReturn(site1);
        assertEquals(site1, siteService.addSite(site1).getBody());
    }

    /**
     * test update a site entity on any field of itself
     */
    @Test
    void testUpdateSite() {
        Site site1 = new Site(1L, "site1", "location1", "open", null, true);
        Site site2 = new Site(1L, "site1Updated", "location1Updated", "open", null, true);
        when(siteRepositoryMock.existsById(1L)).thenReturn(true);
        when(siteRepositoryMock.findBySiteId(1L)).thenReturn(site1);
        when(siteRepositoryMock.save(any())).thenReturn(site2);

        assertTrue(siteService.updateSite(null, "site1", "location1", "open", null, true).getStatusCode().is4xxClientError());
        assertTrue(siteService.updateSite(2L, "site1", "location1", "open", null, true).getStatusCode().is4xxClientError());
        assertTrue(siteService.updateSite(1L, null, null, null, null, null).getStatusCode().is4xxClientError());
        assertEquals(site2, siteService.updateSite(1L, "site1Updated", "location1Updated", "open",
                null, true).getBody());
    }


    /**
     * test delete a site by siteId; test set status to "closed"; test set ceaseDate to current
     * time if ceaseDate is not provided
     */
    @Test
    void testDelete(){

        Site site1 = new Site(1L, "site1", "location1", "open", null, true);
        Site site2 = new Site(1L, "site1","location1","closed", Date.valueOf("2024-4-5"),true);

        when(siteRepositoryMock.existsById(1L)).thenReturn(true);
        when(siteRepositoryMock.findBySiteId(1L)).thenReturn(site1);
        doReturn(site2).when(siteRepositoryMock).saveAndFlush(any());

        assertEquals(site2,siteService.deleteSite(1L, "2024-4-5").getBody());
    }
}
