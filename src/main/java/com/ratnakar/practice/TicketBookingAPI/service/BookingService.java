// Package declaration: Organizes service layer interfaces together
// The service layer contains business logic and acts as a bridge between
// controllers (presentation layer) and repositories (data access layer)
package com.ratnakar.practice.TicketBookingAPI.service;

// Import model classes that this service interface will work with
import com.ratnakar.practice.TicketBookingAPI.model.Booking;   // Booking entity for CRUD operations
import com.ratnakar.practice.TicketBookingAPI.model.BookingData; // DTO for booking data representation

// Import Java Collections Framework for List return type
import java.util.List;

/**
 * BookingService - Service layer interface for booking-related business logic.
 *
 * This is a SERVICE INTERFACE that defines the contract for booking operations.
 * In Spring applications, we typically follow the Interface Segregation Principle
 * by defining service interfaces and then providing implementations.
 *
 * PURPOSE OF SERVICE LAYER:
 * 1. Encapsulates business logic and rules
 * 2. Acts as a transaction boundary (methods are typically @Transactional)
 * 3. Coordinates multiple repository calls
 * 4. Performs validation and data transformation
 * 5. Decouples controllers from data access layer
 *
 * This interface follows the STRATEGY DESIGN PATTERN where different
 * implementations can be provided for the same contract.
 *
 * Spring Framework will automatically detect and inject the implementation
 * (BookingServiceImpl) wherever this interface is used with @Autowired.
 *
 * Note: Methods that can throw exceptions declare them in the signature.
 * This follows checked exception pattern, forcing callers to handle exceptions.
 */
public interface BookingService {

    /**
     * Creates a new booking for a user.
     *
     * This method encapsulates the business logic for booking tickets.
     * It should validate inputs, check availability, calculate costs,
     * create the booking entity, and save it to the database.
     *
     * @param userId The unique identifier of the user making the booking.
     *               This should be a valid user ID that exists in the system.
     * @param bookedByName The name of the person for whom the booking is made.
     *                     This might be different from the user's registered name
     *                     (e.g., booking for family members).
     * @param movieName The name of the movie for which tickets are being booked.
     *                  Should be validated against available movies.
     * @param numberOfTickets The number of tickets to book.
     *                        Should be validated (positive number, available seats).
     * @return Booking entity that was created and saved to the database.
     *         The returned entity will have an auto-generated booking ID.
     * @throws Exception Can throw various exceptions:
     *         - UserException if user not found
     *         - BookingException if no seats available
     *         - IllegalArgumentException for invalid parameters
     *         - DataAccessException for database errors
     *
     * Note: Using 'throws Exception' is a broad declaration. In production,
     * consider throwing specific checked/unchecked exceptions.
     *
     * Transactional behavior: This method should run in a transaction
     * to ensure atomicity (all or nothing) of the booking operation.
     */
    Booking bookTicket(String userId, String bookedByName, String movieName, int numberOfTickets) throws Exception;

    /**
     * Updates an existing booking with a new number of tickets.
     *
     * This method handles the business logic for modifying a booking.
     * It should validate the new ticket count, check availability,
     * update the booking, and recalculate any costs.
     *
     * @param bookingId The unique identifier of the booking to update.
     *                  Must be a valid, existing booking ID.
     * @param newNumberOfTickets The new desired number of tickets.
     *                           Should be validated (positive number, available seats).
     * @return Updated Booking entity after the modification.
     * @throws Exception Can throw various exceptions:
     *         - BookingException if booking not found
     *         - BookingException if insufficient seats available
     *         - IllegalArgumentException for invalid parameters
     *         - DataAccessException for database errors
     *
     * Business rules that might be implemented:
     * 1. Cannot update a cancelled booking
     * 2. Cannot reduce tickets to zero (use cancelBooking instead)
     * 3. May charge/refund difference in ticket prices
     *
     * Transactional behavior: This method should run in a transaction
     * to ensure the update is atomic.
     */
    Booking updateBooking(Long bookingId, int newNumberOfTickets) throws Exception;

    /**
     * Cancels an existing booking.
     *
     * This method handles the business logic for cancelling a booking.
     * It should validate the booking exists, update its status to cancelled,
     * and potentially free up seats and process refunds.
     *
     * @param bookingId The unique identifier of the booking to cancel.
     *                  Must be a valid, existing booking ID.
     * @throws Exception Can throw various exceptions:
     *         - BookingException if booking not found
     *         - BookingException if booking already cancelled
     *         - DataAccessException for database errors
     *
     * Business rules that might be implemented:
     * 1. Check cancellation policy (time limits, fees)
     * 2. Update seat availability
     * 3. Process refund if applicable
     * 4. Send cancellation notification
     *
     * Note: This method returns void because cancellation typically
     * doesn't need to return data. The controller returns a success message.
     *
     * Transactional behavior: This method should run in a transaction
     * to ensure cancellation is atomic.
     */
    void cancelBooking(Long bookingId) throws Exception;

    /**
     * Retrieves all bookings in the system.
     *
     * This method returns a list of all bookings, but instead of returning
     * the full Booking entities, it returns BookingData DTOs which provide
     * a simplified view without the full User object relationship.
     *
     * @return List<BookingData> containing simplified booking information
     *         for all bookings in the system. Returns empty list if no bookings.
     *
     * Why return BookingData instead of Booking?
     * 1. Performance: Avoids loading full User objects (LAZY loading issues)
     * 2. Security: Doesn't expose internal entity structure
     * 3. Decoupling: Client doesn't depend on database schema
     * 4. Customization: Provides only needed fields (booking ID, user ID, etc.)
     *
     * This method typically doesn't throw exceptions for empty results,
     * just returns an empty list. Database errors might throw exceptions.
     *
     * Note: For large datasets, consider adding pagination:
     * Page<BookingData> getAllBookings(Pageable pageable);
     */
    List<BookingData> getAllBookings();

    // Note: Additional methods that could be added to the interface:

    /**
     * Get booking by ID
     * Booking getBookingById(Long bookingId) throws Exception;
     *
     * Get bookings by user ID
     * List<BookingData> getBookingsByUserId(String userId);
     *
     * Get bookings by movie name
     * List<BookingData> getBookingsByMovie(String movieName);
     *
     * Check seat availability
     * int getAvailableSeats(String movieName);
     *
     * With pagination
     * Page<BookingData> getAllBookings(Pageable pageable);
     */

    /**
     * DESIGN PATTERNS USED:
     *
     * 1. Strategy Pattern: Interface defines contract, implementations provide behavior
     * 2. Facade Pattern: Simplifies complex operations (coordinate multiple repository calls)
     * 3. Service Layer Pattern: Separates business logic from presentation and data access
     *
     * SPRING INTEGRATION:
     *
     * The implementation class (BookingServiceImpl) will be annotated with @Service.
     * This allows Spring to:
     * 1. Detect it during component scanning
     * 2. Create a singleton bean
     * 3. Inject it wherever BookingService interface is @Autowired
     *
     * TRANSACTION MANAGEMENT:
     *
     * Typically, service methods are annotated with @Transactional in the implementation.
     * This ensures:
     * 1. Methods run within a transaction
     * 2. Automatic rollback on exceptions
     * 3. Proper isolation levels for concurrent access
     */
}