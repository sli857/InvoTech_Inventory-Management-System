package com.depot.ims.services;

import com.depot.ims.models.Item;
import com.depot.ims.models.Shipment;
import com.depot.ims.repositories.ItemsRepository;
import com.depot.ims.repositories.ShipmentsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

// For more information, please refer to the documentation in the README under Testing Documentation
class ShipServiceTest {

    @Mock
    private ShipmentsRepository shipmentsRepositoryMock = mock(ShipmentsRepository.class);

    @Mock
    private ItemsRepository itemsRepositoryMock = mock(ItemsRepository.class);

    private final ShipService shipService = new ShipService(shipmentsRepositoryMock, itemsRepositoryMock);

    @Test
    void getShipmentById_ValidId_ReturnsShipment() {
        // Given
        int shipmentId = 123;
        Shipment expectedShipment = new Shipment();
        given(shipmentsRepositoryMock.findByShipmentId(shipmentId)).willReturn(expectedShipment);

        // When
        Shipment actualShipment = shipService.getShipmentById(shipmentId);

        // Then
        assertNotNull(actualShipment);
        assertEquals(expectedShipment, actualShipment);
        verify(shipmentsRepositoryMock, times(1)).findByShipmentId(shipmentId);
    }

    @Test
    void getShipmentById_InvalidId_ReturnsNull() {
        // Given
        int shipmentId = 456;
        given(shipmentsRepositoryMock.findByShipmentId(shipmentId)).willReturn(null);

        // When
        Shipment actualShipment = shipService.getShipmentById(shipmentId);

        // Then
        assertNull(actualShipment);
        verify(shipmentsRepositoryMock, times(1)).findByShipmentId(shipmentId);
    }

    @Test
    void getItemById_ValidId_ReturnsItem() {
        // Given
        int itemId = 789;
        Item expectedItem = new Item();
        given(itemsRepositoryMock.findByItemId(itemId)).willReturn(expectedItem);

        // When
        Item actualItem = shipService.getItemById(itemId);

        // Then
        assertNotNull(actualItem);
        assertEquals(expectedItem, actualItem);
        verify(itemsRepositoryMock, times(1)).findByItemId(itemId);
    }

    @Test
    void getItemById_InvalidId_ReturnsNull() {
        // Given
        int itemId = 101;
        given(itemsRepositoryMock.findByItemId(itemId)).willReturn(null);

        // When
        Item actualItem = shipService.getItemById(itemId);

        // Then
        assertNull(actualItem);
        verify(itemsRepositoryMock, times(1)).findByItemId(itemId);
    }
}
