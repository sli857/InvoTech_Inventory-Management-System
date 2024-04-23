package com.depot.ims.repositories;

import com.depot.ims.models.Site;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


// Annotation to specify property sources for the test
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=update"
})
public class SiteTest {
    @Autowired
    private SiteRepository siteRepository;

    @BeforeEach
    void setup() {
        Site site1 = createSite("site1", "location1", "open", null, true);
        Site site2 = createSite("site2", "location2", "closed", null, true);
    }

    @Test
    void testFindAll() {
        List<Site> res = siteRepository.findAll();
        System.out.println(res);
        assertNotNull(res);
        assertEquals(2, res.size());
    }

    @Test
    void testFindSiteById() {
        System.out.println(siteRepository.findAll());
        Site res = siteRepository.findBySiteId(7L);
        assertNotNull(res);
        assertEquals(7L, res.getSiteId());
    }

    @Test
    void testFindSiteByName() {
        System.out.println(siteRepository.findAll());
        Site res = siteRepository.findBySiteName("site2");
        assertNotNull(res);
        assertEquals("site2", res.getSiteName());
    }

    @Test
    void testFindStatusBySiteId() {
        System.out.println(siteRepository.findAll());
        String res = siteRepository.findSiteStatusBySiteId(4L);
        assertNotNull(res);
        assertEquals("closed", res);
    }

    private Site createSite(String siteName,
                            String siteLocation,
                            String siteStatus,
                            String ceaseDate, Boolean internalSite) {
        Site site = Site.builder()
                .siteName(siteName)
                .siteLocation(siteLocation)
                .siteStatus(siteStatus)
                .internalSite(internalSite)
                .build();
        if (ceaseDate != null) site.setCeaseDate(Date.valueOf(ceaseDate));
        return siteRepository.save(site);
    }
}
