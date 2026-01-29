// Package declaration: Organizes utility classes together
// Utility classes provide helper methods for common operations
package com.ratnakar.practice.TicketBookingAPI.utils;

// Spring Framework annotations for dependency injection
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
// JDBC imports for database connection management
import javax.sql.DataSource;  // Standard Java interface for database connections
import java.sql.Connection;   // JDBC Connection interface
import java.sql.SQLException; // Checked exception for database errors

/**
 * DbUtils - Utility class for database connection management with failover.
 *
 * @Component annotation marks this class as a Spring-managed component.
 * Components are generic Spring beans that can be injected anywhere in the application.
 *
 * This class provides a utility method to get database connections with
 * automatic failover from MySQL to PostgreSQL if MySQL is unavailable.
 *
 * IMPORTANT: This class bypasses Spring's connection and transaction management.
 * It directly obtains JDBC connections from the data source, which means:
 * 1. Connections are not managed by Spring's transaction management
 * 2. Connections must be explicitly closed by the caller
 * 3. No automatic connection pooling benefits (though HikariCP is still used)
 * 4. Not integrated with JPA/Hibernate EntityManager
 *
 * Use cases for this approach:
 * 1. Low-level JDBC operations not covered by JPA
 * 2. Database maintenance scripts
 * 3. Raw SQL queries that can't be expressed in JPQL
 * 4. Connection testing/health checks
 *
 * WARNING: Using raw JDBC connections alongside JPA can lead to:
 * 1. Transaction management issues
 * 2. Connection pool exhaustion
 * 3. Inconsistent data states
 * 4. Memory leaks if connections aren't closed
 */
@Component
public class DbUtils {

    /**
     * Dependency Injection of routing data source.
     *
     * @Autowired injects the routingDataSource bean defined in DataSourceConfig.
     * This is a RoutingDataSource that can switch between MySQL and PostgreSQL.
     *
     * The routingDataSource uses ThreadLocal (DbContextHolder) to determine
     * which underlying data source (MySQL or PostgreSQL) to use.
     */
    @Autowired
    private DataSource routingDataSource;

    /**
     * Gets a database connection with automatic failover from MySQL to PostgreSQL.
     *
     * This method implements a failover strategy:
     * 1. First tries to get a connection from MySQL
     * 2. If MySQL fails, automatically falls back to PostgreSQL
     * 3. Always cleans up the ThreadLocal context in finally block
     *
     * @return Connection JDBC connection from either MySQL or PostgreSQL
     * @throws SQLException if both MySQL and PostgreSQL connections fail
     *
     * IMPORTANT SECURITY/CONCURRENCY ISSUE:
     * The finally block clears the DbContextHolder, which is good practice
     * to prevent memory leaks. However, this means the connection is obtained
     * with one database context but then the context is immediately cleared.
     * This shouldn't affect the returned connection, but it's unusual design.
     *
     * DESIGN FLAW: This method has side effects on global state (DbContextHolder)
     * and doesn't maintain the context for the returned connection's lifetime.
     */
    public Connection getConnection() throws SQLException {
        try {
            // Set the database context to MySQL for the current thread
            // This tells RoutingDataSource to use MySQL data source
            DbContextHolder.setDbType("mysql");

            // Get connection from routing data source (will use MySQL due to context)
            // This may throw SQLException if MySQL is unavailable
            return routingDataSource.getConnection();

        } catch (Exception e) {
            // MySQL connection failed, log and switch to PostgreSQL
            // Using System.out.println for logging is not recommended in production
            // Consider using SLF4J/Logback or Spring's logging
            System.out.println("MySQL failed, switching to PostgreSQL");

            // Change database context to PostgreSQL
            DbContextHolder.setDbType("postgres");

            // Get connection from PostgreSQL data source
            // If this also fails, the SQLException will propagate to caller
            return routingDataSource.getConnection();

        } finally {
            // CRITICAL: Always clean up ThreadLocal to prevent memory leaks
            // This removes the database context for the current thread
            DbContextHolder.clearDbType();

            // NOTE: The connection has already been obtained, but the context is cleared.
            // This might be problematic if the connection needs the context later.
            // Typically, you'd maintain the context as long as the connection is open.
        }
    }

    // CRITICAL ISSUES AND IMPROVEMENTS NEEDED:

