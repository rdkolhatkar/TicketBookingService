// Package declaration: Organizes utility classes together
package com.ratnakar.practice.TicketBookingAPI.utils;

// Spring Framework class for dynamic data source routing
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * RoutingDataSource - Dynamic data source router that selects between multiple databases at runtime.
 *
 * This class extends Spring's AbstractRoutingDataSource to implement dynamic routing
 * between multiple data sources (MySQL and PostgreSQL in this application).
 *
 * AbstractRoutingDataSource is an abstract Spring class that:
 * 1. Provides a framework for routing between multiple target DataSources
 * 2. Uses a lookup key to determine which DataSource to use for each operation
 * 3. Acts as a single DataSource facade that delegates to the appropriate target
 *
 * HOW IT WORKS:
 * 1. When a database operation is requested, Spring calls determineCurrentLookupKey()
 * 2. This method returns a lookup key (e.g., "mysql" or "postgres")
 * 3. AbstractRoutingDataSource uses this key to select the actual DataSource from a map
 * 4. All subsequent database operations on that connection use the selected DataSource
 *
 * This enables:
 * - Database failover (if one DB fails, use another)
 * - Read-write splitting (reads from replica, writes to primary)
 * - Multi-tenant architectures (different DB per tenant)
 * - Database migration (gradual move from one DB to another)
 *
 * The routing decision is typically based on:
 * - ThreadLocal context (as implemented here)
 * - Current transaction
 * - Request parameters or headers
 * - Business logic or configuration
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    /**
     * Determines the current lookup key for selecting the target DataSource.
     *
     * This method is called by Spring for every database operation that requires
     * a connection. It must return a lookup key that maps to one of the configured
     * target DataSources in the targetDataSources map.
     *
     * Implementation details:
     * 1. Gets the database type from DbContextHolder (ThreadLocal)
     * 2. If no type is set, defaults to "mysql"
     * 3. Returns the key that will be used to lookup the DataSource
     *
     * @return Object lookup key (String in this case) that identifies the target DataSource
     *
     * The lookup key must match one of the keys in the targetDataSources map
     * configured in DataSourceConfig.routingDataSource():
     *   targetDataSources.put("mysql", mysqlDataSource);
     *   targetDataSources.put("postgres", postgresDataSource);
     *
     * IMPORTANT: This method is called for EVERY database operation.
     * It should be lightweight and fast to avoid performance issues.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // Use ThreadLocal to track which DB to use
        // DbContextHolder.getDbType() returns the database type set for the current thread
        // This is set by application code (e.g., controllers, services, or DbUtils)
        String dbKey = DbContextHolder.getDbType();

        // Default is "mysql" if no specific key is set
        // This ensures the application always has a fallback database
        return dbKey != null ? dbKey : "mysql";
    }

    // Note: Additional considerations and potential improvements

    /**
     * THREAD SAFETY AND PERFORMANCE:
     *
     * 1. This method is called for each connection request, not for each SQL statement
     * 2. Once a connection is obtained from the pool, it remains associated with that
     *    DataSource until returned to the pool
     * 3. For connection pools, the routing decision is made when obtaining a connection,
     *    not when executing each query
     *
     * CONNECTION POOLING IMPLICATIONS:
     *
     * When using connection pools (like HikariCP):
     * 1. Each target DataSource has its own connection pool
     * 2. Connections are checked out from the appropriate pool based on the lookup key
     * 3. When returned, connections go back to their respective pools
     * 4. Connection pool settings (size, timeout) are per DataSource
     */

    /**
     * ERROR HANDLING AND ROBUSTNESS:
     *
     * Consider adding error handling and fallback logic:
     *
     * @Override
     * protected Object determineCurrentLookupKey() {
     *     try {
     *         String dbKey = DbContextHolder.getDbType();
     *         if (dbKey == null) {
     *             return "mysql"; // Default
     *         }
     *
     *         // Validate that the key exists in targetDataSources
     *         if (!getResolvedDataSources().containsKey(dbKey)) {
     *             logger.warn("Invalid data source key: {}, defaulting to mysql", dbKey);
     *             return "mysql";
     *         }
     *
     *         return dbKey;
     *     } catch (Exception e) {
     *         logger.error("Error determining data source key, defaulting to mysql", e);
     *         return "mysql";
     *     }
     * }
     */

    /**
     * TRANSACTION MANAGEMENT CONSIDERATIONS:
     *
     * When using transactions:
     * 1. All database operations within a transaction should use the same DataSource
     * 2. The lookup key should be determined at transaction start and remain consistent
     * 3. Spring's transaction manager typically calls this method at transaction begin
     *
     * For XA (distributed) transactions across multiple databases:
     * 1. Use JtaTransactionManager instead of JpaTransactionManager
     * 2. Consider using Atomikos or Bitronix for JTA implementation
     * 3. Or use eventual consistency patterns (Saga, Outbox) instead of XA
     */

    /**
     * TESTING CONSIDERATIONS:
     *
     * When testing with RoutingDataSource:
     * 1. Mock or stub the DbContextHolder
     * 2. Test both code paths (mysql and postgres)
     * 3. Test the default fallback behavior
     * 4. Consider using in-memory databases (H2, HSQLDB) for tests
     */

    /**
     * ALTERNATIVE APPROACHES:
     *
     * 1. Annotation-based routing:
     *    Use custom annotations to specify which database to use
     *    @TargetDataSource("postgres")
     *    public void someMethod() { ... }
     *
     * 2. Method name pattern routing:
     *    Route based on method name prefixes or patterns
     *    Methods starting with "read" go to replica, others to primary
     *
     * 3. Request parameter routing:
     *    Extract database preference from HTTP request headers
     *    Useful for multi-tenant applications
     *
     * 4. Sharding-based routing:
     *    Route based on data characteristics (e.g., user ID modulo shard count)
     */

    /**
     * SPRING BOOT AUTO-CONFIGURATION COMPATIBILITY:
     *
     * When using Spring Boot with multiple data sources:
     * 1. Spring Boot auto-configuration may conflict with custom DataSource beans
     * 2. Use @Primary annotation on one DataSource bean to avoid conflicts
     * 3. Or exclude DataSourceAutoConfiguration if configuring manually
     *
     * Example:
     * @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
     */

    /**
     * MONITORING AND METRICS:
     *
     * Consider adding monitoring for:
     * 1. Routing decisions count per database
     * 2. Connection pool statistics per database
     * 3. Error rates per database
     * 4. Query performance per database
     *
     * Can expose metrics via Spring Boot Actuator or custom endpoints.
     */
}