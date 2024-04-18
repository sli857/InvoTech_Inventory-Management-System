package com.depot.ims.repositories;

import com.depot.ims.models.Item;
import com.depot.ims.models.Ship;
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
 * Test class for ShipRepository
 */
@DataJpaTest
// Annotation to specify property sources for the test
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=update"
})
class ShipTest {

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    /**
     * Creation of entities before each test
     */
    @BeforeEach
    void setUp() {
        Item item1 = createItem("item 1", 12.99);
        Item item2 = createItem("item 2", 15.99);

        Shipment shipment1 = createShipment(123, 456, "Warehouse A", System.currentTimeMillis(), 86400000);
        Shipment shipment2 = createShipment(456, 789, "Port B", System.currentTimeMillis(), 172800000);

        createShip(shipment1, item1, 10);
        createShip(shipment2, item2, 20);
    }

    /**
     * Tests the findAll() method of ShipRepository
     *
     * <p>Checks if the method returns all the entities in the database.
     */
    @Test
    void findAllTest() {
        // Given
        // setUp() method

        // When
        List<Ship> res = shipRepository.findAll();

        // Then
        assertNotNull(res);
        assertEquals(2, res.size());
    }

    /**
     * Tests the findByItemId() method of ShipRepository
     *
     * <p>Checks if the method returns the entities with the given item id.
     */
    @Test
    void findByItemIdTest() {
        // Given
        // setUp() method

        // When
        List<Ship> res = shipRepository.findByItemId(3L);

        // Then
        assertNotNull(res);
        assertEquals(1, res.size());
    }

    /**
     * Tests the findByShipmentId() method of ShipRepository
     *
     * <p>Checks if the method returns the entities with the given shipment id.
     */
    @Test
    void findByShipmentIdTest() {
        // Given
        // setUp() method

        // When
        List<Ship> res = shipRepository.findByShipmentId(9L);

        // Then
        assertNotNull(res);
        assertEquals(1, res.size());
    }

    /**
     * Tests the findByItemIdAndShipmentId() method of ShipRepository
     *
     * <p>Checks if the method returns the entities with the given item id and shipment id.
     */
    @Test
    void findByItemIdAndShipmentIdTest() {
        // Given
        // setUp() method

        // When
        List<Ship> res = shipRepository.findByItemIdAndShipmentId(7L, 11L);
        List<Ship> res2 = shipRepository.findByItemIdAndShipmentId(8L, 12L);

        // Then
        assertNotNull(res);
        assertNotNull(res2);
        System.out.println(shipRepository.findAll());
        assertEquals(1, res.size());
        assertEquals(1, res2.size());
    }

    // Helper methods to create entities

    /**
     * Creates a Ship entity
     *
     * @param shipment Shipment entity
     * @param item Item entity
     * @param quantity Quantity of the item
     */
    private void createShip(Shipment shipment, Item item, int quantity) {
        Ship ship = Ship.builder()
                .shipmentId(shipment)
                .itemId(item)
                .quantity(quantity)
                .build();
        shipRepository.saveAndFlush(ship);
    }

    /**
     * Creates an Item entity
     *
     * @param name Name of the item
     * @param price Price of the item
     * @return Item entity
     */
    private Item createItem(String name, double price) {
        Item item = Item.builder()
                .itemName(name)
                .itemPrice(price)
                .build();
        return itemRepository.saveAndFlush(item);
    }

    /**
     * Creates a Shipment entity
     *
     * @param source Source of the shipment
     * @param destination Destination of the shipment
     * @param currentLocation Current location of the shipment
     * @param departureTimeMillis Departure time in milliseconds
     * @param estimatedArrivalTimeMillis Estimated arrival time in milliseconds
     * @return Shipment entity
     */
    private Shipment createShipment(long source, long destination, String currentLocation, long departureTimeMillis, long estimatedArrivalTimeMillis) {
        Shipment shipment = Shipment.builder()
                .source(source)
                .destination(destination)
                .currentLocation(currentLocation)
                .departureTime(new Timestamp(departureTimeMillis))
                .estimatedArrivalTime(new Timestamp(departureTimeMillis + estimatedArrivalTimeMillis))
                .actualArrivalTime(null)
                .shipmentStatus("In Transit")
                .build();
        return shipmentRepository.saveAndFlush(shipment);
    }

}
