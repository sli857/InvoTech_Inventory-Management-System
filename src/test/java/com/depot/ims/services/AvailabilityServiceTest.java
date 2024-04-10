package com.depot.ims.services;

import com.depot.ims.models.*;
import com.depot.ims.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the AvailabilityService class.
 * Validates the functionality of availability management operations, including retrieval,
 * and adding of availability details. Utilizes Mockito to mock
 * the siteRepository, ItemRepository, AvailabilityRepository for isolated testing of service logic.
 */
public class AvailabilityServiceTest {

    @Mock
    private SiteRepository siteRepository = mock(SiteRepository.class);
    @Mock
    private ItemRepository itemRepository = mock(ItemRepository.class);
    @Mock
    private AvailabilityRepository availabilityRepository = mock(AvailabilityRepository.class);

    private final AvailabilityService availabilityService = new AvailabilityService(
            siteRepository,
            itemRepository,
            availabilityRepository);
    /**
     * Tests adding an availability
     * mock the availability repository
     * Verifies correct return of the addAvailabilities  method
     */
    @Test
    public void TestAddAvailabilities() {
        Item item = new Item(20L, "item1", 3.0);
        Item item1 = new Item(30L, "item2", 4.0);
        Site site = new Site(11L, "HomeDepot 2", "W54 N53", "open", null,
                true);
        Availability availability = new Availability(site, item, 10);

        when(availabilityRepository.save(availability)).thenReturn(availability);
        ResponseEntity<?> response1 = this.availabilityService.addAvailabilities(availability);
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertNotNull(response1.getBody());
        assertEquals( true, response1.getBody().equals(availability));

    }

    /**
     * Tests retrieving an availability
     * mock the availability repository and site repository
     * Verifies correct return of the addAvailability method
     */
    @Test
    public void TestGetAvailability() {

        Item item = new Item(20L, "item1", 3.0);
        Item item1 = new Item(30L, "item2", 4.0);
        Site site = new Site(11L, "HomeDepot 2", "W54 N53", "open", null,
                true);
        Availability availability = new Availability(site, item, 10);
        Availability availability1 = new Availability(site, item1, 20);
        List<Availability> list = new ArrayList<>();
        list.add(availability);
        list.add(availability1);

        when(this.siteRepository.existsById(11L)).thenReturn(true);
        when(this.availabilityRepository.findBySiteId(11L)).thenReturn(list);
        ResponseEntity<?> response3 = this.availabilityService.getAvailability(11L);
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNotNull(response3.getBody());
        System.out.println(response3.getBody());
        assertEquals( true, response3.getBody().equals(list));

    }

    /**
     * Tests retrieving a list of availability by item id
     * mock the availability repository and item repository
     * Verifies correct return of the getAvailabilitiesByItemId method
     */
    @Test
    public void getAvailabilitiesByItemId() {
        Item item = new Item(20L, "item1", 3.0);
        Item item1 = new Item(30L, "item2", 4.0);
        Site site = new Site(11L, "HomeDepot 2", "W54 N53", "open", null,
                true);
        Site site1 = new Site(12L, "HomeDepot 3", "W54 N53", "open", null,
                true);
        Availability availability = new Availability(site, item, 10);
        Availability availability1 = new Availability(site1, item, 20);
        List<Site> list = new ArrayList<>();
        list.add(site);
        list.add(site1);

        when(this.itemRepository.existsById(20L)).thenReturn(true);
        when(this.itemRepository.findByItemId(20L)).thenReturn(item);
        when(this.availabilityRepository.findSitesByOneItem(item)).thenReturn(list);
        ResponseEntity<?> response4= this.availabilityService.getAvailabilitiesByItemId(20L);
        assertEquals(HttpStatus.OK, response4.getStatusCode());
        assertNotNull(response4.getBody());
        assertEquals( true, response4.getBody().equals(list));

    }

