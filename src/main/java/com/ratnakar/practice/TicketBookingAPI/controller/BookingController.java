// Package declaration: Organizes classes into a namespace to avoid naming conflicts
// This follows Java package naming conventions (domain reversed - com.ratnakar.practice)
package com.ratnakar.practice.TicketBookingAPI.controller;

// Import statements: Bring in required classes from other packages
// Model classes: Define the data structures used in the application
import com.ratnakar.practice.TicketBookingAPI.model.Booking;
import com.ratnakar.practice.TicketBookingAPI.model.BookingData;
import com.ratnakar.practice.TicketBookingAPI.model.BookingRequest;
import com.ratnakar.practice.TicketBookingAPI.model.UpdateBookingRequest;
// Service class: Contains business logic (injected via dependency injection)
import com.ratnakar.practice.TicketBookingAPI.service.BookingService;
// Spring Framework annotations and classes for dependency injection
import org.springframework.beans.factory.annotation.Autowired;
// ResponseEntity: Spring class that represents the entire HTTP response (status, headers, body)
// Comes from org.springframework.http package
import org.springframework.http.ResponseEntity;
// Spring Web annotations for creating RESTful web services
import org.springframework.web.bind.annotation.*;

// Import Java Collections Framework class for list operations
import java.util.List;

/**
 * REST Controller class for handling booking-related HTTP requests.
 *
 * @RestController annotation marks this class as a Spring MVC Controller where
 * each method returns a domain object instead of a view. It combines 
 * @Controller and @ResponseBody annotations. When used, Spring understands
 * that this class will handle HTTP requests and the return values of methods
 * will be written directly to the HTTP response body (typically as JSON/XML).
 * This is a key component in building RESTful web services in Spring Boot.
 */
@RestController
/**
 * @RequestMapping annotation maps all HTTP requests with path "/api/bookings"
 * to this controller. This provides a base URL for all endpoints in this class.
 * The "api" prefix is a common convention to indicate these are API endpoints.
 */
@RequestMapping("/api/bookings")
public class BookingController {

    /**
     * Dependency Injection using @Autowired annotation.
     * @Autowired tells Spring to automatically inject an instance of BookingService
     * into this field. This is called "Field Injection". Spring searches for a
     * bean of type BookingService in its application context and injects it here.
     * This enables loose coupling between the controller and service layers.
     */
    @Autowired
    private BookingService bookingService;

    /**
     * Endpoint for booking a new ticket (CREATE operation).
     *
     * @PostMapping annotation maps HTTP POST requests to this method.
     * The "/book" path is appended to the base path "/api/bookings", 
     * so full endpoint URL is "/api/bookings/book".
     * POST method is used for creating new resources in RESTful APIs.
     *
     * @param request BookingRequest object automatically deserialized from 
     *                JSON in the HTTP request body by Spring (thanks to @RequestBody)
     * @return ResponseEntity<Booking> - HTTP response with status 200 OK and 
     *         the created Booking object in the response body
     * @throws Exception Propagates any exceptions that occur during ticket booking
     */
    // Book a ticket
    @PostMapping("/book")
    public ResponseEntity<Booking> bookTicket(@RequestBody BookingRequest request) throws Exception {
        // Call the service layer method to perform the actual business logic
        // The service method returns a Booking object with booking details
        Booking booking = bookingService.bookTicket(
                request.getUserId(),           // Extract user ID from request
                request.getBookedByName(),     // Extract name of person booking
                request.getMovieName(),        // Extract movie name from request
                request.getNumberOfTickets()   // Extract number of tickets requested
        );
        // ResponseEntity.ok() creates an HTTP 200 OK response with the booking object as body
        // Spring automatically converts the Booking object to JSON format
        return ResponseEntity.ok(booking);
    }

    /**
     * Endpoint for updating an existing booking (UPDATE operation).
     *
     * @PutMapping annotation maps HTTP PUT requests to this method.
     * The "/update/{bookingId}" path includes a path variable {bookingId}.
     * PUT method is typically used for updating existing resources in RESTful APIs.
     *
     * @param bookingId Path variable extracted from URL (e.g., /api/bookings/update/123)
     * @param request UpdateBookingRequest object deserialized from JSON request body
     * @return ResponseEntity<Booking> - HTTP 200 OK response with updated Booking object
     * @throws Exception Propagates any exceptions during update operation
     */
    // Update booking (change number of tickets)
    @PutMapping("/update/{bookingId}")
    public ResponseEntity<Booking> updateBooking(
            // @PathVariable extracts the value from the URL path and maps it to the parameter
            @PathVariable Long bookingId,
            // @RequestBody extracts JSON from request body and converts to Java object
            @RequestBody UpdateBookingRequest request) throws Exception {

        // Return HTTP 200 OK response with the updated booking object
        // The response body contains the Booking returned by the service method
        return ResponseEntity.ok(
                bookingService.updateBooking(
                        bookingId,                     // Pass booking ID to service
                        request.getNewNumberOfTickets() // Pass new ticket count from request
                )
        );
    }

    /**
     * Endpoint for cancelling/delete a booking (DELETE operation).
     *
     * @DeleteMapping annotation maps HTTP DELETE requests to this method.
     * The "/cancel/{bookingId}" path includes a path variable for booking ID.
     * DELETE method is used for removing resources in RESTful APIs.
     *
     * @param bookingId Path variable extracted from URL for the booking to cancel
     * @return ResponseEntity<String> - HTTP 200 OK with confirmation message
     * @throws Exception Propagates any exceptions during cancellation
     */
    // Cancel booking
    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) throws Exception {
        // Call service method to cancel the booking (void method)
        bookingService.cancelBooking(bookingId);
        // Return HTTP 200 OK with a success message in the response body
        return ResponseEntity.ok("Booking with ID " + bookingId + " has been cancelled.");
    }

    /**
     * Endpoint for retrieving all bookings (READ operation).
     *
     * @GetMapping annotation maps HTTP GET requests to this method.
     * The "/all" path is appended to the base path.
     * GET method is used for retrieving resources in RESTful APIs.
     *
     * @return ResponseEntity<List<BookingData>> - HTTP 200 OK with list of all bookings
     * Note: Returns BookingData objects instead of Booking objects, likely a simplified view
     */
    // Get all bookings
    @GetMapping("/all")
    public ResponseEntity<List<BookingData>> getAllBookings() {
        // Call service method to retrieve all bookings
        List<BookingData> bookings = bookingService.getAllBookings();
        // Return HTTP 200 OK with the list of bookings in the response body
        return ResponseEntity.ok(bookings);
    }
}