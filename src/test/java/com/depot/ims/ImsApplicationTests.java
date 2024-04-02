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

@SpringBootTest
@AutoConfigureMockMvc
class ImsApplicationTests {

//	@Mock
//	private SitesRepository sitesRepository;
//	@InjectMocks
//	private SitesController userController;

	@Mock
	private ItemsRepository itemsRepository;
	@InjectMocks
	private ItemsController itemsController;

//	@Autowired
//	private AvailabilitiesRepository availabilitiesRepository;
//	@Autowired
//	private SitesRepository sitesRepository;
//	@Autowired
//	private ItemsRepository itemsRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {

	}

	@Test
	public void testAvailability() {
		//mock the repository class
		//test the return of controller class

	}

	//use Junit test to test the controller class
	@Test
	public void testItem() {
		//mock the repository class
		//test the return of controller class

		//GET request
		Item item = new Item(11L, "name", 1.1);
		when(itemsRepository.findByItemId(11L)).thenReturn(item);
		try {
			mockMvc.perform(get("/items")
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()); // Expect HTTP status code 200 OK
			//.andExpect(jsonPath("$.itemId").value("11L"));// Verify username
//                    .andExpect(jsonPath("$.password").value("password")); // Verify password
		} catch (Exception e) {
			throw new RuntimeException(e);
		}



		//System.out.println(result.getResponse().getContentAsString());
		System.out.println("hiii");
//		ResponseEntity<?> response = this.itemsController.getItem(11L, "name");
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertNotNull(response.getBody());
//		assertEquals(true, response.getBody().equals(item));



//		assertEquals("name", response.getBody().getUsername());
//		assertEquals("password", response.getBody().getPassword());
		//fail();

		//when(sitesRepository.)


		//testing repository
//		Site site1 = new Site(1L, "HomeDepot 2", "W54 N53", "open", null, true);
//		Site site2 = new Site(2L, "HomeDepot 3", "W54 N52", "open", null, true);
//		sitesRepository.save(site1);
//		sitesRepository.save(site2);
//		sitesRepository.updateSiteName(2L, "newname");
//		Site actualSite = sitesRepository.findBySiteId(2L);
//		String name = actualSite.getSiteName();
//		assertEquals(name, "newname");


//		sitesRepository.save(new Site(3L, "HomeDepot 4", "W54 N51", "open", null, true));
//////            sitesRepository.save(new Site(null, "HomeDepot 5", "W54 N50", "open", null, true));
//		Item item1 = new Item(1L, "item1", 2.0);
//		Item item2 = new Item(2L, "item2", 3.0);
//		itemsRepository.save(item1);
//		itemsRepository.save(item2);
//		itemsRepository.save(new Item(3L, "item3", 4.0));
//
//		availabilitiesRepository.save(new Availability(site1, item1, 10));
//		availabilitiesRepository.save(new Availability(site2, item1, 20));

	}





}
