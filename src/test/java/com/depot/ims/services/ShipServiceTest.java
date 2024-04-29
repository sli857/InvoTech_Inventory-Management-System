package com.depot.ims.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.depot.ims.models.*;
import com.depot.ims.repositories.AvailabilityRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.repositories.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** This class tests the ShipService class */
class ShipServiceTest {

    @Mock private ShipmentRepository shipmentRepositoryMock = mock(ShipmentRepository.class);
    @Mock private ItemRepository itemRepositoryMock = mock(ItemRepository.class);
    @Mock private ShipRepository shipRepositoryMock = mock(ShipRepository.class);

    @Mock
    private AvailabilityRepository availabilityRepositoryMock = mock(AvailabilityRepository.class);

    @Mock private AuditService auditServiceMock = mock(AuditService.class);
    private final ShipService shipService =
            new ShipService(
                    shipRepositoryMock,
                    shipmentRepositoryMock,
                    itemRepositoryMock,
                    availabilityRepositoryMock,
                    auditServiceMock);

    /**
     * This test checks if the addShip method works as expected.
     *
     * <p>It works by mock sending a shipment of 50 items from site1 to site2. Their availability is
     * 50 and 20 respectively.
     *
     * <p>After the shipment, the availability of site1 should be 0 and site2 should be 70. The result
     * should be an OK status code.
     */
    @Test
    void addShipQuantityUpdateTest() {
        // Given
        Item item = new Item(1L, "TestItem", 10.0);
        Site site1 = new Site(1L, "TestSite1", "TestAddress1", "Open", null, true);
        Site site2 = new Site(2L, "TestSite2", "TestAddress2", "Open", null, true);
        Shipment sourceShipment = new Shipment(1L, 1L, 2L, null, null, null, null, null);
        Ship ship = Ship.builder().shipmentId(sourceShipment).itemId(item).quantity(50).build();
        Availability availability1 = new Availability(site1, item, 50);
        Availability availability2 = new Availability(site2, item, 20);

        when(shipmentRepositoryMock.findByShipmentId(1L)).thenReturn(sourceShipment);
        when(shipmentRepositoryMock.findByShipmentId(2L)).thenReturn(sourceShipment);
        when(itemRepositoryMock.findByItemId(1L)).thenReturn(item);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(1L, 1L)).thenReturn(availability1);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(2L, 1L)).thenReturn(availability2);
        when(shipRepositoryMock.save(any(Ship.class))).thenReturn(ship);

        // When
        ResponseEntity<?> result = shipService.addShip(ship);

        // Then
        assertNotNull(result);
        assertEquals(0, availability1.getQuantity());
        assertEquals(70, availability2.getQuantity());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * This tests the addShip method with a shipment that does not exist.
     *
     * <p>It works by returning null when querying for that requested shipment. The result should be a
     * bad request status code with the following body:
     *
     * <p>"Invalid shipment, source, destination, or item"
     */
    @Test
    void addShipInvalidShipmentTest() {
        // Given
        Item item = new Item(1L, "TestItem", 10.0);
        Shipment sourceShipment = new Shipment(1L, 1L, 2L, null, null, null, null, null);
        Ship ship = Ship.builder().shipmentId(sourceShipment).itemId(item).quantity(50).build();

        when(shipmentRepositoryMock.findByShipmentId(1L)).thenReturn(null);
        when(shipmentRepositoryMock.findByShipmentId(2L)).thenReturn(null);
        when(itemRepositoryMock.findByItemId(1L)).thenReturn(item);

        // When
        ResponseEntity<?> result = shipService.addShip(ship);

        // Then
        assertNotNull(result);
        assertEquals("Invalid shipment, source, destination, or item", result.getBody());
    }

    /**
     * This tests the addShip method with an item that does not exist.
     *
     * <p>It works by returning null when querying for that requested item. The result should be a bad
     * request status code with the following body:
     *
     * <p>"Invalid shipment, source, destination, or item"
     */
    @Test
    void addShipInvalidItemTest() {
        // Given
        Item item = new Item(1L, "TestItem", 10.0);
        Site site1 = new Site(1L, "TestSite1", "TestAddress1", "Open", null, true);
        Site site2 = new Site(2L, "TestSite2", "TestAddress2", "Open", null, true);
        Shipment sourceShipment = new Shipment(1L, 1L, 2L, null, null, null, null, null);
        Ship ship = Ship.builder().shipmentId(sourceShipment).itemId(item).quantity(50).build();
        Availability availability1 = new Availability(site1, item, 50);
        Availability availability2 = new Availability(site2, item, 20);

        when(shipmentRepositoryMock.findByShipmentId(1L)).thenReturn(sourceShipment);
        when(shipmentRepositoryMock.findByShipmentId(2L)).thenReturn(sourceShipment);
        when(itemRepositoryMock.findByItemId(1L)).thenReturn(null);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(1L, 1L)).thenReturn(availability1);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(2L, 1L)).thenReturn(availability2);

        // When
        ResponseEntity<?> result = shipService.addShip(ship);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid shipment, source, destination, or item", result.getBody());
    }

    /**
     * This tests the addShip method by attempting to ship a quantity of item greater than the source
     * site's availability.
     *
     * <p>It works by specifying a quantity of 50 items to be shipped from site1 to site2. Their
     * availability is 20 and 20 respectively.
     *
     * <p>The result should be a bad request status code with the following body:
     *
     * <p>"Not enough quantity in the source site"
     */
    @Test
    void addShipInsufficientQuantityTest() {
        // Given
        Item item = new Item(1L, "TestItem", 10.0);
        Site site1 = new Site(1L, "TestSite1", "TestAddress1", "Open", null, true);
        Site site2 = new Site(2L, "TestSite2", "TestAddress2", "Open", null, true);
        Shipment sourceShipment = new Shipment(1L, 1L, 2L, null, null, null, null, null);
        Ship ship = Ship.builder().shipmentId(sourceShipment).itemId(item).quantity(50).build();
        Availability availability1 = new Availability(site1, item, 20);
        Availability availability2 = new Availability(site2, item, 20);

        when(shipmentRepositoryMock.findByShipmentId(1L)).thenReturn(sourceShipment);
        when(shipmentRepositoryMock.findByShipmentId(2L)).thenReturn(sourceShipment);
        when(itemRepositoryMock.findByItemId(1L)).thenReturn(item);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(1L, 1L)).thenReturn(availability1);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(2L, 1L)).thenReturn(availability2);

        // When
        ResponseEntity<?> result = shipService.addShip(ship);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Not enough quantity in the source site", result.getBody());
    }

    /**
     * This tests the addShip method with a null ship.
     *
     * <p>The result should be a bad request status code with the following body:
     *
     * <p>"Invalid ship"
     */
    @Test
    void addShipNullTest() {
        // Given
        Ship ship = null;

        // When
        ResponseEntity<?> result = shipService.addShip(ship);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid ship", result.getBody());
    }
}
