// Package declaration: Groups exception handling classes together
// This follows the standard package naming convention and separates
// exception handling logic from other application components
package com.ratnakar.practice.TicketBookingAPI.exception;

// Spring Framework imports for HTTP status codes and response entities

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler class for centralized exception handling in Spring Boot application.
 *
 * @RestControllerAdvice annotation marks this class as a global exception handler that:
 * 1. Combines @ControllerAdvice (for global controller advice) and @ResponseBody (for returning response body)
 * 2. Allows centralized exception handling across all @RestController classes
 * 3. Eliminates the need for repetitive try-catch blocks in individual controllers
 * 4. Provides a single place to handle exceptions and return consistent error responses
 *
 * How it works in Spring Boot:
 * 1. When any controller throws an exception, Spring looks for a matching @ExceptionHandler
 * 2. If found in a @RestControllerAdvice class, it executes that handler method
 * 3. The handler method returns a ResponseEntity that becomes the HTTP response
 *
 * Benefits:
 * - Cleaner controller code without exception handling boilerplate
 * - Consistent error response format across entire API
 * - Separation of concerns: business logic vs error handling
 * - Easy to add new exception handlers in one place
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception handler method specifically for UserException.
     *
     * @ExceptionHandler annotation marks this method as an exception handler
     * for UserException and its subclasses. When any controller method throws
     * a UserException, Spring automatically routes it to this handler method.
     *
     * @param ex The UserException instance that was thrown, automatically
     *           injected by Spring. Contains the error message and potentially
     *           the cause of the exception.
     * @return ResponseEntity<String> - HTTP response with:
     *         1. Status code: 404 NOT FOUND (as defined by HttpStatus.NOT_FOUND)
     *         2. Body: The exception message (ex.getMessage())
     *
     * Why use HttpStatus.NOT_FOUND (404) for UserException?
     * - Typically, UserException is thrown when a user is not found or doesn't exist
     * - HTTP 404 is the standard status code for "resource not found"
     * - This helps API clients understand that the requested user resource doesn't exist
     *
     * Note: The status code should be chosen based on the specific error scenario.
     * For different types of user errors, you might want different status codes:
     * - 400 BAD_REQUEST for validation errors
     * - 409 CONFLICT for duplicate users
     * - 403 FORBIDDEN for authorization issues
     *
     * Example flow:
     * 1. UserController.getUserById("non-existent-id") throws UserException
     * 2. GlobalExceptionHandler.handleUserException() catches it
     * 3. Returns HTTP 404 with message "User not found with ID: non-existent-id"
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {
        // ResponseEntity.status() sets the HTTP status code
        // HttpStatus.NOT_FOUND is a constant representing HTTP 404
        // .body() sets the response body to the exception message
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Note: Additional exception handlers can be added here for other exception types

    // Example: Adding handler for BookingException
    // @ExceptionHandler(BookingException.class)
    // public ResponseEntity<String> handleBookingException(BookingException ex) {
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    // }

    // Example: Adding handler for generic Exception (catch-all)
    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<String> handleGenericException(Exception ex) {
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //             .body("An unexpected error occurred: " + ex.getMessage());
    // }

    // Example: Adding handler for validation exceptions
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<Map<String, String>> handleValidationExceptions(
    //         MethodArgumentNotValidException ex) {
    //     Map<String, String> errors = new HashMap<>();
    //     ex.getBindingResult().getFieldErrors().forEach(error ->
    //             errors.put(error.getField(), error.getDefaultMessage()));
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    // }

    /**
     * Best Practices for Global Exception Handling:
     * 1. Create specific exception handlers for different exception types
     * 2. Return appropriate HTTP status codes for each exception type
     * 3. Include meaningful error messages in response body
     * 4. Log exceptions for debugging (consider adding logger here)
     * 5. Consider creating a standardized error response format (like ErrorResponse class)
     * 6. Handle both checked and unchecked exceptions
     * 7. Test exception scenarios to ensure proper responses
     */
}