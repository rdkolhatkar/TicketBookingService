package com.ratnakar.practice.TicketBookingAPI.service;

import com.ratnakar.practice.TicketBookingAPI.exception.UserException;
import com.ratnakar.practice.TicketBookingAPI.model.User;
import com.ratnakar.practice.TicketBookingAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) throws UserException {
        User registeredUser = userRepository.findByEmail(user.getMobile());
        if (registeredUser != null) throw new UserException("User is already registered!");
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, String key) throws UserException {
        return null;
    }

    @Override
    public List<User> getAllUsers() throws UserException {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserException("No users found");
        }
        return users;
    }

    @Override
    public void deleteUserById(String userId) throws UserException {
        if (!userRepository.existsById(userId)) {
            throw new UserException("User with ID " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public User getUserById(String userId) throws UserException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User with ID " + userId + " not found"));
    }
}