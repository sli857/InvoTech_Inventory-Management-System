package com.depot.ims.services;

import com.depot.ims.models.*;
import com.depot.ims.repositories.*;
import com.depot.ims.requests.ShipRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

// For more information, please refer to the documentation in the README under Testing Documentation
class ShipServiceTest {

    @Mock
    private ShipmentRepository shipmentRepositoryMock = mock(ShipmentRepository.class);
    @Mock
    private ItemRepository itemRepositoryMock = mock(ItemRepository.class);
    @Mock
    private ShipRepository shipRepositoryMock = mock(ShipRepository.class);
    @Mock
    private AvailabilityRepository availabilityRepositoryMock = mock(AvailabilityRepository.class);
    @Mock
    private SiteRepository siteRepositoryMock = mock(SiteRepository.class);
    private final ShipService shipService = new ShipService(
            shipRepositoryMock,
            shipmentRepositoryMock,
            itemRepositoryMock,
            availabilityRepositoryMock,
            siteRepositoryMock
    );

    // TODO fix tests based on new check if item exists in the source site and the new siteRepository use

    @Test
    void addShipQuantityUpdateTest() {
        // Given
        Item item = new Item(1L, "TestItem", 10.0);
        Site site1 = new Site(1L, "TestSite1", "TestAddress1", "Open", null, true);
        Site site2 = new Site(2L, "TestSite2", "TestAddress2", "Open", null, true);
        Shipment shipment = new Shipment(1L, 1L, 2L, null, null, null, null, null);
        Ship ship = Ship.builder()
                .shipmentId(shipment)
                .itemId(item)
                .quantity(50)
                .build();
        ShipRequest shipRequest = ShipRequest.builder()
                .shipmentId(1L)
                .itemId(1L)
                .quantity(50)
                .build();
        Availability availability1 = new Availability(site1, item, 50);
        Availability availability2 = new Availability(site2, item, 20);

        when(shipmentRepositoryMock.findByShipmentId(1L)).thenReturn(shipment);
        when(itemRepositoryMock.findByItemId(1L)).thenReturn(item);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(1L, 1L)).thenReturn(availability1);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(2L, 1L)).thenReturn(availability2);
        when(shipRepositoryMock.save(any(Ship.class))).thenReturn(ship);

        // When
        ResponseEntity<?> result = shipService.addShip(shipRequest);

        // Then
        assertNotNull(result);
        assertEquals(0, availability1.getQuantity());
        assertEquals(70, availability2.getQuantity());
    }

    @Test
    void addShipInvalidShipmentTest() {
        // Given
        Item item = new Item(1L, "TestItem", 10.0);
        Shipment shipment = new Shipment(1L, 1L, 2L, null, null, null, null, null);
        Ship ship = Ship.builder()
                .shipmentId(shipment)
                .itemId(item)
                .quantity(50)
                .build();
        ShipRequest shipRequest = ShipRequest.builder()
                .shipmentId(1L)
                .itemId(1L)
                .quantity(50)
                .build();
        when(shipmentRepositoryMock.findByShipmentId(1L)).thenReturn(null);
        when(itemRepositoryMock.findByItemId(1L)).thenReturn(item);

        // When
        ResponseEntity<?> result = shipService.addShip(shipRequest);

        // Then
        assertNotNull(result);
        assertEquals(String.format("Shipment or item do not exist%nShipment: %s%nItem: %s", null, item), result.getBody());
    }

    @Test
    void addShipInvalidItemTest() {
        // Given
        Item item = new Item(1L, "TestItem", 10.0);
        Site site1 = new Site(1L, "TestSite1", "TestAddress1", "Open", null, true);
        Site site2 = new Site(2L, "TestSite2", "TestAddress2", "Open", null, true);
        Shipment shipment = new Shipment(1L, 1L, 2L, null, null, null, null, null);
        Ship ship = Ship.builder()
                .shipmentId(shipment)
                .itemId(item)
                .quantity(50)
                .build();
        ShipRequest shipRequest = ShipRequest.builder()
                .shipmentId(1L)
                .itemId(1L)
                .quantity(50)
                .build();
        Availability availability1 = new Availability(site1, item, 50);
        Availability availability2 = new Availability(site2, item, 20);

        when(shipmentRepositoryMock.findByShipmentId(1L)).thenReturn(shipment);
        when(itemRepositoryMock.findByItemId(1L)).thenReturn(null);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(1L, 1L)).thenReturn(availability1);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(2L, 1L)).thenReturn(availability2);

        // When
        ResponseEntity<?> result = shipService.addShip(shipRequest);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(String.format("Shipment or item do not exist%nShipment: %s%nItem: %s", shipment, null), result.getBody());
    }

    @Test
    void addShipInsufficientQuantityTest() {
        // Given
        Item item = new Item(1L, "TestItem", 10.0);
        Site site1 = new Site(1L, "TestSite1", "TestAddress1", "Open", null, true);
        Site site2 = new Site(2L, "TestSite2", "TestAddress2", "Open", null, true);
        Shipment shipment = new Shipment(1L, 1L, 2L, null, null, null, null, null);
        Ship ship = Ship.builder()
                .shipmentId(shipment)
                .itemId(item)
                .quantity(50)
                .build();
        ShipRequest shipRequest = ShipRequest.builder()
                .shipmentId(1L)
                .itemId(1L)
                .quantity(50)
                .build();
        Availability availability1 = new Availability(site1, item, 20);
        Availability availability2 = new Availability(site2, item, 20);

        when(shipmentRepositoryMock.findByShipmentId(1L)).thenReturn(shipment);
        when(itemRepositoryMock.findByItemId(1L)).thenReturn(item);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(1L, 1L)).thenReturn(availability1);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(2L, 1L)).thenReturn(availability2);

        // When
        ResponseEntity<?> result = shipService.addShip(shipRequest);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(String.format("Not enough quantity for %d in the source site", shipRequest.getQuantity()), result.getBody());
    }

    @Test
    void addShipNullTest() {
        // Given
        ShipRequest shipRequest = null;

        // When
        ResponseEntity<?> result = shipService.addShip(shipRequest);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid shipRequest", result.getBody());
    }

}
