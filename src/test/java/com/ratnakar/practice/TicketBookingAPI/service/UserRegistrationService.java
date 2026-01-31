// Package declaration: Organizes service layer classes together
// Service classes contain business logic and are part of the service layer
package com.ratnakar.practice.TicketBookingAPI.service;

// Import custom exception for user-related errors
// Import User entity class

import com.ratnakar.practice.TicketBookingAPI.model.User;
import com.ratnakar.practice.TicketBookingAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserRegistrationService - Service class for user registration business logic.
 *
 * @Service annotation marks this class as a Spring Service bean.
 * Service beans contain business logic and are typically used to:
 * 1. Implement business rules and validation
 * 2. Coordinate between repositories and controllers
 * 3. Handle transactions
 * 4. Perform data transformation
 *
 * Note: The class name suggests it handles user registration, but it
 * currently only has a method to check if a user exists. This might be
 * part of a larger registration process, or the class might need
 * additional methods for complete registration functionality.
 *
 * IMPORTANT DESIGN ISSUE: This class has a mix of annotations.
 * It's annotated with both @Service and also imports @Component.
 * @Service is a specialization of @Component, so using just @Service is sufficient.
 * The unused import of @Component should be removed.
 */
@Service
public class UserRegistrationService {

    /**
     * Dependency Injection of UserRepository.
     * @Autowired tells Spring to inject an instance of UserRepository.
     * UserRepository is a Spring Data JPA interface that provides
     * database operations for User entities.
     *
     * Using field injection here. Alternatives:
     * 1. Constructor injection (recommended for required dependencies)
     * 2. Setter injection (for optional dependencies)
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Checks if a user already exists in the system by username.
     *
     * This method is used during registration to prevent duplicate users.
     * It queries the database to check if a user with the given username
     * already exists.
     *
     * IMPORTANT BUG/ISSUE: The method parameter is named 'userName' (String),
     * but the method uses userRepository.findById(userName) which expects
     * a user ID (String/UUID), not a username.
     *
     * In the User entity:
     * - Primary key is 'userId' (String, UUID generated)
     * - There's also a 'userName' field (String, separate from primary key)
     *
     * The method is incorrectly checking by ID when it should check by username.
     * This is a significant bug that needs to be fixed.
     *
     * @param userName The username to check for existence
     * @return boolean - true if user exists, false if not
     *
     * Current incorrect behavior:
     * - Treats 'userName' parameter as if it's a user ID
     * - Uses findById() which searches by primary key (userId), not username
     * - Will return incorrect results
     *
     * Should be fixed to either:
     * 1. Check by username (if that's what's intended):
     *    public boolean checkUserAlreadyExists(String userName) {
     *        return userRepository.findByUserName(userName) != null;
     *    }
     *    (Requires adding findByUserName method in UserRepository)
     *
     * 2. Rename method to checkUserAlreadyExistsById if checking by ID
     *    public boolean checkUserAlreadyExistsById(String userId) {
     *        return userRepository.findById(userId).isPresent();
     *    }
     */
    public boolean checkUserAlreadyExists(String userName){
        // userRepository.findById() returns Optional<User>
        // Optional is a container that may or may not contain a non-null value
        // This method expects a user ID (primary key), not a username

        // BUG: This is searching by ID, not by username
        // The parameter 'userName' is being used as if it's a user ID
        Optional<User> uname = userRepository.findById(userName);

        // Optional.isPresent() returns true if a value is present
        // This checks if the Optional contains a User object
        if(uname.isPresent())
            return true;  // User exists with this ID (not username)
        else
            return false; // No user found with this ID (not username)
    }

    // Note: The method can be simplified using method reference:
    // return userRepository.findById(userName).isPresent();

    // Note: Additional methods that should be in a UserRegistrationService:

    /**
     * Complete user registration method:
     *
     * @Transactional
     * public User registerUser(User user) throws UserException {
     *     // 1. Validate user data (email format, password strength, etc.)
     *     // 2. Check if user already exists (by email or username)
     *     // 3. Hash password before saving
     *     // 4. Save user to database
     *     // 5. Send confirmation email
     *     // 6. Return saved user (without password)
     * }
     */

    /**
     * User validation method:
     *
     * private void validateUserData(User user) throws UserException {
     *     if (user.getEmail() == null || user.getEmail().isEmpty()) {
     *         throw new UserException("Email is required");
     *     }
     *     // More validation...
     * }
     */

    /**
     * Password hashing method (CRITICAL for security):
     *
     * private String hashPassword(String plainPassword) {
     *     // Use BCrypt or similar hashing algorithm
     *     // NEVER store plain text passwords
     *     return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
     * }
     */

    /**
     * Check if email already exists:
     *
     * public boolean isEmailTaken(String email) {
     *     User user = userRepository.findByEmail(email);
     *     return user != null;
     * }
     */

    /**
     * FIXING THE BUG:
     *
     * Option 1: Fix method to check by username (requires adding method to repository)
     *   Step 1: Add method to UserRepository:
     *     Optional<User> findByUserName(String userName);
     *
     *   Step 2: Update this method:
     *     public boolean checkUserAlreadyExists(String userName) {
     *         return userRepository.findByUserName(userName).isPresent();
     *     }
     *
     * Option 2: Rename method to check by ID
     *   public boolean checkUserExistsById(String userId) {
     *       return userRepository.findById(userId).isPresent();
     *   }
     *
     * Option 3: Create separate methods for both checks
     *   public boolean checkUserExistsById(String userId) {
     *       return userRepository.findById(userId).isPresent();
     *   }
     *
     *   public boolean checkUserExistsByUsername(String userName) {
     *       return userRepository.findByUserName(userName).isPresent();
     *   }
     */

    /**
     * TRANSACTION MANAGEMENT:
     * This service currently doesn't have @Transactional methods.
     * For methods that modify data (save, update, delete), add @Transactional.
     *
     * SECURITY CONSIDERATIONS:
     * 1. Always hash passwords (NEVER store plain text)
     * 2. Validate all user inputs
     * 3. Consider email verification for registration
     * 4. Implement rate limiting to prevent abuse
     *
     * BEST PRACTICES:
     * 1. Use constructor injection instead of field injection
     * 2. Add comprehensive logging
     * 3. Use specific exception types (not generic Exception)
     * 4. Follow single responsibility principle
     */
}