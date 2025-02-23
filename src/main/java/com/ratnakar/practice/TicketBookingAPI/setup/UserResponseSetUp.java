package com.ratnakar.practice.TicketBookingAPI.setup;

import com.ratnakar.practice.TicketBookingAPI.model.User;
import com.ratnakar.practice.TicketBookingAPI.model.UserResponse;
import com.ratnakar.practice.TicketBookingAPI.repository.UserRepository;
import com.ratnakar.practice.TicketBookingAPI.service.UserRegistrationService;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


public class UserResponseSetUp {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRegistrationService userRegistrationService;
    @Autowired
    UserResponse userResponse;

    User user = new User();


    public ResponseEntity userRegistrationResponse(){
        String uniqueName = user.getUserName();
        if(!userRegistrationService.checkUserAlreadyExists(uniqueName)){
        user.setUserName(uniqueName);
        userRepository.save(user);
        userResponse.setMsg("New User Added Successfully");
        userResponse.setUserName(uniqueName);
        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.CREATED);
        } else {
            userResponse.setMsg("UserName Already Exists, Please Enter Unique UserName");
            userResponse.setUserName(uniqueName);
            return new ResponseEntity<UserResponse>(userResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
