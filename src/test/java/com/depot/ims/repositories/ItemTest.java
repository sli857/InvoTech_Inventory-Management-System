package com.depot.ims.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import com.depot.ims.models.Item;
import org.springframework.test.context.TestPropertySource;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
// Annotation to specify property sources for the test
@TestPropertySource(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=update"
})
public class ItemTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void  TestFindByItemName() {
        Item item1 = createItem("item 1", 12.99);
        Item item2 = createItem("item 2", 15.99);
        Item item3 = createItem("item 2", 15.99);

        List<Item> list = this.itemRepository.findByItemName("item 2");
        List<Item> expectedList = new ArrayList<>();
        expectedList.add(item2);
        expectedList.add(item3);

        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(list, expectedList);
    }

    @Test
    void TestFindByItemId() {

        Item item1 = createItem("item 1", 12.99);
        Item item2 = createItem("item 2", 15.99);
        Item item3 = createItem("item 2", 15.99);
        Item item = this.itemRepository.findByItemId(4L);

        assertNotNull(item);
        assertEquals(item, item1);

    }

    private Item createItem( String name, double price) {
        Item item = Item.builder()
                .itemName(name)
                .itemPrice(price)
                .build();
        return itemRepository.saveAndFlush(item);
    }





}
