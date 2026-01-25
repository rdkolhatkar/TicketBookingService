package com.ratnakar.practice.TicketBookingAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(nullable = false)
    private String userId; // Reference to User

    @Column(nullable = false)
    private String bookedByName;

    @Column(nullable = false)
    private String movieName;

    @Column(nullable = false)
    private int numberOfTickets;
}

