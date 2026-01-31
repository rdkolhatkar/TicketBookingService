// Package declaration: Organizes utility/configuration classes together
// This class handles database configuration and data source setup
package com.ratnakar.practice.TicketBookingAPI.utils;

// Spring Framework annotations and classes for configuration and dependency injection

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * DataSourceConfig - Configuration class for setting up multiple data sources and JPA.
 *
 * @Configuration annotation marks this class as a source of bean definitions.
 * Configuration classes are processed by Spring container to create and wire beans.
 * They replace traditional XML configuration files in Spring Boot applications.
 *
 * This class configures a multi-database setup with:
 * 1. MySQL data source
 * 2. PostgreSQL data source
 * 3. Routing data source that can switch between them
 * 4. JPA EntityManagerFactory with Hibernate
 * 5. Transaction manager for JPA
 *
 * MULTI-DATABASE ARCHITECTURE:
 * This configuration supports connecting to two different databases simultaneously.
 * Useful for scenarios like:
 * - Database migration (gradual move from one DB to another)
 * - Read-write splitting (write to one, read from another)
 * - Multi-tenant applications with different databases per tenant
 * - Legacy system integration
 */
@Configuration
public class DataSourceConfig {

    // MySQL database configuration properties
    // @Value annotation injects values from application.properties/yml

    /**
     * MySQL database URL.
     * Example format: jdbc:mysql://localhost:3306/ticket_booking_db
     * Injected from spring.datasource.url property in application.properties
     */
    @Value("${spring.datasource.url}")
    private String mysqlUrl;

    /**
     * MySQL database username.
     * Injected from spring.datasource.username property
     */
    @Value("${spring.datasource.username}")
    private String mysqlUsername;

    /**
     * MySQL database password.
     * Injected from spring.datasource.password property
     */
    @Value("${spring.datasource.password}")
    private String mysqlPassword;

    // PostgreSQL database configuration properties

    /**
     * PostgreSQL database URL.
     * Example format: jdbc:postgresql://localhost:5432/ticket_booking_db
     * Injected from postgres.datasource.url property
     */
    @Value("${postgres.datasource.url}")
    private String postgresUrl;

    /**
     * PostgreSQL database username.
     * Injected from postgres.datasource.username property
     */
    @Value("${postgres.datasource.username}")
    private String postgresUsername;

    /**
     * PostgreSQL database password.
     * Injected from postgres.datasource.password property
     */
    @Value("${postgres.datasource.password}")
    private String postgresPassword;

    /**
     * Creates and configures MySQL data source bean.
     *
     * @Bean annotation tells Spring to use this method to create a bean.
     * The bean name is the method name (mysqlDataSource).
     * This bean will be managed by Spring container and can be injected elsewhere.
     *
     * HikariDataSource is a high-performance JDBC connection pool.
     * It's the default connection pool in Spring Boot 2.x+.
     *
     * Connection pooling benefits:
     * 1. Reuses connections instead of creating new ones for each request
     * 2. Improves application performance
     * 3. Manages connection lifecycle
     * 4. Provides metrics and monitoring
     *
     * @return DataSource bean for MySQL database
     */
    @Bean
    public DataSource mysqlDataSource() {
        // Create HikariDataSource instance (connection pool)
        HikariDataSource ds = new HikariDataSource();
        // Set JDBC URL for MySQL database
        ds.setJdbcUrl(mysqlUrl);
        // Set database username
        ds.setUsername(mysqlUsername);
        // Set database password
        ds.setPassword(mysqlPassword);
        // Set JDBC driver class name for MySQL
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return ds;
    }