    /**
     * Tests retrieving a list of availability by item id and site id
     * mock the availability repository, item repository, and site repository
     * Verifies correct return of the getAvailabilityBySiteIdAndItemId method
     */
    @Test
    public void TestGetAvailabilityBySiteIdAndItemId() {
        Item item = new Item(20L, "item1", 3.0);
        Item item1 = new Item(30L, "item2", 4.0);
        Site site = new Site(11L, "HomeDepot 2", "W54 N53", "open", null,
                true);
        Site site1 = new Site(12L, "HomeDepot 3", "W54 N53", "open", null,
                true);
        Availability availability = new Availability(site, item, 10);
        Availability availability1 = new Availability(site1, item, 20);
        //mock itemRepository.existsById(itemId)
        when(this.itemRepository.existsById(20L)).thenReturn(true);
        when(this.siteRepository.existsById(11L)).thenReturn(true);
        when(this.availabilityRepository.findBySiteIdAndItemId(11L, 20L)).thenReturn(availability);
        ResponseEntity<?> response5= this.availabilityService.getAvailabilityBySiteIdAndItemId(11L, 20L);
        assertEquals(HttpStatus.OK, response5.getStatusCode());
        assertNotNull(response5.getBody());
        assertEquals( true, response5.getBody().equals(availability));

    }

    /**
     * Tests retrieving a list of sites by a list of items
     * mock the availability repository, item repository, and site repository
     * Verifies correct return of the getSitesByItems method
     */
    @Test
    public void getSitesByItems() {
        MultiValueMap<String, String> item = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> items = new LinkedMultiValueMap<>();

        Item item1 = new Item(1L, "item1", 3.0);
        Item item2 = new Item(2L, "item2", 4.0);
        Item item3 = new Item(3L, "item3", 1.0);
        Item item4 = new Item(4L, "item4", 1.0);

        Site site = new Site(11L, "HomeDepot 2", "W54 N53", "open", null,
                true);
        Site site1 = new Site(12L, "HomeDepot 2", "W54 N53", "open", null,
                true);
        Site site2 = new Site(13L, "HomeDepot 2", "W54 N53", "open", null,
                true);
        Site site3 = new Site(14L, "HomeDepot 2", "W54 N53", "open", null,
                true);

        item.add("item1", "1");
        items.add("item2", "2");
        items.add("item3", "3");

        Availability availability = new Availability(site, item1, 10);
        Availability availability1 = new Availability(site1, item1, 10);
        Availability availability2 = new Availability(site2, item1, 10);
        Availability availability3 = new Availability(site2, item2, 10);
        Availability availability4 = new Availability(site2, item3, 10);

        List<Site> list1 = new ArrayList<>();
        list1.add(site);
        list1.add(site1);
        list1.add(site2);

        List<Site> list2 = new ArrayList<>();
        list2.add(site2);

        List<Site> list3 = new ArrayList<>();
        list3.add(site2);

        when(this.itemRepository.findByItemId(1L)).thenReturn(item1);
        when(this.itemRepository.findByItemId(2L)).thenReturn(item2);
        when(this.itemRepository.findByItemId(3L)).thenReturn(item3);

        when(this.availabilityRepository.findSitesByOneItem(item1)).thenReturn(list1);
        when(this.availabilityRepository.findSitesByOneItem(item2)).thenReturn(list2);
        when(this.availabilityRepository.findSitesByOneItem(item3)).thenReturn(list3);

        //Test1: when has multiple items
        List<Site> expectedList = new ArrayList<>();
        expectedList.add(site2);
        ResponseEntity<?> response6 = this.availabilityService.getSitesByItems(items);
        assertEquals(HttpStatus.OK, response6.getStatusCode());
        assertNotNull(response6.getBody());
        assertEquals( true, response6.getBody().equals(expectedList));


        //Test2: when only has one item
        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        List<Site> siteList = new ArrayList<>();
        siteList.add(site);

        when(this.availabilityRepository.findSitesByItems(itemList)).thenReturn(siteList);

        ResponseEntity<?> response7 = this.availabilityService.getSitesByItems(item);
        assertEquals(HttpStatus.OK, response7.getStatusCode());
        assertNotNull(response7.getBody());
        assertEquals( true, response7.getBody().equals(siteList));

    }

}
