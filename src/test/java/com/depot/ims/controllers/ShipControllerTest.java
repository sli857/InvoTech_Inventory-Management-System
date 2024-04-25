package com.depot.ims.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.depot.ims.models.Ship;
import com.depot.ims.repositories.ShipRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** This class tests the ShipController class. */
class ShipControllerTest {
  @InjectMocks ShipController shipController;
  @Mock ShipRepository shipRepository;

  private MockMvc mockMvc;

  /**
   * Prepares the test environment by initializing any mocks and creating a MockMvc instance for
   * testing the shipController in isolation
   */
  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(shipController).build();
  }

  /**
   * Test method to verify the functionality of retrieving all ships.
   *
   * <p>This test ensures that the controller's endpoint for retrieving all ships ("/ships") behaves
   * as expected. It sets up a mock repository to return a list of ships, and then performs an HTTP
   * GET request to the endpoint. The test verifies that the response status is OK (200), the
   * response body is an array, and the length of the array matches the number of ships returned by
   * the mock repository.
   *
   * @throws Exception if an error occurs during the test execution
   */
  @Test
  void testGetAllShips() throws Exception {
    // Given
    List<Ship> ships = new ArrayList<>();
    ships.add(new Ship());
    ships.add(new Ship());

    // When
    when(shipRepository.findAll()).thenReturn(ships);

    // Then
    mockMvc
        .perform(get("/ships"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(2));
  }

  /**
   * Test method to verify the functionality of retrieving ships by item ID.
   *
   * <p>This test ensures that the controller's endpoint for retrieving ships by item ID
   * ("/ships/item={itemId}") behaves as expected. It sets up a mock repository to return a list of
   * ships for a given item ID, and then performs an HTTP GET request to the endpoint. The test
   * verifies that the response status is OK (200), the response body is an array, and the length of
   * the array matches the number of ships returned by the mock repository.
   *
   * @throws Exception if an error occurs during the test execution
   */
  @Test
  void testGetShipsByItemId() throws Exception {
    // Given
    int itemId = 1;
    List<Ship> ships = new ArrayList<>();
    ships.add(new Ship());

    // When
    when(shipRepository.findByItemId(any())).thenReturn(ships);

    // Then
    mockMvc
        .perform(get("/ships/item=" + itemId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1));
  }

  /**
   * Test method to verify the functionality of retrieving ships by shipment ID.
   *
   * <p>This test ensures that the controller's endpoint for retrieving ships by shipment ID
   * ("/ships/shipment={shipmentId}") behaves as expected. It sets up a mock repository to return a
   * list of ships for a given shipment ID, and then performs an HTTP GET request to the endpoint.
   * The test verifies that the response status is OK (200), the response body is an array, and the
   * length of the array matches the number of ships returned by the mock repository.
   *
   * @throws Exception if an error occurs during the test execution
   */
  @Test
  void testGetShipsByShipmentId() throws Exception {
    // Given
    int shipmentId = 1;
    List<Ship> ships = new ArrayList<>();
    ships.add(new Ship());

    // When
    when(shipRepository.findByShipmentId(any())).thenReturn(ships);

    // Then
    mockMvc
        .perform(get("/ships/shipment=" + shipmentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1));
  }

  /**
   * Test method to verify the functionality of retrieving ships by item ID and shipment ID.
   *
   * <p>This test ensures that the controller's endpoint for retrieving ships by both item ID and
   * shipment ID ("/ships/item={itemId}/shipment={shipmentId}") behaves as expected. It sets up a
   * mock repository to return a list of ships for given item and shipment IDs, and then performs an
   * HTTP GET request to the endpoint. The test verifies that the response status is OK (200), the
   * response body is an array, and the length of the array matches the number of ships returned by
   * the mock repository.
   *
   * @throws Exception if an error occurs during the test execution
   */
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
    mockMvc
        .perform(get("/ships/item=" + itemId + "/shipment=" + shipmentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1));
  }
}
