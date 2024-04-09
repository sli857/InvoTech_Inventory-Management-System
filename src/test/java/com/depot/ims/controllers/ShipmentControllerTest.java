package com.depot.ims.controllers;

import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.ShipmentRepository;
import com.depot.ims.services.ShipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for the ShipmentController class.
 * These tests mock the behavior of the ShipmentService and ShipmentRepository to verify
 * the ShipmentController's request mappings, response status codes, and body contents.
 */
public class ShipmentControllerTest {
    @InjectMocks
    ShipmentController shipmentController;
    @Mock
    ShipmentRepository shipmentRepository;
    @Mock
    ShipmentService shipmentService;

    private MockMvc mockMvc;


    /**
     * Sets up the testing environment before each test.
     * Initializes mocks and configures the MockMvc object for testing the controller.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shipmentController).build();
    }

    /**
     * Test to verify the retrieval of all shipments.
     * Checks for the correct status code and the presence of shipment data in the response.
     */
    @Test
    public void testGetShipments() throws Exception {
        // Setup mock shipments
        List<Shipment> shipments = Arrays.asList(
                new Shipment(1L,12L, // source
                        32L, // destination
                        "Warehouse Y", // currentLocation
                        Timestamp.valueOf("2024-01-01 08:00:00"), // departureTime
                        Timestamp.valueOf("2024-01-03 08:00:00"), // estimatedArrivalTime
                        Timestamp.valueOf("2024-01-03 07:45:00"), // actualArrivalTime
                        "Delivered"),
                new Shipment(2L,11L, // source
                        12L, // destination
                        "Warehouse Y", // currentLocation
                        Timestamp.valueOf("2024-01-01 08:00:00"), // departureTime
                        Timestamp.valueOf("2024-01-03 08:00:00"), // estimatedArrivalTime
                        Timestamp.valueOf("2024-01-03 07:45:00"), // actualArrivalTime
                        "Delivered"));
        when(shipmentRepository.findAll()).thenReturn(shipments);

        // Perform the test and verify results
        mockMvc.perform(get("/shipments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].source", org.hamcrest.Matchers.is(12)))
                .andReturn(); // Capture the response
    }

    /**
     * Test to verify the retrieval of a single shipment by its ID.
     * Checks for correct status code and shipment details in the response.
     */
    @Test
    public void testGetShipment() throws Exception {
        // Setup mock shipment
        Shipment shipment = new Shipment(1L,12L, // source
                32L, // destination
                "Warehouse W", // currentLocation
                Timestamp.valueOf("2024-01-01 08:00:00"), // departureTime
                Timestamp.valueOf("2024-01-03 08:00:00"), // estimatedArrivalTime
                Timestamp.valueOf("2024-01-03 07:45:00"), // actualArrivalTime
                "Delivered");

        ResponseEntity<Shipment> res = new ResponseEntity<>(
                shipment,
                null,
                HttpStatus.OK
        );

        doReturn(res).when(shipmentService).getShipment(1L);

        // Perform GET request to /shipment endpoint with shipmentId as request parameter
        mockMvc.perform(get("/shipments/shipment")
                        .param("shipmentId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentId", is(1)))
                .andExpect(jsonPath("$.source", is(12)))
                .andExpect(jsonPath("$.currentLocation", is("Warehouse W")))
                .andExpect(jsonPath("$.shipmentStatus", is("Delivered")))
                .andReturn(); // Capture the response
    }


    /**
     * Test to verify the addition of a new shipment.
     * Checks for correct status code and newly added shipment details in the response.
     */
    @Test
    public void testAddShipment() throws Exception {
        // Setup mock shipment to be added and the expected result
        Shipment shipment = new Shipment(null,12L, // source
                32L, // destination
                "Warehouse Y", // currentLocation
                Timestamp.valueOf("2024-01-01 08:00:00"), // departureTime
                Timestamp.valueOf("2024-01-03 08:00:00"), // estimatedArrivalTime
                Timestamp.valueOf("2024-01-03 07:45:00"), // actualArrivalTime
                "Delivered");
        Shipment savedShipment = new Shipment(2L,11L, // source
                22L, // destination
                "Warehouse W", // currentLocation
                Timestamp.valueOf("2024-02-01 08:00:00"), // departureTime
                Timestamp.valueOf("2024-02-03 08:00:00"), // estimatedArrivalTime
                Timestamp.valueOf("2024-02-03 07:45:00"), // actualArrivalTime
                "Delivered");

        when(shipmentRepository.save(any(Shipment.class))).thenReturn(savedShipment);

        ObjectMapper objectMapper = new ObjectMapper();
        String shipmentJson = objectMapper.writeValueAsString(shipment);

        // Perform the test and verify results
        mockMvc.perform(post("/shipments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shipmentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shipmentId", is(2)))
                .andExpect(jsonPath("$.destination", is(22)))
                .andExpect(jsonPath("$.currentLocation", is("Warehouse W")))
                .andExpect(jsonPath("$.shipmentStatus", is("Delivered")));
    }

    /**
     * Test to verify the update functionality for a shipment.
     * Checks for correct status code and the expected success message in the response.
     */
    @Test
    public void testUpdateShipment() throws Exception {
        Shipment update = new Shipment(1L,12L, // source
                32L, // destination
                "Warehouse Y", // currentLocation
                Timestamp.valueOf("2024-01-01 08:00:00"), // departureTime
                Timestamp.valueOf("2024-01-03 08:00:00"), // estimatedArrivalTime
                Timestamp.valueOf("2024-01-03 07:45:00"), // actualArrivalTime
                "Delivered");

        ObjectMapper objectMapper = new ObjectMapper();
        String shipmentJson = objectMapper.writeValueAsString(update);

        // Mock the service call to return a success response
        doReturn(ResponseEntity.ok("Successfully updated")).when(shipmentService).
                updateShipment(1L, 8L, // source
                        99L, // destination
                        "Warehouse A", // currentLocation
                        Timestamp.valueOf("2024-01-12 08:00:00"), // departureTime
                        Timestamp.valueOf("2024-01-13 08:00:00"), // estimatedArrivalTime
                        Timestamp.valueOf("2024-01-14 07:45:00"), // actualArrivalTime
                        "In Transit");

        // Perform the test and verify results
        mockMvc.perform(post("/shipments/update")
                        .param("shipmentId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(shipmentJson))
                .andExpect(status().isOk());
    }

    /**
     * Test to verify successful deletion of a shipment.
     * Checks for correct status code and the expected success message in the response.
     */
    @Test
    void deleteShipmentFound() throws Exception {
        // Mock the service call to return a success response
        doReturn(ResponseEntity.ok("Successfully deleted"))
                .when(shipmentService).deleteShipment(1L);

        // Perform the test and verify results
        mockMvc.perform(delete("/shipments/delete")
                        .param("shipmentId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));

        // Verify that the shipmentService's delete method was called
        verify(shipmentService, times(1)).deleteShipment(1L);
    }

    /**
     * Test to verify the behavior when attempting to delete a non-existent shipment.
     * Checks for correct status code and error message in the response.
     */
    @Test
    void deleteShipmentNotFound() throws Exception {
        // Mock the service call to return an error response
        doReturn(ResponseEntity.badRequest().body("Shipment not found by shipment Id"))
                .when(shipmentService).deleteShipment(2L);

        // Perform the test and verify results
        mockMvc.perform(delete("/shipments/delete")
                        .param("shipmentId", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Shipment not found by shipment Id"));

        // Verify that the shipmentService's delete method was called
        verify(shipmentService, times(1)).deleteShipment(2L);
    }

}