// Package declaration: Organizes repository classes together
// Repository classes in Spring Data JPA are interfaces that handle database operations
package com.ratnakar.practice.TicketBookingAPI.repository;

// Import entity classes that this repository will manage
import com.ratnakar.practice.TicketBookingAPI.model.Booking;  // Booking entity
import com.ratnakar.practice.TicketBookingAPI.model.User;     // User entity for relationship queries
// Spring Data JPA imports for repository functionality
import org.springframework.data.jpa.repository.JpaRepository;  // Base repository interface
import org.springframework.stereotype.Repository;              // Annotation to mark as repository component

// Import Java Collections Framework for List return type
import java.util.List;

/**
 * BookingRepository - Data Access Layer interface for Booking entity.
 *
 * This is a Spring Data JPA repository interface that provides CRUD operations
 * and custom query methods for the Booking entity.
 *
 * @Repository annotation marks this interface as a Spring Data Repository.
 * It's a specialization of @Component, allowing Spring to detect it during
 * component scanning and create a proxy implementation at runtime.
 *
 * extends JpaRepository<Booking, Long> provides:
 * 1. Type parameters: <Booking, Long>
 *    - Booking: The entity type this repository manages
 *    - Long: The type of the entity's primary key (bookingId is Long)
 * 2. Inherited CRUD methods:
 *    - save(S entity): Save or update a booking
 *    - findById(ID id): Find booking by ID (returns Optional<Booking>)
 *    - findAll(): Get all bookings (returns List<Booking>)
 *    - deleteById(ID id): Delete booking by ID
 *    - count(): Get total number of bookings
 *    - And many more...
 *
 * Spring Data JPA automatically provides implementation of these methods
 * at runtime - no need to write implementation code!
 *
 * How it works:
 * 1. Spring creates a proxy implementation of this interface at runtime
 * 2. The proxy handles database operations using JPA EntityManager
 * 3. Query methods are automatically implemented based on method names
 * 4. Can also use @Query annotation for custom JPQL or native queries
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Custom query method to find all bookings by a specific user.
     *
     * This is a "derived query" method - Spring Data JPA automatically
     * implements it based on the method name convention.
     *
     * Method name breakdown:
     * - "findBy": Indicates a query operation
     * - "User": Refers to the User property in Booking entity
     * - The method returns List<Booking> (all bookings for the user)
     *
     * Equivalent JPQL query that Spring generates:
     *   SELECT b FROM Booking b WHERE b.user = :user
     *
     * How Spring interprets this:
     * 1. Looks at Booking entity, finds "user" field (type User)
     * 2. Creates a query: WHERE user = ?1 (parameter 1)
     * 3. Executes the query and returns results
     *
     * @param user The User entity to search bookings for
     * @return List<Booking> containing all bookings associated with the given user
     *         Returns empty list if no bookings found (never returns null)
     *
     * Usage example in service layer:
     *   User user = userRepository.findById(userId);
     *   List<Booking> userBookings = bookingRepository.findByUser(user);
     *
     * Note: This method relies on the @ManyToOne relationship in Booking entity:
     *   @ManyToOne
     *   @JoinColumn(name = "user_id")
     *   private User user;
     *
     * Alternative approaches:
     * 1. Using @Query annotation for custom JPQL:
     *    @Query("SELECT b FROM Booking b WHERE b.user = :user")
     *    List<Booking> findByUser(@Param("user") User user);
     *
     * 2. Find by user ID instead of User entity:
     *    List<Booking> findByUser_UserId(String userId);
     *    (Uses property path: user.userId)
     */
    // Find all bookings by User entity
    List<Booking> findByUser(User user);

    // Note: Additional query methods can be added using naming conventions:

    /**
     * Example: Find bookings by movie name
     * List<Booking> findByMovieName(String movieName);
     *
     * Example: Find bookings with number of tickets greater than
     * List<Booking> findByNumberOfTicketsGreaterThan(int minTickets);
     *
     * Example: Find bookings by user and movie
     * List<Booking> findByUserAndMovieName(User user, String movieName);
     *
     * Example: Find bookings by user ID (alternative to above)
     * List<Booking> findByUser_UserId(String userId);
     */

    /**
     * IMPORTANT SPRING DATA JPA CONCEPTS:
     *
     * 1. Method Naming Conventions:
     *    - findBy[Property]: Find by exact match
     *    - findBy[Property]Like: Find using LIKE operator
     *    - findBy[Property]GreaterThan: Find with > operator
     *    - findBy[Property]Between: Find with BETWEEN operator
     *    - findBy[Property]IsNull: Find null values
     *    - findBy[Property]OrderBy[AnotherProperty]Desc: Sort results
     *
     * 2. Return Types:
     *    - Single entity: Booking
     *    - Optional: Optional<Booking> (safe for single results)
     *    - List: List<Booking> (multiple results)
     *    - Page: Page<Booking> (pagination)
     *    - Stream: Stream<Booking> (Java 8 streams)
     *
     * 3. Parameter Types:
     *    - Entity types (like User)
     *    - Property types (String, Long, etc.)
     *    - Pageable: For pagination and sorting
     *    - Sort: For sorting only
     *
     * 4. @Query Annotation:
     *    Used for complex queries that can't be expressed with method names
     *    @Query("SELECT b FROM Booking b WHERE b.movieName = :name")
     *    List<Booking> findBookingsByMovie(@Param("name") String movieName);
     *
     * 5. Native Queries:
     *    @Query(value = "SELECT * FROM bookings WHERE movie_name = ?1", nativeQuery = true)
     *    List<Booking> findBookingsByMovieNative(String movieName);
     */

    /**
     * TRANSACTION MANAGEMENT:
     * All repository methods are transactional by default in Spring Data JPA.
     * Transactions are automatically managed - no need for @Transactional annotation
     * on repository methods (though it can be added for custom behavior).
     *
     * For custom update/delete operations, use @Modifying with @Query:
     * @Modifying
     * @Query("UPDATE Booking b SET b.numberOfTickets = :tickets WHERE b.bookingId = :id")
     * int updateTicketCount(@Param("id") Long bookingId, @Param("tickets") int tickets);
     */
}