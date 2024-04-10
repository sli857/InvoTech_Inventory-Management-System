package com.depot.ims.services;

import com.depot.ims.models.Item;
import com.depot.ims.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ItemService class.
 * Validates the functionality of availability management operations, including retrieval,
 * item details. Utilizes Mockito to mock ItemRepository for isolated testing of service logic.
 */
public class ItemServiceTest {

    @Mock
    private ItemRepository itemsRepository = mock(ItemRepository.class);

    private final ItemService itemsService = new ItemService(itemsRepository);

    /**
     * Tests retrieving an availability
     * mock the item repository
     * Verifies correct return of the getItem method
     */
    @Test
    public void testGetItem() {
        //mock the repository class
        //test the return of controller class

        //GET request
        Item item = new Item(11L, "name", 1.1);
        when(itemsRepository.findByItemId(11L)).thenReturn(item);
        //get the getItem method in the controller
        ResponseEntity<?> response = this.itemsService.getItem(11L, "name");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().equals(item));

    }
    /**
     * Tests adding an item
     * mock the item repository
     * Verifies correct return of the addItem method
     */
    @Test
    public void testAddItem() {
        //test2.1: test the addItem method inside the item with valid item
        Item newItem = new Item(20L, "new", 3.0);
        ResponseEntity<?> response5= this.itemsService.addItem(newItem);
        assertEquals(HttpStatus.OK, response5.getStatusCode());
        assertNotNull(response5.getBody());
        assertEquals( true, response5.getBody().equals(newItem));
    }

    /**
     * Tests updating an item
     * mock the item repository
     * Verifies correct return of the updateItem method
     */
    @Test
    public void testUpdateItem() {

        //test3.1: test the updateItem method inside the item controller with correct inputs
        Item item = new Item(11L, "name", 1.1);
        Item updateItem = new Item(11L, "newName", 2.1);
        when(itemsRepository.findByItemId(11L)).thenReturn(item);
        when(itemsRepository.existsById(11L)).thenReturn(true);
        when(itemsRepository.save(updateItem)).thenReturn(updateItem);
        ResponseEntity<?> response3 = this.itemsService.updateItem(11L, "newName", 2.1);
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNotNull(response3.getBody());
        assertEquals(true, response3.getBody().equals(updateItem));

        //test3.2: test the updateItem method inside the item with invalid inputs
        ResponseEntity<?> response4 = this.itemsService.updateItem(11L, null, null);
        assertEquals(HttpStatus.BAD_REQUEST, response4.getStatusCode());
        assertNotNull(response4.getBody());
        assertEquals( true, response4.getBody().equals("No value for this update is specified."));

    }

}
