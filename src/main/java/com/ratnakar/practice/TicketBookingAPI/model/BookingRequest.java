package com.ratnakar.practice.TicketBookingAPI.model;

import lombok.Data;

@Data
public class BookingRequest {
    private String userId;
    private String bookedByName;
    private String movieName;
    private int numberOfTickets;
    private int newNumberOfTickets;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookedByName() {
        return bookedByName;
    }

    public void setBookedByName(String bookedByName) {
        this.bookedByName = bookedByName;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public int getNewNumberOfTickets() {
        return newNumberOfTickets;
    }

    public void setNewNumberOfTickets(int newNumberOfTickets) {
        this.newNumberOfTickets = newNumberOfTickets;
    }
}
