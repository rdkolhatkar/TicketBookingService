package com.ratnakar.practice.TicketBookingAPI.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DbUtils {

    @Autowired
    private DataSource routingDataSource;

    public Connection getConnection() throws SQLException {
        try {
            DbContextHolder.setDbType("mysql");
            return routingDataSource.getConnection();
        } catch (Exception e) {
            System.out.println("MySQL failed, switching to PostgreSQL");
            DbContextHolder.setDbType("postgres");
            return routingDataSource.getConnection();
        } finally {
            DbContextHolder.clearDbType();
        }
    }
}
