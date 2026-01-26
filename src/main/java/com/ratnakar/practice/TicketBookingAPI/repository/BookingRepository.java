package com.ratnakar.practice.TicketBookingAPI.repository;

import com.ratnakar.practice.TicketBookingAPI.model.Booking;
import com.ratnakar.practice.TicketBookingAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all bookings by User entity
    List<Booking> findByUser(User user);
}
