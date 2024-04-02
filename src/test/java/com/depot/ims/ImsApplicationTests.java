package com.depot.ims;

import com.depot.ims.controllers.ItemsController;
import com.depot.ims.controllers.SitesController;
import com.depot.ims.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Site;
import com.depot.ims.models.Item;
import com.depot.ims.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class ImsApplicationTests {

	@Mock
	private ItemsRepository itemsRepository;
	@InjectMocks
	private ItemsController itemsController;

	@Autowired
	private MockMvc mockMvc;

	//use Junit test to test the controller class
	@Test
	public void testItem() {
		//mock the repository class
		//test the return of controller class

		//test 1: test the getItem method inside the controller
		//GET request
		Item item = new Item(11L, "name", 1.1);
		when(itemsRepository.findByItemId(11L)).thenReturn(item);
		//get the getItem method in the controller
		ResponseEntity<?> response = this.itemsController.getItem(11L, "name");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(true, response.getBody().equals(item));

		//test 2: test the getItems method inside the item controller
		Item item2 = new Item(12L, "name2", 1.2);
		List<Item> list = new ArrayList<>();
		list.add(item);
		list.add(item2);
		when(itemsRepository.findAll()).thenReturn(list);
		ResponseEntity<?> response2 = this.itemsController.getItems();
		assertEquals(HttpStatus.OK, response2.getStatusCode());
		assertNotNull(response2.getBody());
		assertEquals(true, response2.getBody().equals(list));

		//test3.1: test the updateItem method inside the item controller with correct inputs
		Item updateItem = new Item(11L, "newName", 2.1);
		when(itemsRepository.existsById(11L)).thenReturn(true);
		when(itemsRepository.save(updateItem)).thenReturn(updateItem);
		ResponseEntity<?> response3 = this.itemsController.updateItem(11L, "newName", 2.1);
		assertEquals(HttpStatus.OK, response3.getStatusCode());
		assertNotNull(response3.getBody());
		assertEquals(true, response3.getBody().equals(updateItem));

		//test3.2: test the updateItem method inside the item with invalid inputs
		ResponseEntity<?> response4 = this.itemsController.updateItem(11L, null, null);
		assertEquals(HttpStatus.BAD_REQUEST, response4.getStatusCode());
		assertNotNull(response4.getBody());
		assertEquals( true, response4.getBody().equals("No value for this update is specified."));

		System.out.println(response.getBody());
		System.out.println(response2.getBody());
		System.out.println(response3.getBody());

		//test4.1: test the addItem method inside the item with valid item
		Item newItem = new Item(20L, "new", 3.0);
		ResponseEntity<?> response5= this.itemsController.addItem(newItem);
		assertEquals(HttpStatus.OK, response5.getStatusCode());
		assertNotNull(response5.getBody());
		assertEquals( true, response5.getBody().equals(newItem));



	}


	@Test
	public void testAvailability() {
		//mock the repository class
		//test the return of controller class



	}





}
