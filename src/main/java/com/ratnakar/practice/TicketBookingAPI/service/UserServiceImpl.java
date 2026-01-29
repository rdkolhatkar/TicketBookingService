// Package declaration: Organizes service implementation classes together
package com.ratnakar.practice.TicketBookingAPI.service;

// Import custom exception for user-related errors
import com.ratnakar.practice.TicketBookingAPI.exception.UserException;
// Import User entity class
import com.ratnakar.practice.TicketBookingAPI.model.User;
// Import repository for data access operations
import com.ratnakar.practice.TicketBookingAPI.repository.UserRepository;
// Spring Framework annotations for dependency injection and service layer
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Import Java Collections Framework for List return type
import java.util.List;

/**
 * UserServiceImpl - Implementation of UserService interface.
 *
 * @Service annotation marks this class as a Spring Service bean.
 * Service beans contain business logic and are managed by Spring container.
 *
 * This class implements UserService interface, providing concrete
 * implementation for all user management operations.
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Dependency Injection of UserRepository.
     * @Autowired injects an instance of UserRepository.
     * UserRepository provides database operations for User entities.
     *
     * Using field injection here. Constructor injection would be better
     * for immutability and easier testing.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new user in the system.
     *
     * This method implements user registration with basic duplicate checking.
     *
     * IMPORTANT BUG: The method incorrectly uses user.getMobile() to check
     * for existing users by email. This is a significant logic error.
     *
     * @param user User entity containing registration data
     * @return User entity that was created and saved
     * @throws UserException if user already exists or validation fails
     *
     * Steps:
     * 1. Check if user already exists (currently buggy - uses mobile instead of email)
     * 2. If exists, throw exception
     * 3. Save user to database
     * 4. Return saved user
     *
     * ISSUES:
     * 1. Uses user.getMobile() instead of user.getEmail() for duplicate check
     * 2. No password hashing (security vulnerability)
     * 3. No data validation (relying on entity annotations only)
     * 4. No transaction management (@Transactional missing)
     * 5. Returns full User entity including password (security risk)
     */
    @Override
    public User createUser(User user) throws UserException {
        // BUG: Checking by mobile number instead of email
        // userRepository.findByEmail() expects an email, but we're passing mobile
        // This will always return null (unless mobile matches an email by coincidence)
        User registeredUser = userRepository.findByEmail(user.getMobile());

        // If a user is found (shouldn't happen due to bug), throw exception
        if (registeredUser != null) throw new UserException("User is already registered!");

        // Save user to database WITHOUT password hashing - MAJOR SECURITY RISK
        // Passwords should NEVER be stored in plain text
        return userRepository.save(user);
    }

    /**
     * Updates an existing user's information.
     *
     * This method is currently NOT IMPLEMENTED (returns null).
     * This violates the interface contract and will cause NullPointerException
     * if called.
     *
     * @param user User entity with updated information
     * @param key Authorization key/token (unconventional design)
     * @return null (not implemented)
     * @throws UserException (not currently thrown but should be)
     *
     * TODO: Implement user update functionality with:
     * 1. Authorization validation using the key
     * 2. Data validation
     * 3. Prevent updating certain fields (userId, password separately)
     * 4. Save updated user
     */
    @Override
    public User updateUser(User user, String key) throws UserException {
        // TODO: Implement update user functionality
        // Currently returns null, which will cause issues for callers
        return null;
    }

    /**
     * Retrieves all users from the system.
     *
     * This method returns all users. For production, consider adding
     * pagination to handle large datasets.
     *
     * @return List<User> containing all users
     * @throws UserException if no users found (controversial design choice)
     *
     * DESIGN ISSUE: Throwing an exception when no users exist is
     * unconventional. An empty list is a valid response for "no data".
     * Exceptions should be for exceptional/error conditions, not
     * for expected empty states.
     */
    @Override
    public List<User> getAllUsers() throws UserException {
        // Retrieve all users from database
        // userRepository.findAll() returns List<User>
        List<User> users = userRepository.findAll();

        // Controversial: Throwing exception for empty result
        // Many APIs return empty list for no data
        if (users.isEmpty()) {
            throw new UserException("No users found");
        }

        // SECURITY ISSUE: Returns full User objects including passwords
        // Should return DTOs without sensitive data
        return users;
    }

    /**
     * Deletes a user by their ID.
     *
     * This method performs hard delete (permanent removal).
     * Consider soft delete (mark as inactive) for data preservation.
     *
     * @param userId The unique identifier of the user to delete
     * @throws UserException if user not found
     *
     * Steps:
     * 1. Check if user exists
     * 2. If not, throw exception
     * 3. Delete user from database
     *
     * ISSUES:
     * 1. No authorization check (anyone can delete any user)
     * 2. No cascade handling (what about user's bookings?)
     * 3. No transaction management (@Transactional missing)
     */
    @Override
    public void deleteUserById(String userId) throws UserException {
        // Check if user exists before attempting deletion
        // userRepository.existsById() returns boolean
        if (!userRepository.existsById(userId)) {
            throw new UserException("User with ID " + userId + " does not exist");
        }

        // Perform hard delete (permanent removal)
        // This will fail if there are foreign key constraints (e.g., bookings referencing user)
        userRepository.deleteById(userId);
    }

    /**
     * Retrieves a specific user by their ID.
     *
     * This method fetches a single user by primary key.
     *
     * @param userId The unique identifier of the user to retrieve
     * @return User entity if found
     * @throws UserException if user not found
     *
     * Steps:
     * 1. Attempt to find user by ID
     * 2. If found, return user
     * 3. If not found, throw exception using orElseThrow
     *
     * ISSUES:
     * 1. Returns full User object including password (security risk)
     * 2. Consider returning Optional<User> instead of throwing exception
     */
    @Override
    public User getUserById(String userId) throws UserException {
        // userRepository.findById() returns Optional<User>
        // orElseThrow() throws UserException if user not found
        // Method reference for exception supplier: () -> new UserException(...)
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User with ID " + userId + " not found"));
    }

    // CRITICAL ISSUES TO FIX:

    /**
     * 1. SECURITY - Password Hashing:
     *    In createUser(), password must be hashed before saving:
     *
     *    @Override
     *    @Transactional
     *    public User createUser(User user) throws UserException {
     *        // Check by email, not mobile
     *        User existingUser = userRepository.findByEmail(user.getEmail());
     *        if (existingUser != null) {
     *            throw new UserException("Email already registered");
     *        }
     *
     *        // Hash password before saving
     *        String hashedPassword = passwordEncoder.encode(user.getPassword());
     *        user.setPassword(hashedPassword);
     *
     *        return userRepository.save(user);
     *    }
     *
     *    Requires: PasswordEncoder bean and dependency injection
     */

    /**
     * 2. FIX createUser() duplicate check:
     *    Currently checking by mobile instead of email.
     *    Should also check for duplicate username, mobile.
     */

    /**
     * 3. TRANSACTION MANAGEMENT:
     *    Add @Transactional to methods that modify data:
     *    - createUser, updateUser, deleteUserById
     *    - For read methods: @Transactional(readOnly = true)
     */

    /**
     * 4. RETURN DTOs INSTEAD OF ENTITIES:
     *    Create UserResponse DTO and return it instead of User entity
     *    to avoid exposing sensitive data (passwords).
     */

    /**
     * 5. IMPLEMENT updateUser() method:
     *    Currently returns null, needs proper implementation.
     */

    /**
     * 6. AUTHORIZATION:
     *    Add authorization checks for update and delete operations.
     */

    /**
     * 7. CONSIDER SOFT DELETE:
     *    Instead of hard delete, add 'active' field and deactivate users.
     */

    /**
     * 8. PAGINATION FOR getAllUsers():
     *    For production, use pagination to handle large datasets.
     */
}