// Package declaration: Organizes the model/entity classes together
// This class represents the User entity in the database
package com.ratnakar.practice.TicketBookingAPI.model;

// Import statements for required libraries and annotations

// Jackson JSON library for JSON serialization control
// Jakarta Persistence API for database mapping (ORM)

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * User entity class representing the registration table in the database.
 *
 * This class models a user in the system with personal information and credentials.
 * It uses JPA for database mapping, Jakarta Validation for input validation,
 * and Hibernate for custom ID generation.
 *
 * @Data annotation from Lombok generates:
 * - Getters and setters for all fields
 * - toString(), equals(), and hashCode() methods
 *
 * @NoArgsConstructor generates a no-argument constructor (required by JPA).
 *
 * @AllArgsConstructor generates a constructor with all fields as arguments.
 *
 * @Entity annotation marks this class as a JPA entity that maps to a database table.
 *
 * @Table annotation specifies the database table name as "registration".
 * If @Table is not specified, JPA uses the class name (User) as table name.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registration")
public class User {

    /**
     * Primary key field for the User entity.
     *
     * @Id annotation marks this field as the primary key.
     *
     * @GeneratedValue with generator = "uuid2" specifies custom ID generation.
     *
     * @GenericGenerator from Hibernate defines a UUID generator with strategy "uuid2".
     * This generates a UUID (Universally Unique Identifier) for each user.
     * UUIDs are 128-bit unique identifiers, good for distributed systems.
     *
     * @Column annotation with:
     * - name = "user_id": Maps to user_id column in database
     * - nullable = false: Column cannot be null
     * - columnDefinition = "CHAR(36)": Specifies column as CHAR(36) to store UUID string
     *   UUIDs are 36 characters long (32 hex digits + 4 hyphens)
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "user_id", nullable = false, columnDefinition = "CHAR(36)")
    private String userId;

    /**
     * Username for the user.
     *
     * @NotNull annotation (from Jakarta Validation) ensures field is not null.
     * Note: @NotNull only checks for null, not empty string.
     *
     * @Column maps to "user_name" column in database.
     */
    @NotNull
    @Column(name = "user_name")
    String userName;

    /**
     * User's first name with multiple validation constraints.
     *
     * @NotNull: Cannot be null
     * @NotBlank: Cannot be null, empty, or whitespace only
     * @Column maps to "first_name" column
     *
     * Note: Using both @NotNull and @NotBlank is redundant since
     * @NotBlank already implies @NotNull. @NotBlank is sufficient.
     */
    @NotNull(message = "Name cannot be null!")
    @NotBlank(message = "Name cannot be blank!")
    @Column(name = "first_name")
    String firstName;

    /**
     * User's last name.
     * Only @Column annotation, no validation constraints.
     * This means last name can be null or empty.
     */
    @Column(name = "last_name")
    String lastName;

    /**
     * Mobile number with comprehensive validation.
     *
     * @NotNull: Cannot be null
     * @NotBlank: Cannot be empty or whitespace
     * @Pattern: Must match regex [6789]{1}[0-9]{9}
     *   - [6789]{1}: First digit must be 6, 7, 8, or 9 (Indian mobile numbers)
     *   - [0-9]{9}: Followed by exactly 9 digits (0-9)
     * @Size(min = 10, max = 10): Exactly 10 characters
     * @Column maps to "mobile" column
     *
     * The regex ensures valid Indian mobile numbers starting with 6, 7, 8, or 9.
     */
    @NotNull(message="Mobile number cannot be null!")
    @NotBlank(message= "Mobile number cannot be blank!")
    @Pattern(regexp = "[6789]{1}[0-9]{9}", message = "Enter valid 10 digit mobile number")
    @Size(min = 10, max = 10)
    @Column(name = "mobile")
    String mobile;

    /**
     * Email address with validation.
     *
     * @Email: Validates email format using Jakarta Validation
     * The annotation checks for basic email format (xxx@yyy.zzz)
     * For stricter validation, consider regex pattern
     * @Column maps to "email" column
     */
    @Email
    @Column(name = "email")
    String email;

    /**
     * Password with strong validation.
     *
     * @NotNull: Cannot be null
     * @NotBlank: Cannot be empty or whitespace
     * @Pattern with regex [A-Za-z0-9!@#$%^&*_]{8,15}:
     *   - Must be 8-15 characters long
     *   - Can contain: uppercase/lowercase letters, digits, special characters !@#$%^&*_
     *   - No spaces allowed
     * @Column maps to "password" column
     *
     * SECURITY NOTE: Passwords should be hashed before storing in database.
     * This validation only checks format, not storage security.
     * In production, use password hashing (BCrypt, Argon2, etc.)
     */
    @NotNull(message="Password cannot be null!")
    @NotBlank(message= "Password cannot be blank!")
    @Pattern(regexp = "[A-Za-z0-9!@#$%^&*_]{8,15}", message = "Password must be 8-15 characters including alphanumerics and special characters")
    @Column(name = "password")
    String password;

    // IMPORTANT ISSUE: REDUNDANT GETTERS AND SETTERS
    // The class has @Data annotation (generates getters/setters) AND
    // manually written getters/setters below. This creates duplicate methods.
    // This appears to be a coding error.

    /**
     * Manually written getter for userId.
     * Note: Method name is getUserID() but field is userId (different case).
     * This mismatch can cause issues with frameworks that rely on naming conventions.
     *
     * @return String userId
     */
    public String getUserID() {
        return userId;
    }

    /**
     * Manually written setter for userId.
     * Note: Parameter name userID doesn't match field name userId.
     *
     * @param userID String user ID to set
     */
    public void setUserID(String userID) {
        this.userId = userID;
    }

    // The following getters and setters are redundant due to @Data annotation

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Note: The class is missing important fields that might be needed:
    // 1. @OneToMany relationship with Booking (if you want to fetch user's bookings)
    //    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //    @JsonIgnore  // Prevent infinite recursion in JSON
    //    private List<Booking> bookings;
    // 
    // 2. Timestamps for created/modified dates
    //    @CreatedDate, @LastModifiedDate with @EntityListeners(AuditingEntityListener.class)
    // 
    // 3. Role/authority fields for Spring Security
    // 
    // 4. Active/status field (isActive, isLocked, etc.)

    /**
     * SECURITY RECOMMENDATIONS:
     * 1. Never store passwords in plain text - always hash them
     * 2. Consider using @Transient field for password confirmation in registration
     * 3. Add email verification field (isEmailVerified)
     * 4. Add account lock mechanism for failed login attempts
     *
     * VALIDATION IMPROVEMENTS:
     * 1. Add unique constraints (@Column(unique = true)) for username, email, mobile
     * 2. Consider custom validation annotations for complex rules
     * 3. Add @Past validation for date of birth if added
     *
     * JPA RELATIONSHIPS:
     * If User has bookings, add @OneToMany relationship
     * If User has roles, add @ManyToMany relationship
     */
}