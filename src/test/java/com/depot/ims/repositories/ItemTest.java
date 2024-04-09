package com.depot.ims.repositories;

import com.depot.ims.models.Site;
import org.junit.jupiter.api.BeforeEach;
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

//    @BeforeEach
//    void setup() {
//        Item item1 = createItem("item 1", 12.99);
//        Item item2 = createItem("item 2", 15.99);
//        Item item3 = createItem("item 3", 15.99);
//    }
//
//    @Test
//    void  TestFindByItemName() {
//        List<Item> list = this.itemRepository.findByItemName("item 2");
////        assertNotNull(list);
////        assertEquals(2, list.size());
//    }

//    @Test
//    void TestFindByItemId() {
//        System.out.println(this.itemRepository.findAll());
//        Item item = this.itemRepository.findByItemId(5L);
////        assertNotNull(item);
//
//    }

    private Item createItem( String name, double price) {
        Item item = Item.builder()
                .itemName(name)
                .itemPrice(price)
                .build();
        return itemRepository.saveAndFlush(item);
    }





}
