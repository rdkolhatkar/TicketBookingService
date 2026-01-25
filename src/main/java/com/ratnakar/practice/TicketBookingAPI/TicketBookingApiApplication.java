package com.ratnakar.practice.TicketBookingAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.ratnakar.practice.TicketBookingAPI")
public class TicketBookingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketBookingApiApplication.class, args);
	}

}

