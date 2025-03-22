package com.ratnakar.practice.TicketBookingAPI;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.ratnakar.practice.TicketBookingAPI")
public class TicketBookingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketBookingApiApplication.class, args);
	}

	// Start WireMock Server when the application starts
	CommandLineRunner startWireMockServer() {
		return args -> {
			// Configure WireMock to use files under src/mock
			WireMockConfiguration config = WireMockConfiguration.wireMockConfig()
					.port(8091)
					.usingFilesUnderDirectory("src/mock");

			WireMockServer wireMockServer = new WireMockServer(config);
			wireMockServer.start();

			System.out.println("WireMock server started on port 8091 using src/mock as the file directory");
		};
	}

}

