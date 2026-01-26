package com.ratnakar.practice.TicketBookingAPI.service;

import com.ratnakar.practice.TicketBookingAPI.exception.BookingException;
import com.ratnakar.practice.TicketBookingAPI.model.Booking;
import com.ratnakar.practice.TicketBookingAPI.model.BookingData;
import com.ratnakar.practice.TicketBookingAPI.model.User;
import com.ratnakar.practice.TicketBookingAPI.repository.BookingRepository;
import com.ratnakar.practice.TicketBookingAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    private static final int MAX_TICKETS = 100;

    @Override
    public Booking bookTicket(String userId, String bookedByName, String movieName, int numberOfTickets) throws Exception {

        if (numberOfTickets < 1 || numberOfTickets > MAX_TICKETS) {
            throw new Exception("You can book between 1 and 100 tickets.");
        }

        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found with ID: " + userId));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBookedByName(bookedByName);
        booking.setMovieName(movieName);
        booking.setNumberOfTickets(numberOfTickets);

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking updateBooking(Long bookingId, int newNumberOfTickets) {

        if (newNumberOfTickets < 1 || newNumberOfTickets > MAX_TICKETS) {
            throw new BookingException("You can book between 1 and 100 tickets.");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new BookingException("Booking not found with id: " + bookingId));

        booking.setNumberOfTickets(newNumberOfTickets);

        // No need to explicitly call save() inside transaction,
        // but keeping it is also fine
        return bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(Long bookingId) throws Exception {
        if (!bookingRepository.existsById(bookingId)) {
            throw new Exception("Booking not found with id: " + bookingId);
        }
        bookingRepository.deleteById(bookingId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingData> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingData::new) // now Booking matches constructor
                .collect(Collectors.toList());
    }


}
