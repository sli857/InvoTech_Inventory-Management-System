package com.depot.ims.controllers;

import com.depot.ims.services.*;
import com.depot.ims.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.depot.ims.models.Item;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;

public class ItemControllerTest {

    @InjectMocks
    ItemController itemController;
    @Mock
    ItemRepository itemRepository;

    @Mock
    ItemService itemService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController, itemService).build();
    }

    @Test
    void testGetAllItems() throws Exception {

        Item item = new Item(11L, "name", 1.1);
        Item item2 = new Item(12L, "name2", 1.2);
        List<Item> list = new ArrayList<>();
        list.add(item);
        list.add(item2);

        // When
        when(itemRepository.findAll()).thenReturn(list);

        // Then
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value(item))
                .andExpect(jsonPath("$[1]").value(item2))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void addItem() throws Exception {
        Item item = new Item(2L, "name", 1.1);
        String jsonBody = "{"
                + "\"itemId\": 2,"
                + "\"itemName\": \"name\","
                + "\"itemPrice\": 1.1"
                + "}";
        mockMvc.perform(post("/items/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    void getItem() throws Exception{
        mockMvc.perform(get("/items/item")
                        .param("itemId", "123")  // Example item ID
                        .param("itemName", "exampleItemName")) // Example item name
                .andExpect(status().isOk());
    }

    @Test
    void updateItem() throws Exception{
        mockMvc.perform(post("/items/update")
                        .param("itemId", "123")  // Example item ID
                        .param("itemName", "newItemName") // Example new item name
                        .param("itemPrice", "10.99")) // Example new item price
                .andExpect(status().isOk());

    }


}
