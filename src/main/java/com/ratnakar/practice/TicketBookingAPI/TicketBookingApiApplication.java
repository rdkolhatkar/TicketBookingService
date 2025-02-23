package com.ratnakar.practice.TicketBookingAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication( scanBasePackages={
		"com.ratnakar.practice.TicketBookingAPI.setup",
		"com.ratnakar.practice.TicketBookingAPI.service",
		"com.ratnakar.practice.TicketBookingAPI.model",
		"com.ratnakar.practice.TicketBookingAPI.repository",
		"com.ratnakar.practice.TicketBookingAPI.exception",
		"com.ratnakar.practice.TicketBookingAPI.utils"
} )
public class TicketBookingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketBookingApiApplication.class, args);
	}

}

