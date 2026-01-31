// Package declaration: Groups related exception classes together
// Follows standard Java package naming convention (reverse domain name)
// Exception classes are typically placed in an 'exception' package to organize
// all custom exceptions in one place
package com.ratnakar.practice.TicketBookingAPI.exception;

/**
 * Custom unchecked exception for handling booking-related errors in the application.
 *
 * This class extends RuntimeException, which means it's an unchecked exception.
 *
 * IMPORTANT: In Java, there are two types of exceptions:
 * 1. Checked exceptions (extend Exception) - Must be declared in method signature or handled
 * 2. Unchecked exceptions (extend RuntimeException) - Don't need to be declared or handled
 *
 * Why extend RuntimeException instead of Exception?
 * - In Spring applications, unchecked exceptions are often preferred because:
 *   1. They reduce boilerplate code (no need for throws declarations everywhere)
 *   2. Spring can translate them to appropriate HTTP status codes using @ControllerAdvice
 *   3. They're suitable for errors that clients can't reasonably recover from
 *   4. They follow the "fail-fast" principle
 *
 * This exception would be thrown when booking operations fail, such as:
 * - Trying to book tickets when no seats are available
 * - Attempting to update or cancel a non-existent booking
 * - Invalid booking data (like negative number of tickets)
 * - Database errors during booking operations
 *
 * Example usage in service layer:
 *   throw new BookingException("No seats available for movie: " + movieName);
 */
public class BookingException extends RuntimeException {

    /**
     * Constructor for creating a BookingException with a custom error message.
     *
     * @param message The detailed error message describing what went wrong.
     *                This message should be meaningful and helpful for debugging.
     *                It will be passed to the parent RuntimeException class
     *                and can be retrieved later using getMessage() method.
     *
     * How this works in Spring Boot:
     * 1. When this exception is thrown from a controller or service,
     * 2. Spring's exception handling mechanism (@ControllerAdvice or @ExceptionHandler) catches it
     * 3. It can be converted to an appropriate HTTP response (like 400 Bad Request or 404 Not Found)
     * 4. The message can be included in the response body for client feedback
     */
    public BookingException(String message) {
        // Call the parent class (RuntimeException) constructor with the message
        // This initializes the exception with the provided error message
        // The super() call is mandatory as the parent class doesn't have a default constructor
        super(message);
    }

    // Note: We could add more constructors if needed, for example:
    // 1. Constructor with message and cause (for exception chaining)
    //    public BookingException(String message, Throwable cause) { super(message, cause); }
    // 2. Constructor with just cause
    //    public BookingException(Throwable cause) { super(cause); }

    // Why not add these additional constructors?
    // The current implementation is minimal and sufficient for most use cases.
    // Additional constructors can be added as the application requirements grow.
}