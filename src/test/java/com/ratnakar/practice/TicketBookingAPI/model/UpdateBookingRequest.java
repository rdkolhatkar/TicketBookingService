// Package declaration: Organizes the model classes together
// This class is a DTO (Data Transfer Object) for update booking requests
package com.ratnakar.practice.TicketBookingAPI.model;

/**
 * UpdateBookingRequest - Data Transfer Object for updating booking details.
 *
 * This is a DTO (Data Transfer Object) used specifically for updating 
 * the number of tickets in an existing booking.
 *
 * IMPORTANT DESIGN PRINCIPLE: Single Responsibility Principle
 * This class has a single responsibility: to capture the data needed
 * for updating a booking (specifically, changing the number of tickets).
 *
 * Why a separate DTO instead of reusing BookingRequest?
 * 1. Clear intent: The class name clearly indicates its purpose
 * 2. Minimal data: Only contains fields needed for update operation
 * 3. Avoids confusion: No ambiguity about which fields are required
 * 4. API contract clarity: Clients know exactly what to send for update
 *
 * This pattern follows the RESTful API best practice where:
 * - POST requests (create) use CreateBookingRequest (or BookingRequest)
 * - PUT requests (update) use UpdateBookingRequest
 * - Each operation has its own specific DTO with only necessary fields
 *
 * Note: This class does NOT use Lombok annotations (@Data, @Getter, @Setter)
 * Instead, it uses manually written getters and setters. This is acceptable
 * for simple DTOs with few fields, but for consistency, you might want to
 * use Lombok or stick to manual methods throughout the project.
 */
public class UpdateBookingRequest {

    /**
     * New number of tickets for the booking update.
     *
     * This field represents the updated/desired number of tickets
     * after the update operation. The controller extracts this value
     * and passes it to the service layer to update the booking.
     *
     * Type: int (primitive) - number of tickets cannot be null
     * If you need to represent "no change" or "unknown", you might want:
     * 1. Use Integer (wrapper) to allow null values
     * 2. Add validation (e.g., must be positive number)
     * 3. Add business logic (e.g., cannot reduce to 0 tickets - use cancel instead)
     *
     * In the current implementation:
     * - 0 might mean cancel booking (but there's separate cancel endpoint)
     * - Negative values should be validated in controller/service
     *
     * Example usage in controller:
     *   @PutMapping("/update/{bookingId}")
     *   public ResponseEntity<Booking> updateBooking(
     *       @PathVariable Long bookingId,
     *       @RequestBody UpdateBookingRequest request) {
     *       // request.getNewNumberOfTickets() returns the value sent by client
     *   }
     */
    private int newNumberOfTickets;

    /**
     * Getter method for newNumberOfTickets field.
     *
     * This method follows the JavaBean naming convention (getXxx for fields).
     * It provides read access to the private field.
     *
     * @return int - the new number of tickets requested
     *
     * Note: This is a manually written getter. Alternatives:
     * 1. Use Lombok @Getter annotation
     * 2. Use public field (not recommended for encapsulation)
     * 3. Use record class (Java 14+)
     */
    public int getNewNumberOfTickets() {
        return newNumberOfTickets;
    }

    /**
     * Setter method for newNumberOfTickets field.
     *
     * This method follows the JavaBean naming convention (setXxx for fields).
     * It provides write access to the private field.
     *
     * @param newNumberOfTickets - the new number of tickets to set
     *
     * Note: This setter could include validation logic, for example:
     *   if (newNumberOfTickets <= 0) {
     *       throw new IllegalArgumentException("Number of tickets must be positive");
     *   }
     * However, validation is often done at controller level using @Valid
     * and validation annotations (like @Min, @Max) on the field.
     *
     * Recommended improvement: Add validation annotation
     *   @Min(value = 1, message = "At least 1 ticket is required")
     *   private int newNumberOfTickets;
     */
    public void setNewNumberOfTickets(int newNumberOfTickets) {
        this.newNumberOfTickets = newNumberOfTickets;
    }

    // Note: Additional methods that could be useful:

    /**
     * No-argument constructor (implicitly available)
     * Java provides a default no-arg constructor if no other constructors are defined.
     * This is required for:
     * 1. JSON deserialization (Spring uses it to create object from JSON)
     * 2. JPA entity instantiation (if this were an entity)
     *
     * If you add a parameterized constructor, remember to also add no-arg constructor.
     */

    /**
     * Parameterized constructor (optional but useful)
     *
     * public UpdateBookingRequest(int newNumberOfTickets) {
     *     this.newNumberOfTickets = newNumberOfTickets;
     * }
     *
     * This allows creating objects in one line:
     *   UpdateBookingRequest request = new UpdateBookingRequest(5);
     */

    /**
     * toString() method (helpful for debugging)
     *
     * @Override
     * public String toString() {
     *     return "UpdateBookingRequest{newNumberOfTickets=" + newNumberOfTickets + "}";
     * }
     *
     * This method is automatically called in logs and debug output.
     */

    /**
     * equals() and hashCode() methods (if needed for comparisons)
     *
     * These are important if you plan to:
     * 1. Store UpdateBookingRequest objects in collections
     * 2. Compare UpdateBookingRequest objects
     * 3. Use as keys in maps
     */

    /**
     * Validation considerations:
     *
     * 1. Add validation annotations to ensure data integrity:
     *    import jakarta.validation.constraints.Min;
     *    import jakarta.validation.constraints.NotNull;
     *
     *    @Min(1)  // At least 1 ticket
     *    @NotNull // Not null (though int can't be null, but if using Integer)
     *    private int newNumberOfTickets;
     *
     * 2. Then in controller, use @Valid annotation:
     *    public ResponseEntity<Booking> updateBooking(
     *        @PathVariable Long bookingId,
     *        @Valid @RequestBody UpdateBookingRequest request)
     *
     * 3. Global exception handler can handle MethodArgumentNotValidException
     */
}