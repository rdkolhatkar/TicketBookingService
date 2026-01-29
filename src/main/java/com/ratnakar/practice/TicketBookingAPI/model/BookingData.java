// Package declaration: Organizes the model classes together
// This class is in the same package as other model classes for consistency
package com.ratnakar.practice.TicketBookingAPI.model;

/**
 * BookingData - Data Transfer Object (DTO) for booking information.
 *
 * This is a DTO (Data Transfer Object) pattern implementation.
 *
 * IMPORTANT CONCEPT: DTO vs Entity
 * - Entity (Booking class): Maps directly to database table, contains all fields
 *   including relationships (like User object). Used for database operations.
 * - DTO (BookingData class): Contains only the data needed for a specific use case
 *   (API response). Used to transfer data between layers without exposing internal
 *   structure or causing performance issues.
 *
 * Why use DTOs?
 * 1. Security: Hide sensitive or unnecessary data from the API response
 * 2. Performance: Avoid loading entire object graphs (like User details) when not needed
 * 3. Decoupling: Client doesn't depend on database schema
 * 4. Customization: Provide different views of the same entity for different endpoints
 * 5. Prevent infinite recursion: Avoid circular references in JSON serialization
 *
 * In this case, BookingData is used for the "get all bookings" endpoint
 * to return only essential booking information without the full User object.
 *
 * This class does NOT use:
 * - JPA annotations (not a database entity)
 * - Lombok annotations (manual getters for clarity)
 * - Jackson annotations (uses default JSON serialization)
 */
public class BookingData {

    /**
     * Booking ID - Unique identifier for the booking.
     * This corresponds to booking_id in the bookings table.
     * Using Long wrapper class instead of primitive long to allow null values.
     */
    private Long bookingId;

    /**
     * Name of the person who made the booking.
     * This is a direct copy from the Booking entity.
     */
    private String bookedByName;

    /**
     * Name of the movie for which tickets were booked.
     * This is a direct copy from the Booking entity.
     */
    private String movieName;

    /**
     * Number of tickets booked.
     * Using primitive int as number of tickets cannot be null.
     * If you need to represent "unknown" or "not specified", consider using Integer.
     */
    private int numberOfTickets;

    /**
     * User ID (as String) - Only stores the user ID, not the entire User object.
     * This is the key difference from the Booking entity which has @ManyToOne User.
     *
     * Why store just the userId instead of the User object?
     * 1. Reduces response size: Only ID instead of entire User object
     * 2. Avoids circular references: User might have reference to Bookings
     * 3. Client can fetch user details separately if needed
     * 4. Prevents loading unnecessary data (lazy loading issues in JSON serialization)
     */
    private String userId; // store just the userId

    /**
     * Constructor that converts a Booking entity to BookingData DTO.
     * This is a transformation/mapping constructor.
     *
     * @param booking The Booking entity object from database
     *
     * How it works:
     * 1. Takes a complete Booking entity (with User object) as input
     * 2. Extracts only the fields needed for the API response
     * 3. Gets the user ID from the nested User object
     * 4. Creates a simplified DTO with only necessary data
     *
     * This pattern is called "constructor-based mapping" and is simple
     * for one-to-one mappings. For complex mappings, consider using
     * libraries like ModelMapper or MapStruct.
     *
     * Important: This constructor assumes booking.getUser() is not null.
     * In a production application, you should add null checks.
     */
    public BookingData(Booking booking) {
        // Copy booking ID from entity
        this.bookingId = booking.getBookingId();
        // Copy booked by name from entity
        this.bookedByName = booking.getBookedByName();
        // Copy movie name from entity
        this.movieName = booking.getMovieName();
        // Copy number of tickets from entity
        this.numberOfTickets = booking.getNumberOfTickets();
        // Extract only the user ID from the User object (not the entire object)
        // This prevents serializing the entire User object in JSON response
        this.userId = booking.getUser().getUserID();
    }

    // Getter methods
    // No setter methods because this is an immutable DTO (once created, cannot be modified)
    // This follows the principle of immutable data transfer objects

    /**
     * Getter for booking ID.
     * @return Long booking ID
     */
    public Long getBookingId() { return bookingId; }

    /**
     * Getter for booked by name.
     * @return String name of person who booked
     */
    public String getBookedByName() { return bookedByName; }

    /**
     * Getter for movie name.
     * @return String name of the movie
     */
    public String getMovieName() { return movieName; }

    /**
     * Getter for number of tickets.
     * @return int number of tickets booked
     */
    public int getNumberOfTickets() { return numberOfTickets; }

    /**
     * Getter for user ID.
     * @return String user ID associated with this booking
     */
    public String getUserId() { return userId; }

    // Note: We could add the following if needed:

    // 1. No-argument constructor (if using frameworks that require it)
    // public BookingData() {}

    // 2. Setter methods (if we want mutable DTO, but immutable is preferred)

    // 3. toString() method for debugging
    // @Override
    // public String toString() {
    //     return "BookingData{bookingId=" + bookingId + ", ...}";
    // }

    // 4. equals() and hashCode() methods if using in collections

    /**
     * Alternative approach: Using Builder pattern
     * public static BookingDataBuilder builder() { return new BookingDataBuilder(); }
     *
     * Or using static factory method:
     * public static BookingData from(Booking booking) { return new BookingData(booking); }
     */
}