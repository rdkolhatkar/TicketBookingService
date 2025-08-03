package com.ratnakar.practice.TicketBookingAPI.service;

import com.ratnakar.practice.TicketBookingAPI.exception.UserException;
import com.ratnakar.practice.TicketBookingAPI.model.User;

import java.util.List;

public interface UserService {
    public User createUser(User user) throws UserException;
    public User updateUser(User user, String key) throws UserException;
    List<User> getAllUsers() throws UserException;
    public void deleteUserById(String userId) throws UserException;
    User getUserById(String userId) throws UserException;

}
