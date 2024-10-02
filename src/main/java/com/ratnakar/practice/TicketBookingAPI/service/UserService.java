package com.ratnakar.practice.TicketBookingAPI.service;

import com.ratnakar.practice.TicketBookingAPI.exception.UserException;
import com.ratnakar.practice.TicketBookingAPI.model.User;

public interface UserService {
    public User createUser(User user) throws UserException;
    public User updateUser(User user, String key) throws UserException;
}
