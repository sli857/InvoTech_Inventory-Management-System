package com.depot.ims;

import com.depot.ims.models.Availability;
import com.depot.ims.models.Site;
import com.depot.ims.models.Item;
import com.depot.ims.models.User;
import com.depot.ims.repositories.AvailabilitiesRepository;
import com.depot.ims.repositories.ItemsRepository;
import com.depot.ims.repositories.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.depot.ims.repositories.SitesRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication public class ImsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImsApplication.class, args);
    }

    @Bean public CommandLineRunner commandLineRunner(SitesRepository sitesRepository, ItemsRepository itemsRepository, UsersRepository usersRepository, AvailabilitiesRepository availabilitiesRepository) {
        return (args) -> {

            Site site1 = new Site(1L, "HomeDepot 2", "W54 N53", "open", null, true);
            Site site2 = new Site(2L, "HomeDepot 3", "W54 N52", "open", null, true);
            sitesRepository.save(site1);
            sitesRepository.save(site2);

            sitesRepository.updateSiteName(2L, "newname");
            Site actualSite = sitesRepository.findBySiteId(2L);
            String name = actualSite.getSiteName();

            System.out.println(name);

//            Site site1 = new Site(1L, "HomeDepot 2", "W54 N53", "open", null, true);
//            Site site2 = new Site(2L, "HomeDepot 3", "W54 N52", "open", null, true);
//            sitesRepository.save(site1);
//            sitesRepository.save(site2);
//            sitesRepository.save(new Site(3L, "HomeDepot 4", "W54 N51", "open", null, true));
//////            sitesRepository.save(new Site(null, "HomeDepot 5", "W54 N50", "open", null, true));
//            Item item1 = new Item(1L, "item1", 2.0);
//            Item item2 = new Item(2L, "item2", 3.0);
//            itemsRepository.save(item1);
//            itemsRepository.save(item2);
//            itemsRepository.save(new Item(3L, "item3", 4.0));
//
//            availabilitiesRepository.save(new Availability(site1, item1, 10));
//            availabilitiesRepository.save(new Availability(site2, item1, 20));
//            List<Site> list = new ArrayList<>();
//            list.add(site1);
//            list.add(site2);

//            int sum = availabilitiesRepository.findCombineLevel(list, 1L);
//            List<Availability> a = availabilitiesRepository.findBySiteId(site1);
//            System.out.println("hiii");
//            for (Availability availability : a) {
//                // Print the information related to each Availability object
//                System.out.println("Site ID: " + availability.getSiteId());
//                System.out.println("Item ID: " + availability.getItemId());
//                System.out.println("Quantity: " + availability.getQuantity());
//                // Add more properties as needed
//
//            }
//            int sum = availabilitiesRepository.findCombineLevel(list, item1);
//            System.out.println(sum);
//            int diff = availabilitiesRepository.findDiffLevel(site1, site2, item1);
//            System.out.println(diff);






//
//            Timestamp timestamp = Timestamp.valueOf("2024-03-14 12:34:56");
//            sitesRepository.deleteSite(2L, timestamp);
//            sitesRepository.upDateSiteStatus(2L, "close");
//            sitesRepository.upDateSiteName(2L, "test name");
//            sitesRepository.upDateCeaseDate(3L, timestamp);




//
//            itemsRepository.save(new Item(null, "apple", 12.00));
//            itemsRepository.save(new Item(null, "apple_pear", 13.00));
//            itemsRepository.save(new Item(null, "pear", 14.00));
//
//            usersRepository.save(new User(null, "user1", "pass1"));
//            usersRepository.save(new User(null, "user2", "pass2"));
//            usersRepository.save(new User(null, "user3", "pass3"));
//
        };
    }

}
