package com.depot.ims.models;

import com.depot.ims.repositories.ItemsRepository;
import com.depot.ims.repositories.ShipmentsRepository;
import com.depot.ims.repositories.ShipsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
// Annotation to specify property sources for the test
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=update"
})
class ShipTest {

    // Autowiring repositories for dependency injection
    @Autowired
    private ShipsRepository shipsRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private ShipmentsRepository shipmentsRepository;

    private Item item1;
    private Item item2;
    private Shipment shipment1;
    private Shipment shipment2;
    private Shipment shipment3;

    @BeforeEach
    void setUp() {
        item1 = createItem("item 1", 12.99);
        item2 = createItem("item 2", 15.99);

        shipment1 = createShipment(123, 456, "Warehouse A", System.currentTimeMillis(), 86400000);
        shipment2 = createShipment(456, 789, "Port B", System.currentTimeMillis(), 172800000);
        shipment3 = createShipment(789, 123, "Airport C", System.currentTimeMillis(), 259200000);
    }

    @Test
    void findAllTest() {
        // Given
        createShip(shipment1, item1, 10);
        createShip(shipment2, item2, 20);
        createShip(shipment3, item1, 30);

        // When
        List<Ship> res = shipsRepository.findAll();

        // Then
        assertNotNull(res);
        assertEquals(3, res.size());
    }

    @Test
    void findByItemIdTest() {
        // Given
        createShip(shipment1, item1, 10);
        createShip(shipment2, item2, 20);
        createShip(shipment3, item1, 30);

        // When
        List<Ship> res = shipsRepository.findByItemId(item1);

        // Then
        assertNotNull(res);
        assertEquals(2, res.size());
    }

    @Test
    void findByShipmentIdTest() {
        // Given
        createShip(shipment1, item1, 10);
        createShip(shipment2, item2, 20);
        createShip(shipment3, item1, 30);

        // When
        List<Ship> res = shipsRepository.findByShipmentId(shipment1);
        List<Ship> res2 = shipsRepository.findByShipmentId(shipment2);

        // Then
        assertNotNull(res);
        assertNotNull(res2);
        assertEquals(1, res.size());
        assertEquals(1, res2.size());
    }

    @Test
    void findByItemIdAndShipmentIdTest() {
        // Given
        createShip(shipment1, item1, 10);
        createShip(shipment2, item2, 20);
        createShip(shipment3, item1, 30);

        // When
        List<Ship> res = shipsRepository.findByItemIdAndShipmentId(item1, shipment1);
        List<Ship> res2 = shipsRepository.findByItemIdAndShipmentId(item2, shipment2);

        // Then
        assertNotNull(res);
        assertNotNull(res2);
        assertEquals(1, res.size());
        assertEquals(1, res2.size());
    }

    // Helper methods to create entities

    private void createShip(Shipment shipment, Item item, int quantity) {
        Ship ship = Ship.builder()
                .shipmentId(shipment)
                .itemId(item)
                .quantity(quantity)
                .build();
        shipsRepository.saveAndFlush(ship);
    }

    private Item createItem(String name, double price) {
        Item item = Item.builder()
                .itemName(name)
                .itemPrice(price)
                .build();
        return itemsRepository.saveAndFlush(item);
    }

    private Shipment createShipment(int source, int destination, String currentLocation, long departureTimeMillis, long estimatedArrivalTimeMillis) {
        Shipment shipment = Shipment.builder()
                .source(source)
                .destination(destination)
                .currentLocation(currentLocation)
                .departureTime(new Timestamp(departureTimeMillis))
                .estimatedArrivalTime(new Timestamp(departureTimeMillis + estimatedArrivalTimeMillis))
                .actualArrivalTime(null)
                .shipmentStatus("In Transit")
                .build();
        return shipmentsRepository.saveAndFlush(shipment);
    }

}