    /**
     * ISSUE 1: Connection lifecycle management
     * ========================================
     * The caller is responsible for closing the connection.
     * This often leads to connection leaks in practice.
     *
     * SOLUTION: Use try-with-resources or return a wrapper that auto-closes
     *
     * Better implementation:
     *
     * public Connection getConnection() throws SQLException {
     *     // Use wrapper that implements AutoCloseable
     *     return new AutoCloseableConnection(routingDataSource.getConnection());
     * }
     *
     * OR use Spring's JdbcTemplate which manages connections automatically.
     */

    /**
     * ISSUE 2: Inconsistent context management
     * =========================================
     * The method sets/clears DbContextHolder but the connection retains
     * no reference to which database it's connected to.
     *
     * SOLUTION: Store database type in connection attributes or return a wrapper
     *
     * public class TaggedConnection implements Connection {
     *     private final Connection delegate;
     *     private final String databaseType;
     *
     *     // Implement all Connection methods, delegating to 'delegate'
     *     // Add getDatabaseType() method
     * }
     */

    /**
     * ISSUE 3: Error handling and logging
     * ====================================
     * Using System.out.println for logging is inadequate.
     * No distinction between different types of exceptions.
     *
     * SOLUTION: Use proper logging and exception handling
     *
     * private static final Logger logger = LoggerFactory.getLogger(DbUtils.class);
     *
     * try {
     *     // MySQL attempt
     * } catch (SQLException mysqlEx) {
     *     logger.warn("MySQL connection failed: {}", mysqlEx.getMessage());
     *     logger.info("Failing over to PostgreSQL");
     *     // PostgreSQL attempt
     * }
     */

    /**
     * ISSUE 4: No connection validation
     * ==================================
     * The method doesn't validate if the connection is actually usable.
     *
     * SOLUTION: Add connection test query or use data source validation
     *
     * // In DataSourceConfig, configure validation query:
     * ds.setConnectionTestQuery("SELECT 1"); // MySQL
     * ds.setConnectionTestQuery("SELECT 1"); // PostgreSQL
     */

    /**
     * ISSUE 5: Thread safety concerns
     * ================================
     * The method modifies ThreadLocal state, which affects the current thread.
     * If called from multiple places concurrently on the same thread,
     * there could be context conflicts.
     *
     * SOLUTION: Consider using method synchronization or different design
     */

    /**
     * ALTERNATIVE DESIGN: Spring's JdbcTemplate
     * ==========================================
     * Instead of raw connections, use Spring's JdbcTemplate which:
     * 1. Manages connections automatically
     * 2. Handles exceptions consistently
     * 3. Provides convenient query methods
     * 4. Integrates with transaction management
     *
     * @Service
     * public class DatabaseService {
     *     @Autowired
     *     private JdbcTemplate jdbcTemplate;
     *
     *     public void executeQuery() {
     *         // jdbcTemplate automatically manages connections
     *         jdbcTemplate.query(...);
     *     }
     * }
     */

    /**
     * ALTERNATIVE DESIGN: Multiple JdbcTemplates
     * ===========================================
     * For multi-database setup, create multiple JdbcTemplate beans:
     *
     * @Configuration
     * public class JdbcConfig {
     *     @Bean
     *     public JdbcTemplate mysqlJdbcTemplate(@Qualifier("mysqlDataSource") DataSource ds) {
     *         return new JdbcTemplate(ds);
     *     }
     *
     *     @Bean
     *     public JdbcTemplate postgresJdbcTemplate(@Qualifier("postgresDataSource") DataSource ds) {
     *         return new JdbcTemplate(ds);
     *     }
     * }
     */

    /**
     * BEST PRACTICES FOR PRODUCTION:
     *
     * 1. Use connection pooling (already using HikariCP via HikariDataSource)
     * 2. Always close connections in finally block or try-with-resources
     * 3. Use proper logging framework (SLF4J with Logback)
     * 4. Implement connection health checks
     * 5. Consider circuit breaker pattern for failover
     * 6. Monitor connection pool metrics
     * 7. Use connection timeouts and retry policies
     */

    /**
     * EXAMPLE OF SAFER USAGE PATTERN:
     *
     * // Using try-with-resources to ensure connection is closed
     * try (Connection conn = dbUtils.getConnection();
     *      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
     *      ResultSet rs = stmt.executeQuery()) {
     *
     *     while (rs.next()) {
     *         // Process results
     *     }
     * } catch (SQLException e) {
     *     // Handle exception
     * }
     */
}