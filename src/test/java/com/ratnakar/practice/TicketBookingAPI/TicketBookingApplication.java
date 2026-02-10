// Package declaration: Root package of the Spring Boot application
// This follows standard Java package naming convention (reverse domain name)
package com.ratnakar.practice.TicketBookingAPI;

// Spring Boot imports for application configuration and startup

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * TicketBookingApiApplication - Main entry point of the Spring Boot application.
 *
 * This is the starting point of the Spring Boot application. When the application starts,
 * Spring Boot's auto-configuration kicks in and sets up the application context based on
 * the dependencies present in the classpath.
 *
 * @SpringBootApplication annotation is a convenience annotation that combines:
 * 1. @Configuration: Marks this class as a source of bean definitions for the application context.
 *    Configuration classes can declare beans using @Bean methods.
 *
 * 2. @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings,
 *    other beans, and various property settings. For example, if spring-webmvc is on the classpath,
 *    this annotation flags the application as a web application and activates key behaviors such
 *    as setting up a DispatcherServlet.
 *
 * 3. @ComponentScan: Tells Spring to look for other components, configurations, and services
 *    in the 'com.ratnakar.practice.TicketBookingAPI' package and its sub-packages, allowing it to
 *    find and register controllers, services, repositories, etc.
 *
 * The explicit @ComponentScan annotation here is actually redundant because @SpringBootApplication
 * already includes @ComponentScan. However, it can be used to specify specific base packages if
 * the application structure is non-standard.
 *
 * HOW SPRING BOOT STARTS:
 * 1. The main method calls SpringApplication.run() which launches the application.
 * 2. Spring Boot performs auto-configuration based on the dependencies in pom.xml or build.gradle.
 * 3. It starts an embedded web server (Tomcat, Jetty, or Undertow) if it's a web application.
 * 4. It sets up the Spring application context and registers all beans.
 * 5. It runs CommandLineRunner beans (if any) and the application is ready to serve requests.
 *
 * EMBEDDED SERVER:
 * Spring Boot includes an embedded Tomcat server by default (if spring-boot-starter-web is included).
 * This eliminates the need to deploy WAR files to an external server - the application is self-contained.
 */
@SpringBootApplication
public class TicketBookingApplication {

	/**
	 * Main method - Entry point of the application.
	 *
	 * This is the standard Java main method that serves as the application entry point.
	 * When the application is started, the JVM calls this method.
	 *
	 * @param args Command-line arguments passed to the application.
	 *             These can be used to override application properties, profiles, etc.
	 *             Example: --server.port=8081 to change the server port.
	 *
	 * SpringApplication.run() does the following:
	 * 1. Creates an instance of Spring ApplicationContext (container for beans)
	 * 2. Registers the TicketBookingApiApplication bean (because it's annotated with @SpringBootApplication)
	 * 3. Performs classpath scanning for components in the specified packages
	 * 4. Starts the embedded web server (if it's a web application)
	 * 5. Triggers any CommandLineRunner beans
	 *
	 * The method returns an ApplicationContext which can be used to interact with the Spring container,
	 * but in a typical web application, we don't need to use the return value.
	 */
	public static void main(String[] args) {
		// SpringApplication.run() bootstraps the Spring application
		// Parameters:
		// 1. TicketBookingApiApplication.class: The primary Spring component (configuration class)
		// 2. args: Command-line arguments
		SpringApplication.run(TicketBookingApplication.class, args);

		// After this line, the application is running and listening for HTTP requests (if it's a web app)
		// The main thread will wait indefinitely until the application is shut down
	}

	// Note: Additional configuration can be added to this class:

	/**
	 * Example: Adding a CommandLineRunner bean to run code at startup
	 *
	 * @Bean
	 * public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
	 *     return args -> {
	 *         System.out.println("Application started successfully!");
	 *         // You can print beans, check database connections, etc.
	 *     };
	 * }
	 */

	/**
	 * Example: Adding a custom banner
	 *
	 * Spring Boot looks for a banner.txt file in the resources folder.
	 * You can customize the startup banner by creating src/main/resources/banner.txt
	 */

	/**
	 * SPRING BOOT AUTO-CONFIGURATION MECHANISM:
	 *
	 * Spring Boot uses conditional auto-configuration based on:
	 * 1. Classpath dependencies (what JARs are present)
	 * 2. Existing beans in the context
	 * 3. Property settings (application.properties/yml)
	 *
	 * For example:
	 * - If H2 database is in classpath, configures an in-memory database
	 * - If spring-boot-starter-web is present, configures a web application
	 * - If spring-boot-starter-data-jpa is present, configures JPA repositories
	 */

	/**
	 * APPLICATION PROPERTIES:
	 *
	 * Spring Boot applications are typically configured via:
	 * 1. application.properties or application.yml in src/main/resources
	 * 2. Profile-specific properties (application-dev.properties, application-prod.properties)
	 * 3. Environment variables
	 * 4. Command-line arguments
	 *
	 * Common properties to configure:
	 * - Server port: server.port=8080
	 * - Database connection: spring.datasource.url, username, password
	 * - JPA settings: spring.jpa.hibernate.ddl-auto, show-sql
	 * - Logging: logging.level.com.ratnakar=DEBUG
	 */

	/**
	 * SPRING PROFILES:
	 *
	 * Profiles allow different configuration for different environments (dev, test, prod).
	 *
	 * Activating a profile:
	 * 1. In application.properties: spring.profiles.active=dev
	 * 2. Command line: --spring.profiles.active=dev
	 * 3. Environment variable: SPRING_PROFILES_ACTIVE=dev
	 *
	 * Profile-specific properties files: application-dev.properties, application-prod.properties
	 */

	/**
	 * MONITORING AND MANAGEMENT:
	 *
	 * Spring Boot Actuator provides production-ready features to monitor and manage the application:
	 * 1. Health checks: /actuator/health
	 * 2. Metrics: /actuator/metrics
	 * 3. Environment details: /actuator/env
	 * 4. Application info: /actuator/info
	 *
	 * To enable, add dependency: spring-boot-starter-actuator
	 */
}