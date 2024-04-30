package com.depot.ims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** Starter of the entire Inventory Management System application. Enjoy! :) */
@SpringBootApplication
@EnableJpaAuditing
public class ImsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ImsApplication.class, args);
  }
}
