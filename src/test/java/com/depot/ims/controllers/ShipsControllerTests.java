package com.depot.ims.controllers;

import com.depot.ims.models.Ship;
import com.depot.ims.repositories.ShipsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class ShipsControllerTests {

    @InjectMocks
    ShipsController shipsController;

    @Mock
    ShipsRepository shipsRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(shipsController).build();
    }

    @Test
    void testGetAllShips() throws Exception {
        Ship ship1 = new Ship();
        Ship ship2 = new Ship();
        List<Ship> ships = Arrays.asList(ship1, ship2);

        when(shipsRepository.findAll()).thenReturn(ships);

        mockMvc.perform(get("/ships")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{},{}]"));
    }

    @Test
    void testGetShipsByItemId() throws Exception {
        Ship ship1 = new Ship();
        List<Ship> ships = List.of(ship1);

        when(shipsRepository.findByItemId(any(Integer.class))).thenReturn(ships);

        mockMvc.perform(get("/ships/item=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));
    }

    @Test
    void testGetShipsByShipmentId() throws Exception {
        Ship ship1 = new Ship();
        List<Ship> ships = List.of(ship1);

        when(shipsRepository.findByShipmentId(any(Integer.class))).thenReturn(ships);

        mockMvc.perform(get("/ships/shipment=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));
    }

    @Test
    void testGetShipsByItemIdAndShipmentId() throws Exception {
        Ship ship1 = new Ship();
        List<Ship> ships = List.of(ship1);

        when(shipsRepository.findByItemIdAndShipmentId(any(Integer.class), any(Integer.class))).thenReturn(ships);

        mockMvc.perform(get("/ships/item=1/shipment=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));
    }

}
