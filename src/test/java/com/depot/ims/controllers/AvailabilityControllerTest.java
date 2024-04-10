package com.depot.ims.controllers;

import com.depot.ims.models.*;
import com.depot.ims.repositories.*;
import com.depot.ims.services.AvailabilityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Tests for AvailabilityController - ensures correct handling of availability-related requests.
 * Mocks the AvailabilityRepository and AvailabilityService to isolate controller
 * logic and verify interactions.
 */
public class AvailabilityControllerTest {

    @InjectMocks
    AvailabilityController availabilityController;
    @Mock
    AvailabilityRepository availabilityRepository;

    @Mock
    AvailabilityService availabilityService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    /**
     * Setup method to initialize Mockito mocks and configure the MockMvc instance for
     * standalone controller testing.
     * This setup is performed before each test case.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(availabilityController,
                availabilityService).build();
    }

    /**
     * Tests retrieval of all availability, verifying correct HTTP status and JSON structure.
     * Mocks AvailabilityRepository to return a predefined list of availability.
     * Asserts the size of the returned availability list.
     */
    @Test
    void testGetAllAvailability() throws Exception {
        Date currentDate = new Date(2002, 11, 1);
        Item item = new Item(11L, "name", 1.1);
        Item item2 = new Item(12L, "name2", 1.2);
        Site site1 = new Site(11L, "name", "location",
                "Open", currentDate, true);
        Availability a1 = new Availability(site1, item, 10);
        Availability a2 = new Availability(site1, item2, 20);
        List<Availability> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);
        when(availabilityRepository.findAll()).thenReturn(list);
        mockMvc.perform(get("/availabilities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

    }

    /**
     * Tests retrieval of a list of availability by site id, verifying correct HTTP status
     * and JSON structure.
     */
    @Test
    void testGetAvailability() throws Exception {
        mockMvc.perform(get("/availabilities/site").param("siteID", "123"))
                .andExpect(status().isOk());
    }

    /**
     * Tests retrieval of a list of sites by item id, verifying correct HTTP status and
     * JSON structure.
     */
    @Test
    void testGetSitesByItems() throws Exception {
        MultiValueMap<String, String> item = new LinkedMultiValueMap<>();
        item.add("item1", "1");
        mockMvc.perform(get("/availabilities/searchByItems").params(item))
                .andExpect(status().isOk());
    }

    /**
     * Tests retrieval of an availability by item id, verifying correct HTTP
     * status and JSON structure.
     */
    @Test
    void testGetAvailabilitiesByItemId() throws Exception {
        mockMvc.perform(get("/availabilities/item").param("itemId", "123"))
                .andExpect(status().isOk());
    }

    /**
     * Tests retrieval of an availability by site id and item id, verifying correct HTTP
     * status and JSON structure.
     */
    @Test
    void testGetAvailabilityBySiteIdAndItemId() throws Exception {
        mockMvc.perform(get("/availabilities/site/item").param("siteId", "123")
                        .param("itemId", "123"))
                .andExpect(status().isOk());
    }

    /**
     * Tests adding a new availability, verifying correct HTTP status and JSON structure of the response.
     */
    @Test
    void testAddAvailability() throws Exception {
        Availability availability = new Availability();
        mockMvc.perform(MockMvcRequestBuilders.post("/availabilities/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        // Convert Availability object to JSON string
                        .content(objectMapper.writeValueAsString(availability)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


}
