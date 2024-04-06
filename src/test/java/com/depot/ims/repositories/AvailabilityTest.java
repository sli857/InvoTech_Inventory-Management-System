package com.depot.ims.repositories;



import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import com.depot.ims.models.Availability;
import com.depot.ims.models.Site;
import com.depot.ims.models.Item;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@DataJpaTest
// Annotation to specify property sources for the test
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=update"
})
public class AvailabilityTest {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SiteRepository siteRepository;


    @Test
    void testFindBySiteId() {
        Date currentDate = new Date(2002, 11, 1);
        Item item1 = createItem("item 1", 12.99);
        Item item2 = createItem("item 2", 15.99);
        Site site1 = createSite("name1", "location", "Open", currentDate, true);
        Site site2 = createSite("name2", "location", "Open", currentDate, true);
        Availability availability = createAvailability(site1, item1, 10);
        Availability availability1 = createAvailability(site2, item1, 20);

        List<Availability> list = this.availabilityRepository.findBySiteId(1L);

        assertNotNull(list);
        assertEquals(1, list.size());
        List<Availability> expectedList = new ArrayList<>();
        expectedList.add(availability);
        assertEquals(list, expectedList);
    }


    @Test
    void TestFindByItemId() {
        Date currentDate = new Date(2002, 11, 1);
        Item item1 = createItem("21", 12.99);
        Item item2 = createItem("22", 15.99);
        Site site1 = createSite("21", "location", "Open", currentDate, true);
        Site site2 = createSite("22", "location", "Open", currentDate, true);
        Availability availability = createAvailability(site1, item1, 10);
        Availability availability1 = createAvailability(site2, item1, 20);

        List<Availability> list = this.availabilityRepository.findByItemId(9L);

        assertNotNull(list);
        assertEquals(2, list.size());
        List<Availability> expectedList = new ArrayList<>();
        expectedList.add(availability);
        expectedList.add(availability1);
        assertEquals(list, expectedList);

    }
    @Test
    void TestFindBySiteIdAndItemId() {
        Date currentDate = new Date(2002, 11, 1);
        Item item1 = createItem("31", 12.99);
        Item item2 = createItem("32", 15.99);
        Site site1 = createSite("31", "location", "Open", currentDate, true);
        Site site2 = createSite("32", "location", "Open", currentDate, true);
        Site site3 = createSite("33", "location", "Open", currentDate, true);

        Availability availability = createAvailability(site1, item1, 10);
        Availability availability1 = createAvailability(site2, item1, 20);
        Availability availability2 = createAvailability(site3, item2, 20);

        Availability actualAvailability = this.availabilityRepository.findBySiteIdAndItemId(9L, 8L);

        assertNotNull(actualAvailability);
        assertEquals(availability2, actualAvailability);

    }

    @Test
    void TestFindSitesByItems() {
        Date currentDate = new Date(2002, 11, 1);
        Item item1 = createItem("41", 12.99);
        Item item2 = createItem("42", 15.99);
        Site site1 = createSite("41", "location", "Open", currentDate, true);
        Site site2 = createSite("42", "location", "Open", currentDate, true);
        Availability availability = createAvailability(site1, item1, 10);
        Availability availability1 = createAvailability(site2, item1, 20);
        Availability availability2 = createAvailability(site2, item2, 20);
        List<Item> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        List<Site> siteList = this.availabilityRepository.findSitesByItems(list);

        assertNotNull(siteList);
        assertEquals(2, siteList.size());
        List<Site> expectedList = new ArrayList<>();
        expectedList.add(site1);
        expectedList.add(site2);
        assertEquals(siteList, expectedList);

    }

    @Test
    void TestFindSitesByOneItem() {

        Date currentDate = new Date(2002, 11, 1);
        Item item1 = createItem("41", 12.99);
        Item item2 = createItem("42", 15.99);
        Site site1 = createSite("41", "location", "Open", currentDate, true);
        Site site2 = createSite("42", "location", "Open", currentDate, true);
        Availability availability = createAvailability(site1, item1, 10);
        Availability availability1 = createAvailability(site2, item2, 20);
        List<Site> siteList = this.availabilityRepository.findSitesByOneItem(item1);

        assertNotNull(siteList);
        assertEquals(1, siteList.size());
        List<Site> expectedList = new ArrayList<>();
        expectedList.add(site1);
        assertEquals(siteList, expectedList);

    }

    private Item createItem( String name, double price) {
        Item item = Item.builder()
                .itemName(name)
                .itemPrice(price)
                .build();
        return itemRepository.saveAndFlush(item);
    }

    private Site createSite(String name, String location, String status,
                            Date ceaseData, Boolean internalSite ) {

        Site site = Site.builder()
                .siteName(name)
                .siteLocation(location)
                .siteStatus(status)
                .ceaseDate(ceaseData)
                .internalSite(internalSite).build();

        return siteRepository.saveAndFlush(site);

    }

    private Availability createAvailability(Site site, Item item, Integer quantity) {
        Availability availability = Availability.builder()
                .siteId(site)
                .itemId(item)
                .quantity(quantity)
                .build();

        return availabilityRepository.saveAndFlush(availability);
    }





}
