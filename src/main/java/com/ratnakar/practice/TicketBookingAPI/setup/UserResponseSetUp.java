// Package declaration: Organizes setup/utility classes together
// Setup classes typically handle configuration, response formatting, or other cross-cutting concerns
package com.ratnakar.practice.TicketBookingAPI.setup;

// Import entity and DTO classes
import com.ratnakar.practice.TicketBookingAPI.model.User;          // User entity
import com.ratnakar.practice.TicketBookingAPI.model.UserResponse;  // DTO for user responses
// Import repository for data access
import com.ratnakar.practice.TicketBookingAPI.repository.UserRepository;
// Import service for business logic
import com.ratnakar.practice.TicketBookingAPI.service.UserRegistrationService;
// Jakarta Persistence annotation (imported but not used - should be removed)
import jakarta.persistence.Entity;
// Spring Framework annotations for dependency injection and component management
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
// Spring HTTP status and response entity classes
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// Spring component annotations
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * UserResponseSetUp - Component for setting up user registration responses.
 *
 * @Component annotation marks this class as a Spring-managed component.
 * Components are generic Spring beans that don't fit into more specific
 * categories like @Service, @Repository, or @Controller.
 *
 * This class is responsible for formatting and structuring responses
 * for user registration operations. It acts as a response builder/factory.
 *
 * DESIGN PATTERN: This class follows the Builder/Facade pattern for
 * constructing consistent API responses. However, it mixes concerns:
 * 1. Data access (saving user)
 * 2. Business logic (checking if user exists)
 * 3. Response building
 *
 * Ideally, these concerns should be separated:
 * - Service layer: Business logic and data access
 * - Controller: Request handling
 * - Response builder: Response formatting
 */
@Component
public class UserResponseSetUp {

    /**
     * Dependency Injection of UserRepository.
     * @Autowired injects UserRepository for database operations.
     * Used to save user data and potentially query the database.
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Dependency Injection of UserRegistrationService.
     * @Autowired injects UserRegistrationService for checking if user exists.
     * Note: UserRegistrationService has a bug (checking by ID instead of username).
     */
    @Autowired
    UserRegistrationService userRegistrationService;

