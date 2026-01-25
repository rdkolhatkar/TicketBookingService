package com.ratnakar.practice.TicketBookingAPI.utils;

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

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

    // MySQL
    @Value("${spring.datasource.url}")
    private String mysqlUrl;
    @Value("${spring.datasource.username}")
    private String mysqlUsername;
    @Value("${spring.datasource.password}")
    private String mysqlPassword;

    // PostgreSQL
    @Value("${postgres.datasource.url}")
    private String postgresUrl;
    @Value("${postgres.datasource.username}")
    private String postgresUsername;
    @Value("${postgres.datasource.password}")
    private String postgresPassword;

    @Bean
    public DataSource mysqlDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(mysqlUrl);
        ds.setUsername(mysqlUsername);
        ds.setPassword(mysqlPassword);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return ds;
    }

    @Bean
    public DataSource postgresDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(postgresUrl);
        ds.setUsername(postgresUsername);
        ds.setPassword(postgresPassword);
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }

    @Bean
    public DataSource routingDataSource(@Qualifier("mysqlDataSource") DataSource mysql,
                                        @Qualifier("postgresDataSource") DataSource postgres) {
        RoutingDataSource routing = new RoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("mysql", mysql);
        targetDataSources.put("postgres", postgres);
        routing.setTargetDataSources(targetDataSources);
        routing.setDefaultTargetDataSource(mysql);
        return routing;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource routingDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(routingDataSource);
        em.setPackagesToScan("com.ratnakar.practice.TicketBookingAPI.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }
}
