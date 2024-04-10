package com.depot.ims.repositories;

import com.depot.ims.models.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for the Item entity using the H2 in-memory database.
 * Tests validate the basic CRUD operations provided by ItemRepository.
 */
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class ItemTest {

    @Autowired
    private ItemRepository itemRepository;

    /**
     * Tests retrieval of a list of item entities by item name
     * Validates that the findAll operation can successfully retrieve the correct item entities and
     * number of item entities
     */
    @Test
    void testFindByItemName() {
        // Populate test data in the database
        itemRepository.save(new Item("item 1", 12.99));
        itemRepository.save(new Item("item 2", 15.99));

        // Call the actual method being tested
        List<Item> result = itemRepository.findByItemName("item 2");

        // Assert the result
        assertEquals(1, result.size());
        assertEquals("item 2", result.get(0).getItemName());
    }

    /**
     * Tests retrieval of a list of item entities by item id
     * Validates that the findAll operation can successfully retrieve the correct item entities and
     * number of item entities
     */
    @Test
    void testFindByItemId() {
        // Populate test data in the database
        Item savedItem = itemRepository.save(new Item("item 3", 20.0));

        // Call the actual method being tested
        Item result = itemRepository.findByItemId(savedItem.getItemId());

        // Assert the result
        assertEquals("item 3", result.getItemName());
        assertEquals(20.0, result.getItemPrice());
    }
}
