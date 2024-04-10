package com.depot.ims.repositories;

import com.depot.ims.models.Shipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import java.sql.Timestamp;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for Shipment entity using H2 in-memory database.
 * These tests verify the ShipmentRepository's interactions with the database.
 */
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=update"
})
public class ShipmentTest {
    @Autowired
    private ShipmentRepository shipmentRepository;

    /**
     * Sets up test data before each test execution.
     * This method pre-populates the in-memory database with Shipment entities
     * to ensure a consistent
     * starting state for each test.
     */
    @BeforeEach
    void setup() {
        // Preparing test data
        createShipment(2L, // source
                3L, // destination
                "Warehouse A", // currentLocation
                Timestamp.valueOf("2024-04-01 08:00:00"), // departureTime
                Timestamp.valueOf("2024-04-03 08:00:00"), // estimatedArrivalTime
                Timestamp.valueOf("2024-04-03 07:45:00"), // actualArrivalTime
                "Delivered"); // shipmentStatus
        createShipment(5L, // source
                6L, // destination
                "Transit Point B", // currentLocation
                Timestamp.valueOf("2024-04-02 09:00:00"), // departureTime
                Timestamp.valueOf("2024-04-04 09:00:00"), // estimatedArrivalTime
                null, // actualArrivalTime is null if the shipment hasn't arrived yet
                "In Transit"); // shipmentStatus
    }

    /**
     * Tests if all shipments are correctly retrieved from the database.
     * This test verifies the correct retrieval of all Shipment entities, ensuring
     * the findAll method functions as expected.
     */
    @Test
    void findAllTest() {
        List<Shipment> shipments = shipmentRepository.findAll();
        assertNotNull(shipments);
        assertEquals(2, shipments.size());
    }

    /**
     * Tests retrieval of a shipment by its source location ID.
     * This test ensures that the ShipmentRepository can find a Shipment
     * entity based on the source location ID.
     */
    @Test
    void findByShipmentIdTest() {
        Long source = 5L;
        Shipment shipment = shipmentRepository.findBySource(source);
        assertNotNull(shipment);
        assertEquals(source, shipment.getSource());
    }

    /**
     * Helper method to create and persist a Shipment entity for testing.
     *
     * @param newSource The source location ID of the new shipment.
     * @param newDestination The destination location ID of the new shipment.
     * @param newCurrentLocation The current location description of the new shipment.
     * @param newDepartureTime The departure time of the new shipment.
     * @param newEstimatedArrivalTime The estimated arrival time of the new shipment.
     * @param newActualArrivalTime The actual arrival time of the new shipment (can be null).
     * @param newShipmentStatus The status of the new shipment.
     * @return The saved Shipment entity.
     */
    private Shipment createShipment(Long newSource,
                                    Long newDestination,
                                    String newCurrentLocation,
                                    Timestamp newDepartureTime,
                                    Timestamp newEstimatedArrivalTime,
                                    Timestamp newActualArrivalTime,
                                    String newShipmentStatus) {
        Shipment shipment = new Shipment();

        shipment.setSource(newSource);
        shipment.setDestination(newDestination);
        shipment.setCurrentLocation(newCurrentLocation);
        shipment.setDepartureTime(newDepartureTime);
        shipment.setEstimatedArrivalTime(newEstimatedArrivalTime);
        shipment.setActualArrivalTime(newActualArrivalTime);
        shipment.setShipmentStatus(newShipmentStatus);
        return shipmentRepository.saveAndFlush(shipment);
    }
}
