// Package declaration: Organizes the exception class in the exception package
// This follows standard Java package structure where all custom exceptions
// are grouped together for better organization and maintainability
package com.ratnakar.practice.TicketBookingAPI.exception;

/**
 * Custom checked exception for handling user-related errors in the application.
 *
 * This class extends Exception, which makes it a CHECKED exception.
 *
 * IMPORTANT DISTINCTION: In Java, there are two main types of exceptions:
 * 1. Checked exceptions (extends Exception) - Must be declared with 'throws' 
 *    in method signature or handled with try-catch blocks
 * 2. Unchecked exceptions (extends RuntimeException) - Don't need declaration
 *    or explicit handling
 *
 * Why extend Exception (checked) instead of RuntimeException (unchecked)?
 * 1. Design Decision: When the developer wants to force callers to handle or
 *    declare this specific exception, making the error handling explicit
 * 2. Business Logic: User-related errors (like user not found, duplicate user,
 *    invalid user data) might be considered recoverable scenarios where the
 *    caller should explicitly handle the situation
 * 3. API Contract: Makes it clear in the method signature that this method
 *    can throw user-related exceptions
 *
 * However, note that in Spring applications, it's common to use unchecked
 * exceptions (RuntimeException) because:
 * - Spring can translate them to appropriate HTTP status codes
 * - They reduce boilerplate code in service and controller layers
 * - They're often used with @ControllerAdvice for global exception handling
 *
 * Since this extends Exception (checked), any method that throws UserException
 * must declare it with 'throws UserException' or handle it with try-catch.
 *
 * This exception would be thrown in scenarios such as:
 * - User not found when trying to fetch by ID
 * - Duplicate user registration (email/username already exists)
 * - Invalid user data (age below minimum, invalid email format, etc.)
 * - Database constraint violations related to user operations
 *
 * Example usage in service layer:
 *   public User getUserById(String id) throws UserException {
 *       User user = userRepository.findById(id);
 *       if (user == null) {
 *           throw new UserException("User not found with ID: " + id);
 *       }
 *       return user;
 *   }
 */
public class UserException extends Exception {

    /**
     * Constructor for creating a UserException with a custom error message.
     *
     * @param message The descriptive error message that explains what went wrong.
     *                This message should be user-friendly and helpful for debugging.
     *                It will be stored in the exception and can be retrieved
     *                using the getMessage() method inherited from Exception class.
     *
     * How this works in Spring Boot with GlobalExceptionHandler:
     * 1. When a method throws UserException, it must be caught or declared
     * 2. In the controller, we declare 'throws UserException' in method signature
     * 3. When thrown, the GlobalExceptionHandler (with @ExceptionHandler) catches it
     * 4. The handler converts it to an HTTP response (like 404 NOT FOUND)
     * 5. The message is included in the response body for client feedback
     *
     * Note: Because this is a checked exception, the calling code has two options:
     * Option 1: Declare it in method signature
     *   public void someMethod() throws UserException { ... }
     *
     * Option 2: Handle it with try-catch
     *   try {
     *       userService.someOperation();
     *   } catch (UserException e) {
     *       // Handle the exception
     *   }
     */
    public UserException(String message) {
        // Call the parent class (Exception) constructor with the message
        // This initializes the exception with the provided error message
        // The super() call is required because Exception doesn't have a default constructor
        super(message);
    }

    // Note: We could add additional constructors for more flexibility:

    /**
     * Example of constructor with message and cause (exception chaining):
     * This is useful when wrapping another exception (like SQLException)
     *
     * public UserException(String message, Throwable cause) {
     *     super(message, cause);
     * }
     *
     * This preserves the original exception stack trace, which is helpful for debugging.
     */

    /**
     * Example of constructor with just cause:
     * This converts another exception to UserException
     *
     * public UserException(Throwable cause) {
     *     super(cause);
     * }
     */

    /**
     * Design Consideration:
     * If you find yourself frequently needing to wrap other exceptions,
     * or if you want to preserve the original exception cause for logging,
     * consider adding the constructors above.
     *
     * The current implementation with just the message constructor is
     * often sufficient for simple error scenarios where the cause
     * is known and doesn't need to be wrapped.
     */
}