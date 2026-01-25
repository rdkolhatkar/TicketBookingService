package com.ratnakar.practice.TicketBookingAPI.service;

import com.ratnakar.practice.TicketBookingAPI.model.Booking;

import java.util.List;

public interface BookingService {

    Booking bookTicket(String userId, String bookedByName, String movieName, int numberOfTickets) throws Exception;

    Booking updateBooking(Long bookingId, int newNumberOfTickets) throws Exception;

    void cancelBooking(Long bookingId) throws Exception;

    List<Booking> getAllBookings();
}

