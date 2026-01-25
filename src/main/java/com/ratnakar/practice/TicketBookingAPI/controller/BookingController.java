package com.ratnakar.practice.TicketBookingAPI.controller;

import com.ratnakar.practice.TicketBookingAPI.model.Booking;
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
    public ResponseEntity<Booking> bookTicket(
            @RequestParam String userId,
            @RequestParam String bookedByName,
            @RequestParam String movieName,
            @RequestParam int numberOfTickets
    ) throws Exception {
        Booking booking = bookingService.bookTicket(userId, bookedByName, movieName, numberOfTickets);
        return ResponseEntity.ok(booking);
    }

    // Update booking (change number of tickets)
    @PutMapping("/update/{bookingId}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long bookingId,
            @RequestParam int newNumberOfTickets
    ) throws Exception {
        Booking updatedBooking = bookingService.updateBooking(bookingId, newNumberOfTickets);
        return ResponseEntity.ok(updatedBooking);
    }

    // Cancel booking
    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) throws Exception {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking with ID " + bookingId + " has been cancelled.");
    }

    // Get all bookings
    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
}