    /**
     * Creates and configures PostgreSQL data source bean.
     *
     * Similar to mysqlDataSource but for PostgreSQL.
     * Uses PostgreSQL JDBC driver.
     *
     * @return DataSource bean for PostgreSQL database
     */
    @Bean
    public DataSource postgresDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(postgresUrl);
        ds.setUsername(postgresUsername);
        ds.setPassword(postgresPassword);
        // PostgreSQL JDBC driver class
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }

    /**
     * Creates routing data source that can switch between MySQL and PostgreSQL.
     *
     * This bean uses a custom RoutingDataSource (not shown in this class,
     * but presumably extends AbstractRoutingDataSource from Spring).
     *
     * @Qualifier annotations specify which beans to inject when multiple
     * DataSource beans exist. Spring uses bean names (method names) as qualifiers.
     *
     * AbstractRoutingDataSource is a Spring class that dynamically determines
     * the actual DataSource to use based on a lookup key.
     *
     * @param mysql DataSource bean for MySQL (injected by name)
     * @param postgres DataSource bean for PostgreSQL (injected by name)
     * @return RoutingDataSource that can route to different databases
     */
    @Bean
    public DataSource routingDataSource(@Qualifier("mysqlDataSource") DataSource mysql,
                                        @Qualifier("postgresDataSource") DataSource postgres) {
        // Create instance of custom RoutingDataSource
        RoutingDataSource routing = new RoutingDataSource();

        // Create map of target data sources
        // The keys ("mysql", "postgres") are lookup keys used to determine target DataSource
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("mysql", mysql);
        targetDataSources.put("postgres", postgres);

        // Set the target data sources map
        routing.setTargetDataSources(targetDataSources);

        // Set default data source (used when no specific lookup key is provided)
        routing.setDefaultTargetDataSource(mysql);

        return routing;
    }

    /**
     * Creates JPA EntityManagerFactory bean.
     *
     * EntityManagerFactory is the main entry point for JPA.
     * It creates EntityManager instances that manage entities and database operations.
     *
     * LocalContainerEntityManagerFactoryBean gives full control over
     * EntityManagerFactory configuration and integrates with Spring's transaction management.
     *
     * @param routingDataSource The routing data source (injected by Spring)
     * @return LocalContainerEntityManagerFactoryBean for JPA
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource routingDataSource) {
        // Create EntityManagerFactory bean
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        // Set the data source (with routing capability)
        em.setDataSource(routingDataSource);

        // Set package(s) to scan for entity classes (JPA @Entity classes)
        // Spring will scan these packages and register all @Entity classes
        em.setPackagesToScan("com.ratnakar.practice.TicketBookingAPI.model");

        // Set Hibernate as JPA provider
        // HibernateJpaVendorAdapter adapts Hibernate to JPA specification
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Additional JPA properties could be set here:
        // Map<String, Object> properties = new HashMap<>();
        // properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        // properties.put("hibernate.hbm2ddl.auto", "update");
        // properties.put("hibernate.show_sql", "true");
        // em.setJpaPropertyMap(properties);

        return em;
    }

    /**
     * Creates JPA TransactionManager bean.
     *
     * JpaTransactionManager is Spring's transaction manager for JPA.
     * It manages transactions for JPA EntityManager operations.
     *
     * Transaction management ensures data integrity by grouping database
     * operations into atomic units (all succeed or all fail).
     *
     * @param emf EntityManagerFactory bean (injected by Spring)
     * @return JpaTransactionManager for managing JPA transactions
     */
    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        // Create JPA transaction manager with the EntityManagerFactory
        return new JpaTransactionManager(emf.getObject());
    }

    // Note: Missing parts and considerations

    /**
     * MISSING: RoutingDataSource class definition
     * The code uses RoutingDataSource but it's not defined in this file.
     * It should be a class that extends AbstractRoutingDataSource and
     * implements determineCurrentLookupKey() method.
     *
     * Example:
     * public class RoutingDataSource extends AbstractRoutingDataSource {
     *     @Override
     *     protected Object determineCurrentLookupKey() {
     *         // Return "mysql" or "postgres" based on some logic
     *         // Could use ThreadLocal, request header, configuration, etc.
     *         return DatabaseContextHolder.getCurrentDb();
     *     }
     * }
     */

    /**
     * CONFIGURATION PROPERTIES CONSIDERATIONS:
     *
     * For production, additional connection pool settings should be configured:
     *
     * For HikariCP:
     * - setMaximumPoolSize(): Maximum number of connections in pool
     * - setMinimumIdle(): Minimum idle connections
     * - setConnectionTimeout(): Timeout for getting connection from pool
     * - setIdleTimeout(): Time before idle connections are closed
     * - setMaxLifetime(): Maximum lifetime of a connection
     * - setConnectionTestQuery(): Query to validate connection health
     *
     * Example:
     *   ds.setMaximumPoolSize(20);
     *   ds.setMinimumIdle(5);
     *   ds.setConnectionTimeout(30000); // 30 seconds
     */

    /**
     * DATABASE-SPECIFIC CONSIDERATIONS:
     *
     * 1. MySQL:
     *    - Default port: 3306
     *    - Driver: com.mysql.cj.jdbc.Driver (for MySQL Connector/J 8+)
     *    - URL format: jdbc:mysql://host:port/database
     *
     * 2. PostgreSQL:
     *    - Default port: 5432
     *    - Driver: org.postgresql.Driver
     *    - URL format: jdbc:postgresql://host:port/database
     */

    /**
     * TRANSACTION MANAGEMENT CONSIDERATIONS:
     *
     * When using multiple data sources, consider:
     * 1. Distributed transactions (XA transactions) if operations span both databases
     * 2. ChainedTransactionManager for best-effort multi-database transactions
     * 3. Saga pattern for long-running transactions across multiple databases
     */

    /**
     * BEST PRACTICES:
     *
     * 1. Externalize configuration (use application.properties/yml)
     * 2. Use connection pooling (already using HikariCP)
     * 3. Configure proper connection pool settings for production
     * 4. Use database-specific dialects in JPA properties
     * 5. Consider flyway/liquibase for database migration management
     * 6. Use different EntityManagerFactory for each data source if entities differ
     * 7. Implement proper error handling and connection validation
     */
}