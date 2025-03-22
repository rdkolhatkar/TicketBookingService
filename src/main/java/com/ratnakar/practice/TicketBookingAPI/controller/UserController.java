package com.ratnakar.practice.TicketBookingAPI.controller;

import com.ratnakar.practice.TicketBookingAPI.exception.UserException;
import com.ratnakar.practice.TicketBookingAPI.model.User;
import com.ratnakar.practice.TicketBookingAPI.service.UserService;
import com.ratnakar.practice.TicketBookingAPI.setup.UserResponseSetUp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    UserResponseSetUp userResponseSetUp;
    @PostMapping("/user/register")
    public ResponseEntity registerUser(@Valid @RequestBody User user) throws UserException {
        return userResponseSetUp.userRegistrationResponse(user);
    }
}