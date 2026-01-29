// Package declaration: Organizes service layer interfaces together
// Service interfaces define the contract for business operations
package com.ratnakar.practice.TicketBookingAPI.service;

// Import custom exception for user-related errors
import com.ratnakar.practice.TicketBookingAPI.exception.UserException;
// Import User entity class
import com.ratnakar.practice.TicketBookingAPI.model.User;

// Import Java Collections Framework for List return type
import java.util.List;

/**
 * UserService - Service layer interface for user management operations.
 *
 * This interface defines the contract for all user-related business operations.
 * It follows the INTERFACE SEGREGATION PRINCIPLE by providing a focused
 * set of methods for user management.
 *
 * SERVICE LAYER PURPOSE:
 * 1. Defines business operations separate from data access and presentation layers
 * 2. Provides a clean API for user management
 * 3. Allows multiple implementations (e.g., for testing, different environments)
 * 4. Enables Spring's dependency injection and AOP features (@Transactional, caching)
 *
 * This interface will be implemented by a concrete class (UserServiceImpl)
 * annotated with @Service. Spring will create a proxy for transaction management
 * and dependency injection.
 *
 * All methods declare UserException (checked exception) to enforce
 * proper error handling by callers. This is a design choice - alternatively,
 * could use unchecked exceptions (RuntimeException) for less boilerplate.
 */
public interface UserService {

    /**
     * Creates a new user in the system.
     *
     * This method handles the complete user registration process including:
     * - Data validation (email format, password strength, etc.)
     * - Duplicate checking (email, username, mobile)
     * - Password hashing (security critical)
     * - Saving to database
     * - Sending welcome/verification email
     *
     * @param user User entity containing registration data.
     *             Should include all required fields: userName, firstName,
     *             email, mobile, password, etc.
     *             Password should be in plain text (will be hashed in implementation).
     * @return User entity that was created and saved to database.
     *         Typically returns the user with generated ID but without password.
     * @throws UserException if:
     *         - User data validation fails (invalid email, weak password, etc.)
     *         - User already exists (duplicate email, username, or mobile)
     *         - Database error occurs during save
     *
     * Note: The returned User object might have the password field cleared
     * or hashed for security. Best practice is to return a UserResponse DTO
     * instead of the entity to avoid exposing sensitive data.
     */
    public User createUser(User user) throws UserException;

    /**
     * Updates an existing user's information.
     *
     * This method allows updating user profile information. Typically includes:
     * - Authorization check (key/token validation)
     * - Validation of updated data
     * - Preventing updates to certain fields (like user ID)
     * - Saving updated information
     *
     * @param user User entity with updated information.
     *             Should contain the updated fields (firstName, lastName, etc.).
     *             The user ID should remain unchanged.
     * @param key Authorization key/token to verify the user has permission
     *            to perform the update. This could be:
     *            - Session token
     *            - JWT token
     *            - API key
     *            - User ID (if passed separately for verification)
     * @return Updated User entity after changes are saved.
     * @throws UserException if:
     *         - User not found
     *         - Authorization failed (invalid key)
     *         - Validation fails (invalid updated data)
     *         - Attempt to update restricted fields (like user ID)
     *
     * Note: The 'key' parameter design is unconventional. Typically,
     * authorization is handled via Spring Security or token in request headers.
     * This design might indicate a custom authorization mechanism.
     */
    public User updateUser(User user, String key) throws UserException;

    /**
     * Retrieves all users from the system.
     *
     * This method returns a list of all registered users. Should be used
     * with caution in production due to potential performance issues with
     * large datasets. Consider adding pagination for production use.
     *
     * SECURITY NOTE: This method should return minimal user information
     * (exclude passwords, sensitive data) or use a DTO (UserResponse).
     *
     * @return List<User> containing all users in the system.
     *         Returns empty list if no users exist.
     * @throws UserException if:
     *         - Database access error occurs
     *         - Permission/authorization error (if added later)
     *
     * Recommendation: Add pagination parameters:
     * List<User> getAllUsers(int page, int size) throws UserException;
     * Or use Page<User> return type with Pageable parameter.
     */
    List<User> getAllUsers() throws UserException;

    /**
     * Deletes a user by their ID.
     *
     * This method permanently removes a user from the system. Typically includes:
     * - Authorization check (admin or user themselves)
     * - Cascade deletion of related data (bookings, etc.)
     * - Cleanup of user sessions/tokens
     *
     * @param userId The unique identifier (UUID) of the user to delete.
     * @throws UserException if:
     *         - User not found with the given ID
     *         - Authorization/permission error
     *         - Database constraint violation (if user has active bookings)
     *         - Database error during deletion
     *
     * Note: Consider implementing soft delete (mark as inactive) instead of
     * hard delete to preserve data integrity and history.
     */
    public void deleteUserById(String userId) throws UserException;

    /**
     * Retrieves a specific user by their ID.
     *
     * This method fetches a single user's details. Commonly used for:
     * - User profile display
     * - Admin viewing user details
     * - User information updates
     *
     * @param userId The unique identifier (UUID) of the user to retrieve.
     * @return User entity with the matching ID.
     * @throws UserException if:
     *         - User not found with the given ID
     *         - Database access error
     *
     * SECURITY NOTE: Should exclude sensitive fields like password.
     * Consider returning a UserResponse DTO instead of User entity.
     *
     * Performance: Ensure the user ID is indexed in the database for fast lookup.
     */
    User getUserById(String userId) throws UserException;

    // Note: Additional methods that could be useful:

    /**
     * Get user by email (for login/forgot password):
     * User getUserByEmail(String email) throws UserException;
     *
     * Search users by name (for admin panel):
     * List<User> searchUsersByName(String name) throws UserException;
     *
     * Change password:
     * void changePassword(String userId, String oldPassword, String newPassword) throws UserException;
     *
     * Reset password (forgot password flow):
     * void resetPassword(String email) throws UserException;
     *
     * Verify email:
     * void verifyEmail(String token) throws UserException;
     */

    /**
     * DESIGN PATTERNS AND SPRING INTEGRATION:
     *
     * 1. Template Method Pattern: Interface defines operations, implementation provides details
     * 2. Dependency Injection: Implementation will be injected where this interface is used
     * 3. Proxy Pattern: Spring creates proxies for transaction management and AOP
     *
     * TRANSACTION MANAGEMENT:
     * Implementation methods should be annotated with @Transactional for:
     * - createUser, updateUser, deleteUserById (write operations)
     * - getAllUsers, getUserById (read-only operations with readOnly=true)
     *
     * SECURITY CONSIDERATIONS:
     * 1. Never return passwords in any method
     * 2. Validate all inputs
     * 3. Implement proper authorization checks
     * 4. Use parameterized queries to prevent SQL injection
     * 5. Consider rate limiting for sensitive operations
     */
}