package com.depot.ims.services;

import com.depot.ims.models.*;
import com.depot.ims.repositories.AvailabilityRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.repositories.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

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
    private final ShipService shipService = new ShipService(shipRepositoryMock,
            shipmentRepositoryMock,
            itemRepositoryMock,
            availabilityRepositoryMock);

    @Test
    void addShipQuantityUpdateTest() {
        // Given
        Item item = new Item(1L, "TestItem", 10.0);
        Site site1 = new Site(1L, "TestSite1", "TestAddress1", "Open", null, true);
        Site site2 = new Site(2L, "TestSite2", "TestAddress2", "Open", null, true);
        Shipment sourceShipment = new Shipment(1L, 1L, 2L, null, null, null, null, null);
        Ship ship = Ship.builder()
                .shipmentId(sourceShipment)
                .itemId(item)
                .quantity(50)
                .build();
        Availability availability1 = new Availability(site1, item, 50);
        Availability availability2 = new Availability(site2, item, 20);

        when(shipmentRepositoryMock.findByShipmentId(1L)).thenReturn(sourceShipment);
        when(shipmentRepositoryMock.findByShipmentId(2L)).thenReturn(sourceShipment);
        when(itemRepositoryMock.findByItemId(1L)).thenReturn(item);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(1L, 1L)).thenReturn(availability1);
        when(availabilityRepositoryMock.findBySiteIdAndItemId(2L, 1L)).thenReturn(availability2);
        when(shipRepositoryMock.save(any(Ship.class))).thenReturn(ship);

        // When
        Ship result = shipService.addShip(ship);

        // Then
        assertNotNull(result);
        assertEquals(0, availability1.getQuantity());
        assertEquals(70, availability2.getQuantity());
    }

}
