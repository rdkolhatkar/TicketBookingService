// Package declaration: Organizes the model classes together
// This class is a DTO (Data Transfer Object) for receiving booking requests
package com.ratnakar.practice.TicketBookingAPI.model;

// Lombok annotation to reduce boilerplate code
// @Data annotation generates getters, setters, toString, equals, and hashCode
import lombok.Data;

/**
 * BookingRequest - Data Transfer Object for receiving booking creation/update requests.
 *
 * This is a DTO (Data Transfer Object) used to capture data from incoming HTTP requests.
 *
 * IMPORTANT: This class seems to be serving dual purpose, which might be a design issue:
 * 1. For creating new bookings (bookTicket endpoint) - needs: userId, bookedByName, movieName, numberOfTickets
 * 2. For updating bookings (updateBooking endpoint) - needs: newNumberOfTickets
 *
 * However, looking at the controller, there are TWO separate request classes:
 * - BookingRequest (this class) - used for POST /api/bookings/book
 * - UpdateBookingRequest (separate class) - used for PUT /api/bookings/update/{bookingId}
 *
 * The presence of both numberOfTickets and newNumberOfTickets in this class suggests
 * either a design flaw or this class is being reused for multiple purposes.
 *
 * Best Practice: Create separate DTOs for different operations to avoid confusion:
 * - CreateBookingRequest (for POST)
 * - UpdateBookingRequest (for PUT)
 *
 * @Data annotation from Lombok generates:
 * 1. getUserId(), setUserId(String)
 * 2. getBookedByName(), setBookedByName(String)
 * 3. getMovieName(), setMovieName(String)
 * 4. getNumberOfTickets(), setNumberOfTickets(int)
 * 5. getNewNumberOfTickets(), setNewNumberOfTickets(int)
 * 6. toString(), equals(), hashCode() methods
 *
 * However, note that this class ALSO has manually written getters and setters below.
 * This creates redundancy because:
 * - @Data already generates getters and setters
 * - Having both causes duplicate methods (not valid in Java)
 *
 * This appears to be an ERROR in the code. The manual getters/setters should be removed
 * since @Data annotation already generates them.
 */
@Data
public class BookingRequest {

    /**
     * User ID of the person making the booking.
     * This should reference an existing user in the system.
     * Using String type for user ID allows for different ID formats
     * (UUID, alphanumeric, etc.)
     */
    private String userId;

    /**
     * Name of the person for whom the booking is made.
     * This might be different from the user's registered name.
     * For example: A user might book tickets for family members.
     */
    private String bookedByName;

    /**
     * Name of the movie for which tickets are being booked.
     * This should match a movie available in the system.
     */
    private String movieName;

    /**
     * Number of tickets to book.
     * For new bookings, this is the initial number of tickets.
     * For updates, this might represent the current number before update.
     *
     * ISSUE: This field name suggests it's for new bookings, but the class
     * also has newNumberOfTickets which suggests it's for updates.
     * This ambiguity should be resolved.
     */
    private int numberOfTickets;

    /**
     * New number of tickets for updating an existing booking.
     * This field seems to be for update operations, but this class
     * is named BookingRequest (not UpdateBookingRequest).
     *
     * CONFLICT: The controller uses UpdateBookingRequest for update operations,
     * so this field in BookingRequest class might never be used.
     * This indicates a potential refactoring opportunity.
     */
    private int newNumberOfTickets;

    // MANUALLY WRITTEN GETTERS AND SETTERS (REDUNDANT)
    // These are redundant because @Data annotation already generates them.
    // Having both causes compilation errors in Java.
    // This appears to be a coding mistake.

    /**
     * Getter for userId - Manually written (redundant due to @Data)
     * @return String user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Setter for userId - Manually written (redundant due to @Data)
     * @param userId String user ID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Getter for bookedByName - Manually written (redundant due to @Data)
     * @return String name of person booking
     */
    public String getBookedByName() {
        return bookedByName;
    }

    /**
     * Setter for bookedByName - Manually written (redundant due to @Data)
     * @param bookedByName String name to set
     */
    public void setBookedByName(String bookedByName) {
        this.bookedByName = bookedByName;
    }

    /**
     * Getter for movieName - Manually written (redundant due to @Data)
     * @return String movie name
     */
    public String getMovieName() {
        return movieName;
    }

    /**
     * Setter for movieName - Manually written (redundant due to @Data)
     * @param movieName String movie name to set
     */
    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    /**
     * Getter for numberOfTickets - Manually written (redundant due to @Data)
     * @return int number of tickets
     */
    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    /**
     * Setter for numberOfTickets - Manually written (redundant due to @Data)
     * @param numberOfTickets int number of tickets to set
     */
    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    /**
     * Getter for newNumberOfTickets - Manually written (redundant due to @Data)
     * @return int new number of tickets
     */
    public int getNewNumberOfTickets() {
        return newNumberOfTickets;
    }

    /**
     * Setter for newNumberOfTickets - Manually written (redundant due to @Data)
     * @param newNumberOfTickets int new number of tickets to set
     */
    public void setNewNumberOfTickets(int newNumberOfTickets) {
        this.newNumberOfTickets = newNumberOfTickets;
    }

    /**
     * RECOMMENDED FIXES:
     *
     * Option 1: Remove manual getters/setters and rely on @Data
     *   - Delete all manual getters and setters
     *   - Keep @Data annotation
     *
     * Option 2: Remove @Data and keep manual getters/setters
     *   - Remove @Data annotation
     *   - Keep manual getters/setters (add missing ones if any)
     *
     * Option 3: Split into two separate DTOs
     *   - CreateBookingRequest: userId, bookedByName, movieName, numberOfTickets
     *   - UpdateBookingRequest: newNumberOfTickets
     *
     * Option 4: Use inheritance
     *   - BaseBookingRequest: common fields
     *   - CreateBookingRequest extends BaseBookingRequest
     *   - UpdateBookingRequest extends BaseBookingRequest (adds newNumberOfTickets)
     *
     * CURRENT ISSUES:
     * 1. Dual-purpose class (violates Single Responsibility Principle)
     * 2. Redundant code (@Data + manual getters/setters)
     * 3. Unclear field naming (numberOfTickets vs newNumberOfTickets)
     * 4. Potential confusion in API usage
     */
}