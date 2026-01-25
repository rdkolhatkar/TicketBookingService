package com.ratnakar.practice.TicketBookingAPI.service;

import com.ratnakar.practice.TicketBookingAPI.model.Booking;
import com.ratnakar.practice.TicketBookingAPI.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    private static final int MAX_TICKETS = 100;

    @Override
    public Booking bookTicket(String userId, String bookedByName, String movieName, int numberOfTickets) throws Exception {
        if (numberOfTickets < 1 || numberOfTickets > MAX_TICKETS) {
            throw new Exception("You can book between 1 and 100 tickets.");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setBookedByName(bookedByName);
        booking.setMovieName(movieName);
        booking.setNumberOfTickets(numberOfTickets);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Long bookingId, int newNumberOfTickets) throws Exception {
        if (newNumberOfTickets < 1 || newNumberOfTickets > MAX_TICKETS) {
            throw new Exception("You can book between 1 and 100 tickets.");
        }

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (!optionalBooking.isPresent()) {
            throw new Exception("Booking not found with id: " + bookingId);
        }

        Booking booking = optionalBooking.get();
        booking.setNumberOfTickets(newNumberOfTickets);
        return bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(Long bookingId) throws Exception {
        if (!bookingRepository.existsById(bookingId)) {
            throw new Exception("Booking not found with id: " + bookingId);
        }
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}

