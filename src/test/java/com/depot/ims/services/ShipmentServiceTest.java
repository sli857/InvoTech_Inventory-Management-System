package com.depot.ims.services;

import com.depot.ims.models.Shipment;
import com.depot.ims.models.User;
import com.depot.ims.repositories.ShipmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import java.sql.Timestamp;
import java.util.stream.Stream;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ShipmentService class.
 * These tests mock the ShipmentRepository to isolate the service logic and verify
 * its correctness under various conditions.
 */
public class ShipmentServiceTest {
    @Mock
    private ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
    private final ShipmentService shipmentService = new ShipmentService(shipmentRepository);

    @BeforeEach
    void setup() {
    }

    /**
     * Tests retrieval of a shipment using the shipment service.
     * Verifies correct behavior when a shipment is found and when the input is null.
     */
    @Test
    void testGetShipment() {
        // Setup test data
        Shipment shipment1 = new Shipment(1L, 2L, // source
                3L, // destination
                "Warehouse A", // currentLocation
                Timestamp.valueOf("2024-04-01 08:00:00"), // departureTime
                Timestamp.valueOf("2024-04-03 08:00:00"), // estimatedArrivalTime
                Timestamp.valueOf("2024-04-03 07:45:00"), // actualArrivalTime
                "Delivered");
        Shipment shipment2 = new Shipment(4L, 5L, // source
                6L, // destination
                "Transit Point B", // currentLocation
                Timestamp.valueOf("2024-04-02 09:00:00"), // departureTime
                Timestamp.valueOf("2024-04-04 09:00:00"), // estimatedArrivalTime
                null, // actualArrivalTime is null if the shipment hasn't arrived yet
                "In Transit");

        when(shipmentRepository.findByShipmentId(1L)).thenReturn(shipment1);
        when(shipmentRepository.findBySource(5L)).thenReturn(shipment2);

        // Execute test
        ResponseEntity<?> res1 = shipmentService.getShipment(1L);
        ResponseEntity<?> res2 = shipmentService.getShipment(null);

        // Verify outcomes
        Stream.of(res1, res2).forEach(Assertions::assertNotNull);
        assertEquals(ResponseEntity.ok(shipment1), res1);
        assertTrue(res2.getStatusCode().is4xxClientError());
    }

    /**
     * Tests successful deletion of a shipment.
     * Verifies that the correct response is returned when a shipment is
     * successfully found and deleted.
     */
    @Test
    void testDeleteShipmentFound() {
        Long shipmentId = 1L;
        when(shipmentRepository.existsById(shipmentId)).thenReturn(true);

        ResponseEntity<?> response = shipmentService.deleteShipment(shipmentId);

        assertEquals("Successfully deleted", response.getBody());
        verify(shipmentRepository).deleteById(shipmentId);
    }

    /**
     * Tests deletion of a shipment that does not exist.
     * Verifies that an appropriate message is returned when attempting to
     * delete a non-existent shipment.
     */
    @Test
    void testDeleteShipmentNotFound() {
        Long shipmentId = 10L;
        when(shipmentRepository.existsById(shipmentId)).thenReturn(false);

        ResponseEntity<?> response = shipmentService.deleteShipment(shipmentId);

        assertEquals("Shipment not found by shipment Id", response.getBody());
        verify(shipmentRepository, never()).deleteById(shipmentId);
    }

    /**
     * Tests update operation on a shipment that does not exist.
     * Verifies that an appropriate error message is returned for non-existent shipments.
     */
    @Test
    void testUpdateShipmentNotFound() {
        Long shipmentId = 11L;
        when(shipmentRepository.existsById(shipmentId)).thenReturn(false);

        ResponseEntity<?> response = shipmentService.updateShipment(
                11L, 21L, // source
                31L, // destination
                "Warehouse K", // currentLocation
                Timestamp.valueOf("2024-01-01 08:00:00"), // departureTime
                Timestamp.valueOf("2024-01-03 08:00:00"), // estimatedArrivalTime
                Timestamp.valueOf("2024-01-03 07:45:00"), // actualArrivalTime
                "Delivered");
        assertEquals("Shipment not found by shipment id!", response.getBody());
    }

    /**
     * Tests update operation without specifying any values to update.
     * Verifies that an error message is returned when no update values are specified.
     */
    @Test
    void testUpdateShipmentNoUpdateValuesSpecified() {
        Long shipmentId = 1L;
        when(shipmentRepository.existsById(shipmentId)).thenReturn(true);

        ResponseEntity<?> response = shipmentService.updateShipment(shipmentId,
                null, null, null,
                null, null,
                null, null);
        assertEquals("No value for this update is specified", response.getBody());
    }

    /**
     * Tests successful update of a shipment.
     * Verifies that the shipment is updated correctly when valid update values are specified.
     */
    @Test
    void testUpdateShipmentSuccessful() {
        Long shipmentId = 1L;
        Shipment shipment = new Shipment(shipmentId,12L, // source
                32L, // destination
                "Warehouse Y", // currentLocation
                Timestamp.valueOf("2024-01-01 08:00:00"), // departureTime
                Timestamp.valueOf("2024-01-03 08:00:00"), // estimatedArrivalTime
                Timestamp.valueOf("2024-01-03 07:45:00"), // actualArrivalTime
                "Delivered");
        when(shipmentRepository.existsById(shipmentId)).thenReturn(true);
        when(shipmentRepository.findByShipmentId(shipmentId)).thenReturn(shipment);
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);

        ResponseEntity<?> response = shipmentService.updateShipment(shipmentId,
                66L, 62L, // destination
                "Warehouse O", // currentLocation
                Timestamp.valueOf("2024-01-01 08:00:00"), // departureTime
                Timestamp.valueOf("2024-01-03 08:00:00"), // estimatedArrivalTime
                Timestamp.valueOf("2024-01-03 07:45:00"), // actualArrivalTime
                "Delivered");

        Shipment shipment1 = new Shipment(shipmentId, 66L, 62L, // destination
                "Warehouse O", // currentLocation
                Timestamp.valueOf("2024-01-01 08:00:00"), // departureTime
                Timestamp.valueOf("2024-01-03 08:00:00"), // estimatedArrivalTime
                Timestamp.valueOf("2024-01-03 07:45:00"), // actualArrivalTime
                "Delivered");

        assertEquals(shipment1, response.getBody());
        verify(shipmentRepository).save(shipment);
    }


}