package com.depot.ims;

import com.depot.ims.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Site;
import com.depot.ims.models.Item;
import com.depot.ims.models.User;

@SpringBootTest
class ImsApplicationTests {

	@Autowired
	private AvailabilitiesRepository availabilitiesRepository;
	@Autowired
	private SitesRepository sitesRepository;
	@Autowired
	private ItemsRepository itemsRepository;

	@Test
	void contextLoads() {

	}

	@Test
	public void testItemRepository() {

		Site site1 = new Site(1L, "HomeDepot 2", "W54 N53", "open", null, true);
		Site site2 = new Site(2L, "HomeDepot 3", "W54 N52", "open", null, true);
		sitesRepository.save(site1);
		sitesRepository.save(site2);
		sitesRepository.updateSiteName(2L, "newname");
		Site actualSite = sitesRepository.findBySiteId(2L);
		String name = actualSite.getSiteName();
		assertEquals(name, "newname");
		



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
