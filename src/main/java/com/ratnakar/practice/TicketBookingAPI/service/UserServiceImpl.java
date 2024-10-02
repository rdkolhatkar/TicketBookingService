package com.ratnakar.practice.TicketBookingAPI.service;

import com.ratnakar.practice.TicketBookingAPI.exception.UserException;
import com.ratnakar.practice.TicketBookingAPI.model.User;
import com.ratnakar.practice.TicketBookingAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) throws UserException {
        User registeredUser = userRepository.findByEmail(user.getMobile());
        if(registeredUser != null) throw new UserException("User is already registered!");
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, String key) throws UserException {
        return null;
    }
}
