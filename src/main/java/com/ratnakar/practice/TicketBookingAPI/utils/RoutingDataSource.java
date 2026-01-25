package com.ratnakar.practice.TicketBookingAPI.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // Use ThreadLocal to track which DB to use
        // Default is "mysql"
        String dbKey = DbContextHolder.getDbType();
        return dbKey != null ? dbKey : "mysql";
    }
}
