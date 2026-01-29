// Package declaration: Groups related controller classes together
// Follows standard Java package naming convention (reverse domain name)
package com.ratnakar.practice.TicketBookingAPI.controller;

// Import statements: Required classes from various packages

// Custom exception class for user-related errors
import com.ratnakar.practice.TicketBookingAPI.exception.UserException;
// User model/entity class representing user data
import com.ratnakar.practice.TicketBookingAPI.model.User;
// Service class containing business logic for user operations
import com.ratnakar.practice.TicketBookingAPI.service.UserService;
// Helper/setup class for creating user registration responses
import com.ratnakar.practice.TicketBookingAPI.setup.UserResponseSetUp;
// Jakarta Validation API: @Valid annotation for request validation
// Part of Java EE (now Jakarta EE) for bean validation
import jakarta.validation.Valid;
// Spring Framework annotation for dependency injection
import org.springframework.beans.factory.annotation.Autowired;
// ResponseEntity: Spring class representing HTTP response (status, headers, body)
// Comes from org.springframework.http package
import org.springframework.http.ResponseEntity;
// Spring Web annotations for creating REST endpoints and handling HTTP methods
import org.springframework.web.bind.annotation.*;

// Java Collections Framework: List interface for collections
import java.util.List;

/**
 * REST Controller for handling user-related operations.
 *
 * @RestController annotation marks this class as a Spring MVC Controller
 * where each method returns data that will be written directly to the
 * HTTP response body (typically as JSON). This annotation is a combination
 * of @Controller and @ResponseBody. It's used for building RESTful web
 * services where we return data rather than view templates.
 */
@RestController
public class UserController {

    /**
     * Dependency Injection of UserService using @Autowired.
     * @Autowired tells Spring to automatically inject an instance of
     * UserService into this field. Spring looks for a bean of type
     * UserService in its application context (created via @Service annotation
     * on the service class) and injects it here. This is field injection,
     * one of three types of dependency injection in Spring (constructor,
     * setter, and field injection).
     */
    @Autowired
    private UserService userService;

    /**
     * Dependency Injection of UserResponseSetUp.
     * This is a helper/setup class that likely handles formatting or
     * structuring responses for user registration. Using a separate class
     * for response setup follows the Single Responsibility Principle and
     * separates concerns.
     */
    @Autowired
    UserResponseSetUp userResponseSetUp;

    /**
     * Endpoint for registering a new user (CREATE operation).
     *
     * @PostMapping annotation maps HTTP POST requests to "/api/users/register"
     * to this method. POST is used for creating new resources in REST.
     * The endpoint path "/api/users/register" indicates this is part of
     * the user API for registration.
     *
     * @param user User object automatically deserialized from JSON request body.
     *             @RequestBody annotation tells Spring to convert the incoming
     *             JSON to a User object. @Valid annotation triggers validation
     *             on the User object based on annotations in the User class
     *             (like @NotNull, @Size, etc.). If validation fails, Spring
     *             throws a MethodArgumentNotValidException.
     * @return ResponseEntity without a generic type (raw type) - the actual
     *         return type is determined by userResponseSetUp.userRegistrationResponse().
     *         Typically returns HTTP 201 Created for successful registration
     *         or appropriate error status.
     * @throws UserException Custom exception for user-related errors (like
     *         duplicate user, validation failures, etc.)
     */
    @PostMapping("/api/users/register")
    public ResponseEntity registerUser(@Valid @RequestBody User user) throws UserException {
        // Delegate the response creation to UserResponseSetUp class
        // This separates the response formatting logic from the controller
        return userResponseSetUp.userRegistrationResponse(user);
    }

    /**
     * Endpoint for retrieving all users (READ operation).
     *
     * @GetMapping annotation maps HTTP GET requests to "/api/users" to this method.
     * GET is used for retrieving resources in REST. This endpoint returns
     * a list of all registered users.
     *
     * @return ResponseEntity<List<User>> - HTTP 200 OK response containing
     *         a list of User objects in the response body. Spring automatically
     *         converts the List<User> to JSON format.
     * @throws UserException Custom exception if something goes wrong while
     *         fetching users (like database connection issues)
     */
    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getAllUsers() throws UserException {
        // Call service layer to get all users from database
        List<User> users = userService.getAllUsers();
        // Return HTTP 200 OK with the list of users
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint for deleting a user by ID (DELETE operation).
     *
     * @DeleteMapping annotation maps HTTP DELETE requests to "/api/users/delete/{id}"
     * to this method. The {id} in the path is a path variable that will be
     * replaced with the actual user ID. DELETE is used for removing resources.
     *
     * @param userId Path variable extracted from the URL. The @PathVariable
     *               annotation binds the {id} from the URL to the userId parameter.
     *               The ("id") specifies the name of the path variable to match.
     *               This is useful when the method parameter name differs from
     *               the path variable name.
     * @return ResponseEntity<String> - HTTP 200 OK with a success message
     *         in the response body.
     * @throws UserException Custom exception if user not found or deletion fails
     */
    @DeleteMapping("/api/users/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") String userId) throws UserException {
        // Call service layer to delete the user by ID
        userService.deleteUserById(userId);
        // Return HTTP 200 OK with a success message
        return ResponseEntity.ok("User with ID " + userId + " deleted successfully");
    }

    /**
     * Endpoint for retrieving a specific user by ID (READ operation).
     *
     * @GetMapping annotation maps HTTP GET requests to "/api/users/{id}"
     * to this method. This is a dynamic endpoint where {id} is replaced
     * with the actual user ID. GET is used for retrieving a single resource.
     *
     * @param userId Path variable extracted from the URL representing the
     *               user ID to fetch. The @PathVariable annotation binds the
     *               {id} from the URL to the userId parameter.
     * @return ResponseEntity<User> - HTTP 200 OK with the User object in
     *         the response body if found. Spring automatically converts
     *         User object to JSON.
     * @throws UserException Custom exception if user with given ID is not found
     */
    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String userId) throws UserException {
        // Call service layer to get user by ID
        User user = userService.getUserById(userId);
        // Return HTTP 200 OK with the user object
        return ResponseEntity.ok(user);
    }
}