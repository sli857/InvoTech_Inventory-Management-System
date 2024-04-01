package com.depot.ims.services;

import com.depot.ims.models.Item;
import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.AvailabilitiesRepository;
import com.depot.ims.repositories.ItemRepository;
import com.depot.ims.repositories.ShipRepository;
import com.depot.ims.repositories.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
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
    private AvailabilitiesRepository availabilitiesRepositoryMock = mock(AvailabilitiesRepository.class);

    private final ShipService shipService = new ShipService(shipRepositoryMock,
            shipmentRepositoryMock,
            itemRepositoryMock,
            availabilitiesRepositoryMock);

    @Test
    void getShipmentById_ValidId_ReturnsShipment() {
        // Given
        int shipmentId = 123;
        Shipment expectedShipment = new Shipment();
        given(shipmentRepositoryMock.findByShipmentId(shipmentId)).willReturn(expectedShipment);

        // When
        Shipment actualShipment = shipService.getShipmentById(shipmentId);

        // Then
        assertNotNull(actualShipment);
        assertEquals(expectedShipment, actualShipment);
        verify(shipmentRepositoryMock, times(1)).findByShipmentId(shipmentId);
    }

    @Test
    void getShipmentById_InvalidId_ReturnsNull() {
        // Given
        int shipmentId = 456;
        given(shipmentRepositoryMock.findByShipmentId(shipmentId)).willReturn(null);

        // When
        Shipment actualShipment = shipService.getShipmentById(shipmentId);

        // Then
        assertNull(actualShipment);
        verify(shipmentRepositoryMock, times(1)).findByShipmentId(shipmentId);
    }

    @Test
    void getItemById_ValidId_ReturnsItem() {
        // Given
        int itemId = 789;
        Item expectedItem = new Item();
        given(itemRepositoryMock.findByItemId(itemId)).willReturn(expectedItem);

        // When
        Item actualItem = shipService.getItemById(itemId);

        // Then
        assertNotNull(actualItem);
        assertEquals(expectedItem, actualItem);
        verify(itemRepositoryMock, times(1)).findByItemId(itemId);
    }

    @Test
    void getItemById_InvalidId_ReturnsNull() {
        // Given
        int itemId = 101;
        given(itemRepositoryMock.findByItemId(itemId)).willReturn(null);

        // When
        Item actualItem = shipService.getItemById(itemId);

        // Then
        assertNull(actualItem);
        verify(itemRepositoryMock, times(1)).findByItemId(itemId);
    }
}
