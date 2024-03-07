package com.depot.ims;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.depot.ims.models.Sites;
import com.depot.ims.repositories.SitesRepository;

@SpringBootApplication
public class ImsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImsApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(
		SitesRepository sitesRepository
	) {
		return(args) -> {		
			sitesRepository.save(new Sites(null, "HomeDepot 1", "W54 N54", "open", null, true));
		};
	}

}