package com.depot.ims;

import com.depot.ims.controllers.AvailabilitiesController;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

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

	@Mock
	private AvailabilitiesRepository availabilitiesRepository;
	@InjectMocks
	private AvailabilitiesController availabilitiesController;

	@Mock
	private  SitesRepository sitesRepository;
	@InjectMocks
	private  SitesController sitesController;


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

		//test1: test getAllAvailabilities method inside the availabilities controller class
		Item item = new Item(20L, "item1", 3.0);
		Item item1 = new Item(30L, "item2", 4.0);
		Site site = new Site(11L, "HomeDepot 2", "W54 N53", "open", null, true);
		Availability availability = new Availability(site, item, 10);
		Availability availability1 = new Availability(site, item1, 20);
		List<Availability> list = new ArrayList<>();
		list.add(availability);
		list.add(availability1);
		when(availabilitiesRepository.findAll()).thenReturn(list);
		ResponseEntity<?> response= this.availabilitiesController.getAllAvailabilities();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals( true, response.getBody().equals(list));

		//test2: test addAvailabilities method in the availabilities controller class
		when(availabilitiesRepository.save(availability)).thenReturn(availability);
		ResponseEntity<?> response1 = this.availabilitiesController.addAvailabilities(availability);
		assertEquals(HttpStatus.OK, response1.getStatusCode());
		assertNotNull(response1.getBody());
		assertEquals( true, response1.getBody().equals(availability));

		//test3: test getAvailability method in the availabilities controller class

		//mock sitesRepository.existsById(siteID)
		when(sitesRepository.existsById(11L)).thenReturn(true);
		//mock Site site = sitesRepository.findBySiteId(siteID);
		when(sitesRepository.findBySiteId(11L)).thenReturn(site);
		//mock ist<Availability> availabilityList = this.availabilitiesRepository.findBySiteId(site);
		when(availabilitiesRepository.findBySiteId(site)).thenReturn(list);
		ResponseEntity<?> response3 = this.availabilitiesController.getAvailability(11L);
		assertEquals(HttpStatus.OK, response3.getStatusCode());
		assertNotNull(response3.getBody());
		assertEquals( true, response3.getBody().equals(list));



		//test4: test getAvailabilitiesByItemId method in the availability controller class
		List<Site> siteList = new ArrayList<>();
		siteList.add(site);
		// mock itemsRepository.existsById(itemId)
		when(itemsRepository.existsById(20L)).thenReturn(true);
		// mock itemsRepository.findByItemId(itemId);
		when(itemsRepository.findByItemId(20L)).thenReturn(item);
		// mock availabilitiesRepository.findSitesByOneItem(item);
		when(availabilitiesRepository.findSitesByOneItem(item)).thenReturn(siteList);
		ResponseEntity<?> response4 = this.availabilitiesController.getAvailabilitiesByItemId(20L);
		assertEquals(HttpStatus.OK, response4.getStatusCode());
		assertNotNull(response4.getBody());
		assertEquals( true, response4.getBody().equals(siteList));

		//test 5: test getAvailabilityBySiteIdAndItemId method in the availability controller class
		//mock itemsRepository.existsById(itemId)
		//mock sitesRepository.existsById(siteId)
		//mock findByItemId(itemId)
		//mock findBySiteId(siteId)
		//already mocked
		when(availabilitiesRepository.findBySiteIdAndItemId(site, item)).thenReturn(availability);
		ResponseEntity<?> response5 = this.availabilitiesController.getAvailabilityBySiteIdAndItemId(11L, 20L);
		assertEquals(HttpStatus.OK, response5.getStatusCode());
		assertNotNull(response5.getBody());
		assertEquals( true, response5.getBody().equals(availability));


		//test5: test getSitesByItems method in the availability controller class
		//add the testing method for this
//		ObjectMapper mapper = new ObjectMapper();
//		// Create an empty array node
//		ArrayNode idList = mapper.createArrayNode();
//		// Add IDs to the array node
//		idList.add(11L);
//		idList.add(12L);
//		idList.add(13L);
//		// Convert ArrayNode to JsonNode
//		JsonNode jsonNode = (JsonNode) idList;









	}





}
