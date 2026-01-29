// Package declaration: Organizes the model/entity classes together
// This follows the standard package structure where model classes (entities)
// are grouped separately from controllers, services, etc.
package com.ratnakar.practice.TicketBookingAPI.model;

// Import statements for required libraries and annotations

// Jackson JSON library annotations for JSON serialization/deserialization
// com.fasterxml.jackson.annotation - Used to control how objects are converted to/from JSON
import com.fasterxml.jackson.annotation.JsonIgnore;
// Jakarta Persistence API (JPA) annotations for database mapping
// jakarta.persistence.* - Standard Java API for object-relational mapping (ORM)
import jakarta.persistence.*;
// Lombok annotations to reduce boilerplate code
// lombok.* - Library that generates getters, setters, constructors, etc. at compile time
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Booking entity class representing the booking table in the database.
 *
 * This class is annotated with JPA (Jakarta Persistence API) annotations
 * to map Java objects to database tables (Object-Relational Mapping).
 *
 * @Data annotation from Lombok generates:
 * 1. Getters for all fields
 * 2. Setters for all non-final fields
 * 3. toString() method
 * 4. equals() and hashCode() methods
 * This eliminates the need to write boilerplate code.
 *
 * @NoArgsConstructor generates a no-argument constructor.
 * Required by JPA for entity instantiation.
 *
 * @AllArgsConstructor generates a constructor with all fields as arguments.
 * Useful for creating objects with all values set at once.
 *
 * @Entity annotation marks this class as a JPA entity.
 * This tells Spring Data JPA that this class should be mapped to a database table.
 * The class name (Booking) by default maps to table name "booking" (lowercase),
 * but we override this with @Table annotation.
 *
 * @Table annotation specifies the database table name for this entity.
 * name = "bookings" means this entity maps to the "bookings" table in the database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    /**
     * Primary key field for the Booking entity.
     *
     * @Id annotation marks this field as the primary key of the entity.
     * Every JPA entity must have a primary key.
     *
     * @GeneratedValue annotation specifies how the primary key should be generated.
     * strategy = GenerationType.IDENTITY means the database will automatically
     * generate a unique ID for each new record (auto-increment in MySQL, serial in PostgreSQL).
     * Other strategies include:
     * - GenerationType.AUTO: Let the persistence provider choose
     * - GenerationType.SEQUENCE: Use database sequence
     * - GenerationType.TABLE: Use a separate table for ID generation
     *
     * @Column annotation maps this field to a specific database column.
     * name = "booking_id" specifies the column name in the "bookings" table.
     * If @Column is not specified, JPA uses the field name (bookingId) by default.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id") // match the actual DB column
    private Long bookingId;

    /**
     * Many-to-One relationship with User entity.
     *
     * @ManyToOne annotation defines a many-to-one relationship between Booking and User.
     * Multiple bookings can be associated with one user.
     *
     * fetch = FetchType.LAZY specifies lazy loading strategy.
     * LAZY: The User object is loaded only when it's explicitly accessed.
     * EAGER: The User object is loaded immediately with the Booking (default for @ManyToOne).
     * LAZY loading improves performance by loading related entities only when needed.
     *
     * @JoinColumn annotation defines the foreign key column in the bookings table.
     * name = "user_id" specifies the foreign key column name that references the users table.
     * nullable = false means this field cannot be null (a booking must have a user).
     *
     * @JsonIgnore annotation from Jackson library prevents this field from being
     * included in JSON serialization/deserialization. When Booking objects are
     * converted to JSON (e.g., in API responses), the User object won't be included.
     * This prevents infinite recursion in bidirectional relationships and reduces
     * response size. If you need user data, use Data Transfer Objects (DTOs) instead.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    /**
     * Name of the person who made the booking.
     *
     * @Column annotation maps this field to the "booked_by_name" column.
     * If the column name matches the field name (bookedByName), @Column is optional.
     * Specifying it explicitly is good practice for clarity.
     */
    @Column(name = "booked_by_name")
    private String bookedByName;

    /**
     * Name of the movie for which tickets are booked.
     *
     * @Column annotation maps this field to the "movie_name" column.
     * String fields in JPA are typically mapped to VARCHAR columns in the database.
     */
    @Column(name = "movie_name")
    private String movieName;

    /**
     * Number of tickets booked.
     *
     * @Column annotation maps this field to the "number_of_tickets" column.
     * int primitive type is mapped to INTEGER column in most databases.
     * Note: Using primitive int means it cannot be null in Java (defaults to 0).
     * If you need null values, use Integer wrapper class instead.
     */
    @Column(name = "number_of_tickets")
    private int numberOfTickets;

    // Note: Lombok's @Data annotation automatically generates:
    // 1. getBookingId(), setBookingId(Long id)
    // 2. getUser(), setUser(User user)
    // 3. getBookedByName(), setBookedByName(String name)
    // 4. getMovieName(), setMovieName(String name)
    // 5. getNumberOfTickets(), setNumberOfTickets(int tickets)
    // 6. toString(), equals(), hashCode() methods

    // Note: Additional methods can be added here if needed, but they should
    // not interfere with Lombok-generated methods.
}