package com.depot.ims.controllers;

import com.depot.ims.models.Ship;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.requests.ShipRequest;
import com.depot.ims.services.ShipService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// For more information, please refer to the documentation in the README under Testing Documentation
class ShipControllerTest {
    @InjectMocks
    ShipController shipController;
    @Mock
    ShipRepository shipRepository;
    @Mock
    ShipService shipService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shipController).build();
    }

    @Test
    void testGetAllShips() throws Exception {
        // Given
        List<Ship> ships = new ArrayList<>();
        ships.add(new Ship());
        ships.add(new Ship());

        // When
        when(shipRepository.findAll()).thenReturn(ships);

        // Then
        mockMvc.perform(get("/ships"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetShipsByItemId() throws Exception {
        // Given
        int itemId = 1;
        List<Ship> ships = new ArrayList<>();
        ships.add(new Ship());

        // When
        when(shipRepository.findByItemId(any())).thenReturn(ships);

        // Then
        mockMvc.perform(get("/ships/item=" + itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetShipsByShipmentId() throws Exception {
        // Given
        int shipmentId = 1;
        List<Ship> ships = new ArrayList<>();
        ships.add(new Ship());

        // When
        when(shipRepository.findByShipmentId(any())).thenReturn(ships);

        // Then
        mockMvc.perform(get("/ships/shipment=" + shipmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetShipsByItemIdAndShipmentId() throws Exception {
        // Given
        int itemId = 1;
        int shipmentId = 1;
        List<Ship> ships = new ArrayList<>();
        ships.add(new Ship());

        // When
        when(shipRepository.findByItemIdAndShipmentId(any(), any())).thenReturn(ships);

        // Then
        mockMvc.perform(get("/ships/item=" + itemId + "/shipment=" + shipmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testAddShip() throws Exception {
        // Given
        ShipRequest shipRequest = new ShipRequest();
        Ship ship = new Ship();
        ResponseEntity<?> res = new ResponseEntity<>(
                ship,
                HttpStatus.OK
        );

        // When
        doReturn(res).when(shipService).addShip(any(ShipRequest.class));

        // Then
        mockMvc.perform(post("/ships/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(shipRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(ship));
    }

}
