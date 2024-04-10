package com.depot.ims.repositories;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for the availability entity using the H2 in-memory database.
 * Tests validate the basic CRUD operations provided by AvailabilityRepository.
 */
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class AvailabilityTest {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SiteRepository siteRepository;

    /**
     * Tests retrieval of all availability entities by site id.
     * Validates that the findBySiteId operation can successfully retrieve the correct
     * number of availability entities, and check the content of the return.
     */
    @Test
    void testFindBySiteId() {
        Site site = siteRepository.save(new Site("Site 1", "Location 1", "Open",
                null, true));
        Item item = itemRepository.save(new Item("Item 1", 12.99));
        availabilityRepository.save(new Availability(site, item, 10));
        List<Availability> result = availabilityRepository.findBySiteId(site.getSiteId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(site.getSiteId(), result.get(0).getSiteId().getSiteId());
    }

    /**
     * Tests retrieval of all availability entities by item id.
     * Validates that the findByItemId operation can successfully retrieve the correct
     * number of availability entities, and check the content of the return.
     */
    @Test
    void testFindByItemId() {
        Site site = siteRepository.save(new Site("Site 1", "Location 1", "Open",
                null, true));
        Item item = itemRepository.save(new Item("Item 1", 12.99));
        availabilityRepository.save(new Availability(site, item, 10));
        List<Availability> result = availabilityRepository.findByItemId(item.getItemId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getItemId(), result.get(0).getItemId().getItemId());
    }

    /**
     * Tests retrieval of a specific availability entity by item id and site id.
     * Validates that the findBySiteIdAndItemId operation can successfully retrieve the correct
     *availability entity
     */
    @Test
    void testFindBySiteIdAndItemId() {
        Site site = siteRepository.save(new Site("Site 1", "Location 1", "Open",
                null, true));
        Item item = itemRepository.save(new Item("Item 1", 12.99));

        Availability availability = availabilityRepository.save(new Availability(site, item, 10));

        Availability result = availabilityRepository.findBySiteIdAndItemId(site.getSiteId(), item.getItemId());

        assertNotNull(result);
        assertEquals(availability, result);
    }


    /**
     * Tests retrieval of a list of site entities by a list of item
     * Validates that the findSitesByItems operation can successfully retrieve the correct
     * sites entity
     */
    @Test
    void testFindSitesByItems() {
        // Create test data
        Item item1 = itemRepository.save(new Item("Item 1", 12.99));
        Item item2 = itemRepository.save(new Item("Item 2", 15.99));
        Site site1 = siteRepository.save(new Site("Site 1", "Location 1", "Open",
                null, true));
        Site site2 = siteRepository.save(new Site("Site 2", "Location 2", "Closed",
                null, false));

        availabilityRepository.save(new Availability(site1, item1, 10));
        availabilityRepository.save(new Availability(site2, item2, 20));

        // Call the repository method
        List<Site> result = availabilityRepository.findSitesByItems(List.of(item1, item2));

        // Assert the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(site1.getSiteId(), result.get(0).getSiteId());
        assertEquals(site2.getSiteId(), result.get(1).getSiteId());
    }

    /**
     * Tests retrieval of a list of site entities by an item
     * Validates that the findSitesByOneItem operation can successfully retrieve the correct
     * sites entity
     */
    @Test
    void testFindSitesByOneItem() {
        // Create test data
        Item item = itemRepository.save(new Item("Item 1", 12.99));
        Site site1 = siteRepository.save(new Site("Site 1", "Location 1", "Open",
                null, true));
        Site site2 = siteRepository.save(new Site("Site 2", "Location 2", "Closed",
                null, false));

        availabilityRepository.save(new Availability(site1, item, 10));
        availabilityRepository.save(new Availability(site2, item, 20));

        // Call the repository method
        List<Site> result = availabilityRepository.findSitesByOneItem(item);

        // Assert the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(site1.getSiteId(), result.get(0).getSiteId());
        assertEquals(site2.getSiteId(), result.get(1).getSiteId());
    }
}

