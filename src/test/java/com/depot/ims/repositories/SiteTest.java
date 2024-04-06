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

@DataJpaTest
// Annotation to specify property sources for the test
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
        Site site2 = createSite("site2", "location2", "open", null, true);
    }

    @Test
    void findAllTest() {
        List<Site> res = siteRepository.findAll();
        assertNotNull(res);
        assertEquals(2, res.size());
    }

    @Test
    void findSiteByIdTest() {
        Site res = siteRepository.findBySiteId(3L);
        assertNotNull(res);
        assertEquals("site1", res.getSiteName());
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
        return siteRepository.saveAndFlush(site);
    }
}
