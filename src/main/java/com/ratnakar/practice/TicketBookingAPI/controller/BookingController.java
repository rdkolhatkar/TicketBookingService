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
        Booking booking = bookingService.bookTicket(
                request.getUserId(),
                request.getBookedByName(),
                request.getMovieName(),
                request.getNumberOfTickets()
        );
        return ResponseEntity.ok(booking);
    }

    // Update booking (change number of tickets)
    @PutMapping("/update/{bookingId}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long bookingId,
            @RequestBody UpdateBookingRequest request) throws Exception {

        return ResponseEntity.ok(
                bookingService.updateBooking(
                        bookingId,
                        request.getNewNumberOfTickets()
                )
        );
    }

    // Cancel booking
    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) throws Exception {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking with ID " + bookingId + " has been cancelled.");
    }

    // Get all bookings
    @GetMapping("/all")
    public ResponseEntity<List<BookingData>> getAllBookings() {
        List<BookingData> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
}