    /**
     * Builds and returns the response for user registration.
     *
     * This method handles the complete user registration flow including:
     * 1. Saving the user to database
     * 2. Checking if username already exists
     * 3. Building success or error response
     *
     * IMPORTANT DESIGN FLAWS:
     * 1. Saves user BEFORE checking if username exists (race condition and data inconsistency)
     * 2. Uses buggy UserRegistrationService.checkUserAlreadyExists()
     * 3. Returns user ID before verifying registration succeeded
     * 4. Mixed concerns (data access, validation, response building)
     *
     * @param user User entity containing registration data
     * @return ResponseEntity<UserResponse> HTTP response with appropriate status and body
     *
     * The method flow:
     * 1. Saves user immediately (problematic - should validate first)
     * 2. Extracts user data
     * 3. Checks if username exists (after already saving!)
     * 4. Returns success or failure response
     */
    public ResponseEntity<UserResponse> userRegistrationResponse(User user) {
        // Create a new UserResponse instance for each request
        // This ensures thread safety and fresh state for each response
        UserResponse userResponse = new UserResponse(); // create new instance each time

        // CRITICAL BUG: Saves user BEFORE checking if username already exists
        // This will create duplicate users even if validation fails
        // Should validate first, then save only if validation passes
        User savedUser = userRepository.save(user);

        // Extract user data for response (these values come from the unsaved 'user' object)
        // Note: Using getters on the original 'user' object, not the 'savedUser'
        String uniqueName = user.getUserName();      // Username from request
        String uniqueId = user.getUserID();          // User ID from request (should be null/generated)
        String uniqueFirstName = user.getFirstName(); // First name from request
        String uniqueLastName = user.getLastName();   // Last name from request

        // Check if user already exists by username
        // Uses UserRegistrationService.checkUserAlreadyExists() which has a bug:
        // It checks by ID, not by username (method name is misleading)
        // Also, this check happens AFTER saving the user, which is wrong
        if (!userRegistrationService.checkUserAlreadyExists(uniqueName)) {
            // Success case: User doesn't exist (according to buggy check)
            userResponse.setMsg("New User Added Successfully");
            userResponse.setUserName(uniqueName);

            // BUG: savedUser.getUserID() is called but not assigned to anything
            // This line does nothing - it just calls the method and ignores the return value
            savedUser.getUserID();

            // Set the response fields (using values from original 'user', not 'savedUser')
            userResponse.setUserID(uniqueId);          // Might be null if not generated yet
            userResponse.setFirstName(uniqueFirstName);
            userResponse.setLastName(uniqueLastName);

            // Return HTTP 201 Created for successful resource creation
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } else {
            // Error case: Username already exists (but user was already saved!)
            userResponse.setMsg("UserName Already Exists, Please Enter Unique UserName");
            userResponse.setUserName(uniqueName);

            // Return HTTP 417 Expectation Failed
            // This is an unusual status code for this scenario. Better choices:
            // - 409 Conflict: Resource conflict (duplicate user)
            // - 400 Bad Request: Invalid input (duplicate username)
            return new ResponseEntity<>(userResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }

    // CRITICAL ISSUES THAT NEED FIXING:

    /**
     * MAJOR BUG 1: Saves user before validation
     * ==========================================
     * The method saves the user with userRepository.save(user) BEFORE
     * checking if the username already exists. This means:
     * 1. Duplicate users will be created in the database
     * 2. The validation check is useless (closing the barn door after the horse has bolted)
     *
     * FIX: Validate first, then save only if validation passes:
     *
     *   // 1. Validate input
     *   if (userRegistrationService.checkUserAlreadyExists(uniqueName)) {
     *       // Return error immediately
     *       userResponse.setMsg("Username already exists");
     *       return new ResponseEntity<>(userResponse, HttpStatus.CONFLICT);
     *   }
     *
     *   // 2. Save only if validation passes
     *   User savedUser = userRepository.save(user);
     */

    /**
     * MAJOR BUG 2: Uses buggy validation service
     * ===========================================
     * UserRegistrationService.checkUserAlreadyExists() has a bug:
     * It checks by user ID (findById) instead of by username.
     *
     * FIX: Either fix the service method or check directly in repository:
     *
     *   // Option 1: Add method to UserRepository
     *   // In UserRepository: Optional<User> findByUserName(String userName);
     *
     *   // Option 2: Check by username directly
     *   boolean usernameExists = userRepository.findByUserName(uniqueName).isPresent();
     */

    /**
     * BUG 3: Unused method call
     * ==========================
     * The line `savedUser.getUserID();` does nothing.
     * It calls the getter but doesn't use the return value.
     *
     * FIX: Remove or use the return value:
     *   userResponse.setUserID(savedUser.getUserID());
     */

    /**
     * BUG 4: Using original user instead of saved user
     * =================================================
     * The response uses values from the original 'user' object
     * (uniqueId, uniqueFirstName, uniqueLastName) instead of from
     * the 'savedUser' object. This matters because:
     * 1. The saved user has a generated ID (if using UUID or auto-increment)
     * 2. The saved user might have default values or transformations applied
     *
     * FIX: Use the savedUser object for response data:
     *   userResponse.setUserID(savedUser.getUserID());
     *   userResponse.setFirstName(savedUser.getFirstName());
     *   // etc.
     */

    /**
     * BUG 5: Incorrect HTTP status code
     * ==================================
     * Using HTTP 417 Expectation Failed for duplicate user is non-standard.
     * Standard status codes for this scenario:
     * - 409 Conflict: When request conflicts with current state (duplicate)
     * - 400 Bad Request: When client sends invalid data (duplicate username)
     *
     * FIX: Use appropriate status code:
     *   return new ResponseEntity<>(userResponse, HttpStatus.CONFLICT);
     */

    /**
     * DESIGN IMPROVEMENT: Separate concerns
     * ======================================
     * This method does too much:
     * 1. Saves data (repository concern)
     * 2. Validates business rules (service concern)
     * 3. Builds responses (presentation concern)
     *
     * BETTER DESIGN:
     * 1. Move user saving and validation to UserService
     * 2. Keep only response building in this class
     * 3. Or eliminate this class and handle responses in controller
     *
     * Example:
     *
     * @Service
     * public class UserService {
     *     public User registerUser(User user) throws UserException {
     *         // Validate, save, return saved user
     *     }
     * }
     *
     * @RestController
     * public class UserController {
     *     public ResponseEntity<UserResponse> registerUser(@RequestBody User user) {
     *         try {
     *             User savedUser = userService.registerUser(user);
     *             UserResponse response = createSuccessResponse(savedUser);
     *             return ResponseEntity.status(HttpStatus.CREATED).body(response);
     *         } catch (UserException e) {
     *             UserResponse response = createErrorResponse(e.getMessage());
     *             return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
     *         }
     *     }
     * }
     */

    /**
     * SECURITY ISSUE: No password hashing
     * ====================================
     * The user is saved with plain text password.
     * This is a MAJOR security vulnerability.
     *
     * FIX: Hash password before saving:
     *   // In UserService or before saving
     *   String hashedPassword = passwordEncoder.encode(user.getPassword());
     *   user.setPassword(hashedPassword);
     *   User savedUser = userRepository.save(user);
     */
}