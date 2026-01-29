// Package declaration: Organizes repository classes together
// Repository classes are part of the data access layer in Spring applications
package com.ratnakar.practice.TicketBookingAPI.repository;

// Import the User entity class that this repository manages
import com.ratnakar.practice.TicketBookingAPI.model.User;
// Spring Data JPA imports for repository functionality
import org.springframework.data.jpa.repository.JpaRepository;  // Base repository interface with CRUD operations
import org.springframework.stereotype.Repository;              // Annotation to mark as Spring repository component

/**
 * UserRepository - Data Access Layer interface for User entity.
 *
 * This is a Spring Data JPA repository interface that provides CRUD operations
 * and custom query methods for the User entity. It serves as the bridge between
 * the application and the database for all user-related data operations.
 *
 * @Repository annotation marks this interface as a Spring Data Repository.
 * It's a specialization of @Component, which allows:
 * 1. Spring to detect this interface during component scanning
 * 2. Automatic exception translation (SQL exceptions to Spring's DataAccessException)
 * 3. Creation of a proxy implementation at runtime (no manual implementation needed)
 *
 * extends JpaRepository<User, String> provides:
 * 1. Type parameters: <User, String>
 *    - User: The entity type this repository manages (maps to "registration" table)
 *    - String: The type of the entity's primary key (userId is String/UUID)
 * 2. Inherited CRUD methods from JpaRepository (which extends PagingAndSortingRepository, CrudRepository):
 *    - save(User entity): Save or update a user (returns saved entity)
 *    - findById(String id): Find user by ID (returns Optional<User>)
 *    - existsById(String id): Check if user exists
 *    - findAll(): Get all users (returns List<User>)
 *    - findAllById(Iterable<String> ids): Get multiple users by IDs
 *    - count(): Get total number of users
 *    - deleteById(String id): Delete user by ID
 *    - delete(User entity): Delete user by entity
 *    - deleteAll(): Delete all users
 *    - And many more...
 *
 * Spring Data JPA automatically generates the implementation of these methods
 * at runtime based on method naming conventions or @Query annotations.
 *
 * How it works (Spring Magic):
 * 1. During application startup, Spring scans for interfaces extending JpaRepository
 * 2. Creates dynamic proxy implementation using Java Reflection
 * 3. The proxy uses EntityManager to execute database operations
 * 4. Query methods are automatically implemented based on method names
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Custom query method to find a user by email address.
     *
     * This is a "derived query" method - Spring Data JPA automatically
     * implements it based on the method name convention without writing any SQL/JPQL.
     *
     * Method name breakdown:
     * - "findBy": Indicates a query operation (find records)
     * - "Email": Refers to the email property in User entity (field name: email)
     * - The method returns a single User entity (or null if not found)
     *
     * Equivalent JPQL query that Spring generates automatically:
     *   SELECT u FROM User u WHERE u.email = ?1
     *
     * Important: The field name in the method must match the entity field name (case-insensitive).
     * "Email" in method name refers to "email" field in User entity.
     *
     * @param email The email address to search for (must match exactly, case-sensitive unless database is case-insensitive)
     * @return User entity with the matching email, or null if not found
     *
     * Usage example in service layer:
     *   User user = userRepository.findByEmail("john@example.com");
     *   if (user != null) {
     *       // User exists with this email
     *   }
     *
     * Note: This method returns a single User. Assumptions:
     * 1. Email should be unique in the database (should have unique constraint)
     * 2. If multiple users have same email, returns the first one (unpredictable)
     *
     * For better practice:
     * 1. Add unique constraint on email column in User entity
     * 2. Consider returning Optional<User> to handle null safely
     * 3. Consider using findByEmailIgnoreCase for case-insensitive search
     */
    public User findByEmail(String email);

    // Note: Additional query methods can be added using Spring Data JPA naming conventions:

    /**
     * Example: Find user by username (assuming username is unique)
     * User findByUserName(String userName);
     *
     * Example: Find user by mobile number
     * User findByMobile(String mobile);
     *
     * Example: Find users by first name (returns list since multiple users can have same first name)
     * List<User> findByFirstName(String firstName);
     *
     * Example: Find users by first name ignoring case
     * List<User> findByFirstNameIgnoreCase(String firstName);
     *
     * Example: Find user by email and password (for login)
     * User findByEmailAndPassword(String email, String password);
     *
     * Example: Check if user exists by email
     * boolean existsByEmail(String email);
     *
     * Example: Find users with pagination
     * Page<User> findByLastName(String lastName, Pageable pageable);
     *
     * Example: Find users sorted by first name
     * List<User> findByLastNameOrderByFirstNameAsc(String lastName);
     */

    /**
     * Using @Query annotation for custom JPQL queries:
     *
     * @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
     * User findActiveUserByEmail(@Param("email") String email);
     *
     * @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%'))")
     * List<User> searchByFirstNameContaining(@Param("name") String name);
     *
     * @Query(value = "SELECT * FROM registration WHERE email = ?1", nativeQuery = true)
     * User findUserByEmailNative(String email);
     */

    /**
     * Important considerations for production:
     *
     * 1. Unique Constraints: Ensure email, username, mobile have unique constraints in database
     *    to prevent duplicate users and ensure findBy methods work predictably
     *
     * 2. Indexing: Database columns used in WHERE clauses (email, username) should be indexed
     *    for better query performance
     *
     * 3. Case Sensitivity: Be aware of database collation (case-sensitive vs case-insensitive)
     *    Use IgnoreCase in method names if needed: findByEmailIgnoreCase
     *
     * 4. Return Type Safety: Consider using Optional<User> instead of User to avoid NullPointerException
     *    Optional<User> findByEmail(String email);
     *
     * 5. Performance: For methods that might return many results, consider pagination (Page<User>)
     *
     * 6. Security: Never write methods that expose passwords or other sensitive data
     *
     * 7. Transaction Management: Repository methods are transactional by default in Spring Data JPA
     */

    /**
     * How Spring Data JPA translates method names to queries:
     *
     * 1. Parses method name: "findByEmail"
     * 2. Removes prefixes: "findBy" → "Email"
     * 3. Maps to entity property: "Email" → User.email field
     * 4. Determines operator: No operator specified, uses "="
     * 5. Determines return type: User (single entity)
     * 6. Generates JPQL: "SELECT u FROM User u WHERE u.email = ?1"
     * 7. Creates implementation at runtime
     *
     * Supported keywords in method names:
     * - And, Or
     * - Between, LessThan, GreaterThan
     * - Like, Containing, StartingWith, EndingWith
     * - IsNull, IsNotNull, NotNull
     * - True, False
     * - IgnoreCase
     * - OrderBy
     */
}