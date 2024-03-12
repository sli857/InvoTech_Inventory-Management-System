package com.depot.ims;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.depot.ims.models.Site;
import com.depot.ims.repositories.SitesRepository;

@SpringBootApplication public class ImsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImsApplication.class, args);
    }

    @Bean public CommandLineRunner commandLineRunner(SitesRepository sitesRepository) {
        return (args) -> {
            // sitesRepository.save(new Sites(null, "HomeDepot 1", "W54 N54", "open",
            // null,
            // true));
            sitesRepository.save(new Site(null, "HomeDepot 2", "W54 N53", "open", null, true));
            sitesRepository.save(new Site(null, "HomeDepot 3", "W54 N52", "open", null, true));
            sitesRepository.save(new Site(null, "HomeDepot 4", "W54 N51", "open", null, true));
            sitesRepository.save(new Site(null, "HomeDepot 5", "W54 N50", "open", null, true));

            // sitesRepository.findAllSites().forEach(System.out::println);
            sitesRepository.findBySiteName("HomeDepot 5").forEach(System.out::println);

            sitesRepository.findAll().forEach(System.out::println);

        };
    }

}
