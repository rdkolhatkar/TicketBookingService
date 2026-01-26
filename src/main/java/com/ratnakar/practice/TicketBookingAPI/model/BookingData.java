package com.ratnakar.practice.TicketBookingAPI.model;

public class BookingData {
    private Long bookingId;
    private String bookedByName;
    private String movieName;
    private int numberOfTickets;
    private String userId; // store just the userId

    public BookingData(Booking booking) {
        this.bookingId = booking.getBookingId();
        this.bookedByName = booking.getBookedByName();
        this.movieName = booking.getMovieName();
        this.numberOfTickets = booking.getNumberOfTickets();
        this.userId = booking.getUser().getUserID();
    }


    // Getters
    public Long getBookingId() { return bookingId; }
    public String getBookedByName() { return bookedByName; }
    public String getMovieName() { return movieName; }
    public int getNumberOfTickets() { return numberOfTickets; }
    public String getUserId() { return userId; }
}
