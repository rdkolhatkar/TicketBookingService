// Package declaration: Organizes utility classes together
// Utility classes provide helper methods and shared functionality
package com.ratnakar.practice.TicketBookingAPI.utils;

/**
 * DbContextHolder - Thread-local context holder for database routing.
 *
 * This class manages the current database context (which database to use)
 * on a per-thread basis using ThreadLocal.
 *
 * IMPORTANT CONCEPT: ThreadLocal
 * ThreadLocal is a Java class that provides thread-local variables.
 * Each thread has its own independently initialized copy of the variable.
 *
 * How ThreadLocal works:
 * 1. Each thread accessing a ThreadLocal variable has its own, independently initialized copy
 * 2. ThreadLocal instances are typically private static fields
 * 3. Values are stored in a map where the key is the ThreadLocal instance and value is the thread's value
 *
 * Why use ThreadLocal for database routing?
 * 1. Web applications typically handle one request per thread
 * 2. We need to remember which database to use for the entire request processing
 * 3. ThreadLocal ensures the database choice is isolated to the current request/thread
 * 4. Prevents database context from leaking between requests
 *
 * This class is used in conjunction with:
 * 1. RoutingDataSource (extends AbstractRoutingDataSource)
 * 2. DataSourceConfig (configures multiple data sources)
 * 3. Controllers/Services that need to switch databases
 *
 * Typical usage flow:
 * 1. Before database operation: DbContextHolder.setDbType("mysql") or ("postgres")
 * 2. During operation: RoutingDataSource calls DbContextHolder.getDbType()
 * 3. After operation: DbContextHolder.clearDbType() (cleanup)
 *
 * SECURITY/CONCURRENCY NOTE:
 * ThreadLocal variables must be cleaned up after use to prevent memory leaks
 * in thread pools (like those used in web servers).
 */
public class DbContextHolder {

    /**
     * ThreadLocal variable to store the current database type for each thread.
     *
     * private: Accessible only within this class (encapsulation)
     * static: Belongs to class, not instance (shared across all instances)
     * final: Cannot be reassigned (immutable reference)
     *
     * ThreadLocal<String>: Each thread will have its own String value
     * The String value represents the database type: "mysql" or "postgres"
     *
     * ThreadLocal is initialized with no initial value (each thread starts with null)
     * This means getDbType() will return null until setDbType() is called
     */
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * Sets the database type for the current thread.
     *
     * @param dbType The database type to use for the current thread.
     *               Expected values: "mysql" or "postgres" (based on DataSourceConfig)
     *               Should match the keys used in RoutingDataSource targetDataSources map
     *
     * Usage example in a controller or service:
     *   DbContextHolder.setDbType("postgres");
     *   // Perform database operations
     *   // They will use PostgreSQL database
     *   DbContextHolder.clearDbType(); // Clean up
     *
     * Important: This method must be called before any database operations
     * in the current thread that need to use the specified database.
     */
    public static void setDbType(String dbType) {
        // ThreadLocal.set() stores the value for the current thread
        // Other threads cannot see or modify this value
        contextHolder.set(dbType);
    }

    /**
     * Gets the database type for the current thread.
     *
     * @return String representing the current database type, or null if not set
     *
     * This method is typically called by RoutingDataSource.determineCurrentLookupKey()
     * to decide which data source to use for the current operation.
     *
     * Example implementation in RoutingDataSource:
     *   @Override
     *   protected Object determineCurrentLookupKey() {
     *       return DbContextHolder.getDbType();
     *   }
     *
     * If this returns null, RoutingDataSource will use the default target data source
     * (MySQL, as configured in DataSourceConfig.routingDataSource())
     */
    public static String getDbType() {
        // ThreadLocal.get() retrieves the value for the current thread
        // Returns null if no value has been set for the current thread
        return contextHolder.get();
    }

    /**
     * Clears the database type for the current thread.
     *
     * IMPORTANT: This method MUST be called to prevent memory leaks.
     *
     * Why clear ThreadLocal?
     * 1. Web servers use thread pools (threads are reused for multiple requests)
     * 2. If we don't clear ThreadLocal, the database type from a previous request
     *    might be used for a subsequent request on the same thread
     * 3. This could cause incorrect database routing or security issues
     * 4. Memory leak: ThreadLocal values prevent ThreadLocal objects from being garbage collected
     *
     * Best practice: Always use try-finally to ensure cleanup:
     *
     *   DbContextHolder.setDbType("postgres");
     *   try {
     *       // Perform database operations
     *   } finally {
     *       DbContextHolder.clearDbType(); // Always clean up
     *   }
     */
    public static void clearDbType() {
        // ThreadLocal.remove() removes the value for the current thread
        // This is crucial for preventing memory leaks in thread pools
        contextHolder.remove();
    }

    // Note: Additional methods that could be useful

    /**
     * Check if database type is set for current thread
     *
     * public static boolean isDbTypeSet() {
     *     return contextHolder.get() != null;
     * }
     */

    /**
     * Get database type with default fallback
     *
     * public static String getDbTypeOrDefault(String defaultDb) {
     *     String dbType = contextHolder.get();
     *     return dbType != null ? dbType : defaultDb;
     * }
     */

    /**
     * ALTERNATIVE APPROACHES:
     *
     * 1. Using InheritableThreadLocal (if you need child threads to inherit the value)
     *    private static final InheritableThreadLocal<String> contextHolder = 
     *        new InheritableThreadLocal<>();
     *
     * 2. Using RequestContextHolder from Spring Web (for web applications)
     *    Store the value in request attributes instead of ThreadLocal
     *
     * 3. Using MDC (Mapped Diagnostic Context) from SLF4J for logging context
     *    Can store database type for logging purposes
     */

    /**
     * COMMON MISTAKES AND BEST PRACTICES:
     *
     * 1. Memory Leaks: Always call remove() in finally block
     * 2. Initial Value: Consider providing default value if needed
     * 3. Testing: ThreadLocal can make testing difficult (need to set/clear in tests)
     * 4. Async Operations: ThreadLocal doesn't work with @Async or CompletableFuture
     *    (values are not propagated to new threads)
     *
     * SPRING INTEGRATION:
     *
     * For better integration with Spring, consider:
     * 1. Using @Aspect to automatically set/clear ThreadLocal around methods
     * 2. Using HandlerInterceptor to set ThreadLocal for web requests
     * 3. Using TransactionSynchronizationManager for transaction-aware context
     */

    /**
     * EXAMPLE COMPLETE USAGE:
     *
     * // In a service method that needs to use PostgreSQL
     * public void someMethod() {
     *     // Set database context
     *     DbContextHolder.setDbType("postgres");
     *
     *     try {
     *         // Perform database operations
     *         userRepository.save(user); // Will use PostgreSQL
     *         bookingRepository.save(booking); // Will use PostgreSQL
     *     } finally {
     *         // Always clean up
     *         DbContextHolder.clearDbType();
     *     }
     * }
     *
     * // In RoutingDataSource class
     * public class RoutingDataSource extends AbstractRoutingDataSource {
     *     @Override
     *     protected Object determineCurrentLookupKey() {
     *         return DbContextHolder.getDbType();
     *     }
     * }
     */
}