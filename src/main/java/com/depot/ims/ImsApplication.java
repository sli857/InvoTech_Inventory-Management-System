package com.depot.ims;

import com.depot.ims.models.User;
import com.depot.ims.repositories.ItemsRepository;
import com.depot.ims.repositories.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.depot.ims.repositories.SitesRepository;

@SpringBootApplication public class ImsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImsApplication.class, args);
    }

//    @Bean public CommandLineRunner commandLineRunner(SitesRepository sitesRepository, ItemsRepository itemsRepository, UsersRepository usersRepository) {
//        return (args) -> {
//
//            sitesRepository.save(new Site(null, "HomeDepot 2", "W54 N53", "open", null, true));
//            sitesRepository.save(new Site(null, "HomeDepot 3", "W54 N52", "open", null, true));
//            sitesRepository.save(new Site(null, "HomeDepot 4", "W54 N51", "open", null, true));
//            sitesRepository.save(new Site(null, "HomeDepot 5", "W54 N50", "open", null, true));
//
//            itemsRepository.save(new Item(null, "apple", 12.00));
//            itemsRepository.save(new Item(null, "apple_pear", 13.00));
//            itemsRepository.save(new Item(null, "pear", 14.00));
//
//            usersRepository.save(new User(null, "user1", "pass1"));
//            usersRepository.save(new User(null, "user2", "pass2"));
//            usersRepository.save(new User(null, "user3", "pass3"));
//
//        };
//    }

}
