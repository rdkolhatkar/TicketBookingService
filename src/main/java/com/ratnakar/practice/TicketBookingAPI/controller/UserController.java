package com.ratnakar.practice.TicketBookingAPI.controller;

import com.ratnakar.practice.TicketBookingAPI.exception.UserException;
import com.ratnakar.practice.TicketBookingAPI.model.User;
import com.ratnakar.practice.TicketBookingAPI.service.UserService;
import com.ratnakar.practice.TicketBookingAPI.setup.UserResponseSetUp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    UserResponseSetUp userResponseSetUp;
    @PostMapping("/api/users/register")
    public ResponseEntity registerUser(@Valid @RequestBody User user) throws UserException {
        return userResponseSetUp.userRegistrationResponse(user);
    }
    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers() throws UserException {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @DeleteMapping("/api/users/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") String userId) throws UserException {
        userService.deleteUserById(userId);
        return ResponseEntity.ok("User with ID " + userId + " deleted successfully");
    }
    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String userId) throws UserException {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
}