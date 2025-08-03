package com.ratnakar.practice.TicketBookingAPI.setup;

import com.ratnakar.practice.TicketBookingAPI.model.User;
import com.ratnakar.practice.TicketBookingAPI.model.UserResponse;
import com.ratnakar.practice.TicketBookingAPI.repository.UserRepository;
import com.ratnakar.practice.TicketBookingAPI.service.UserRegistrationService;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class UserResponseSetUp {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRegistrationService userRegistrationService;

    public ResponseEntity<UserResponse> userRegistrationResponse(User user) {
        UserResponse userResponse = new UserResponse(); // create new instance each time
        User savedUser = userRepository.save(user);
        String uniqueName = user.getUserName();
        String uniqueId = user.getUserID();
        String uniqueFirstName = user.getFirstName();
        String uniqueLastName = user.getLastName();
        if (!userRegistrationService.checkUserAlreadyExists(uniqueName)) {
            userResponse.setMsg("New User Added Successfully");
            userResponse.setUserName(uniqueName);
            savedUser.getUserID();
            userResponse.setUserID(uniqueId);
            userResponse.setFirstName(uniqueFirstName);
            userResponse.setLastName(uniqueLastName);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } else {
            userResponse.setMsg("UserName Already Exists, Please Enter Unique UserName");
            userResponse.setUserName(uniqueName);
            return new ResponseEntity<>(userResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
