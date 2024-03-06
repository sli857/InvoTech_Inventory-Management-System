package com.depot.ims;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.depot.ims.accessingdatajpa.User;
import com.depot.ims.accessingdatajpa.UserRepository;


@SpringBootApplication
public class ImsApplication {

	private static final Logger log = LoggerFactory.getLogger(ImsApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ImsApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository repository) {
	  return (args) -> {
		// save a few customers
		repository.save(new User("Jack", "Bauer"));
		repository.save(new User("Chloe", "O'Brian"));
		repository.save(new User("Kim", "Bauer"));
		repository.save(new User("David", "Palmer"));
		repository.save(new User("Michelle", "Dessler"));
  
		// fetch all customers
		log.info("Customers found with findAll():");
		log.info("-------------------------------");
		repository.findAll().forEach(customer -> {
		  log.info(customer.toString());
		});
		log.info("");
  
		// fetch an individual customer by ID
		User user = repository.findById(1);
		log.info("Customer found with findById(1L):");
		log.info("--------------------------------");
		log.info(user.toString());
		log.info("");
  
		// fetch customers by last name
		log.info("Customer found with findByLastName('Bauer'):");
		log.info("--------------------------------------------");
		repository.findByUsername("Bauer").forEach(bauer -> {
		  log.info(bauer.toString());
		});
		log.info("");
	  };
	}

}
