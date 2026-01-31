package com.ratnakar.practice.TicketBookingAPI.controller;
import com.ratnakar.practice.TicketBookingAPI.model.Booking;
import com.ratnakar.practice.TicketBookingAPI.model.BookingData;
import com.ratnakar.practice.TicketBookingAPI.model.BookingRequest;
import com.ratnakar.practice.TicketBookingAPI.model.UpdateBookingRequest;
import com.ratnakar.practice.TicketBookingAPI.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;
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
    // Cancel booking
    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) throws Exception {
        // Call service method to cancel the booking (void method)
        bookingService.cancelBooking(bookingId);
        // Return HTTP 200 OK with a success message in the response body
        return ResponseEntity.ok("Booking with ID " + bookingId + " has been cancelled.");
    }
    // Get all bookings
    @GetMapping("/all")
    public ResponseEntity<List<BookingData>> getAllBookings() {
        // Call service method to retrieve all bookings
        List<BookingData> bookings = bookingService.getAllBookings();
        // Return HTTP 200 OK with the list of bookings in the response body
        return ResponseEntity.ok(bookings);
    }
}