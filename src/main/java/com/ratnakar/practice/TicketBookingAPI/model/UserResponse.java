// Package declaration: Organizes the model classes together
// This class is a DTO (Data Transfer Object) for user API responses
package com.ratnakar.practice.TicketBookingAPI.model;

/**
 * UserResponse - Data Transfer Object for user API responses.
 *
 * This is a DTO (Data Transfer Object) used to send user information
 * in API responses. It provides a controlled, limited view of user data.
 *
 * IMPORTANT SECURITY AND DESIGN PRINCIPLES:
 * 1. Never expose sensitive data (passwords, tokens, etc.) in responses
 * 2. Only return data that the client needs (principle of least privilege)
 * 3. Use different DTOs for different operations (registration, login, profile)
 *
 * This class is typically used for responses after:
 * - User registration (returns user ID and success message)
 * - User profile retrieval (returns user details without password)
 * - User update operations (returns updated user information)
 *
 * Note: This class does NOT include:
 * - Password (sensitive, never exposed in responses)
 * - Mobile number (might be considered sensitive in some applications)
 * - Email (sometimes excluded for privacy, but often included)
 *
 * Why not return the User entity directly?
 * 1. Security: User entity contains password and other sensitive fields
 * 2. Control: Can choose which fields to expose for each endpoint
 * 3. Performance: Avoid loading unnecessary relationships
 * 4. Stability: API response format doesn't change if database schema changes
 */
public class UserResponse {

    /**
     * Message field for sending success/error messages.
     * This provides feedback to the client about the operation result.
     * Examples: "Registration successful", "User not found", "Update failed"
     */
    private String msg;

    /**
     * User ID - unique identifier for the user.
     * This is the same UUID that's stored in the database.
     * Important to return this so clients can reference the user in future requests.
     */
    private String userID;

    /**
     * User's first name.
     * Personal information that's typically safe to expose.
     */
    private String firstName;

    /**
     * User's last name.
     * Personal information that's typically safe to expose.
     */
    private String lastName;

    /**
     * Username - the identifier used for login.
     * This might be the same as email or a separate handle.
     */
    private String userName;

    // Getter and Setter methods
    // These are manually written without Lombok annotation.
    // This provides full control over the methods and avoids dependency on Lombok.

    /**
     * Getter for username.
     * @return String username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter for username.
     * @param userName String username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter for first name.
     * @return String first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for first name.
     * @param firstName String first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for last name.
     * @return String last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for last name.
     * @param lastName String last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for user ID.
     * Note: Method name getUserID() but field is userID (camelCase).
     * @return String user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Setter for user ID.
     * @param userID String user ID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Getter for message.
     * @return String message
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Setter for message.
     * @param msg String message to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    // Note: Additional useful methods that could be added:

    /**
     * Builder pattern implementation (useful for creating complex objects):
     * public static UserResponseBuilder builder() {
     *     return new UserResponseBuilder();
     * }
     *
     * // Inner builder class
     * public static class UserResponseBuilder {
     *     private UserResponse response = new UserResponse();
     *
     *     public UserResponseBuilder msg(String msg) {
     *         response.setMsg(msg);
     *         return this;
     *     }
     *
     *     // ... other builder methods
     *
     *     public UserResponse build() {
     *         return response;
     *     }
     * }
     */

    /**
     * Static factory method from User entity (common pattern):
     *
     * public static UserResponse fromUser(User user) {
     *     UserResponse response = new UserResponse();
     *     response.setUserID(user.getUserId());
     *     response.setFirstName(user.getFirstName());
     *     response.setLastName(user.getLastName());
     *     response.setUserName(user.getUserName());
     *     response.setMsg("User details retrieved successfully");
     *     return response;
     * }
     */

    /**
     * toString() method for debugging (recommended):
     *
     * @Override
     * public String toString() {
     *     return "UserResponse{" +
     *             "msg='" + msg + '\'' +
     *             ", userID='" + userID + '\'' +
     *             ", firstName='" + firstName + '\'' +
     *             ", lastName='" + lastName + '\'' +
     *             ", userName='" + userName + '\'' +
     *             '}';
     * }
     */

    /**
     * equals() and hashCode() methods (if needed for collections):
     *
     * These are important if UserResponse objects will be:
     * - Stored in Sets or used as Map keys
     * - Compared for equality in tests
     */

    /**
     * Constructor options:
     *
     * 1. No-argument constructor (implicitly available)
     *    Required for JSON deserialization by Spring
     *
     * 2. All-argument constructor (optional but useful)
     *    public UserResponse(String msg, String userID, String firstName, 
     *                        String lastName, String userName) {
     *        this.msg = msg;
     *        this.userID = userID;
     *        this.firstName = firstName;
     *        this.lastName = lastName;
     *        this.userName = userName;
     *    }
     *
     * 3. Constructor from User entity (see static factory method above)
     */

    /**
     * Best Practices for Response DTOs:
     *
     * 1. Immutable vs Mutable:
     *    - Current: Mutable (has setters)
     *    - Alternative: Make it immutable (remove setters, use constructor)
     *
     * 2. Validation: Add validation annotations if this DTO is used for requests too
     *
     * 3. Documentation: Add field descriptions for API documentation (Swagger/OpenAPI)
     *
     * 4. Consistency: Use same DTO structure across all user endpoints
     *
     * 5. Versioning: Consider versioning if API response format might change
     */
}