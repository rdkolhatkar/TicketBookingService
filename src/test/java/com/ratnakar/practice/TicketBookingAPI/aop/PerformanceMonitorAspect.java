package com.ratnakar.practice.TicketBookingAPI.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceMonitorAspect {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PerformanceMonitorAspect.class);

    // @Around aspect will always return the Object type data and that's why we have given the return type as Object to this Method.
    @Around("com.ratnakar.practice.TicketBookingAPI.aop.CommonPointcuts.serviceLayer()")
    public Object monitorTimeForDatabaseQuery(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // Here we have to check the Amount of time taken by the API to Execute DB Query to check if a user already exists in the database by username.
        // "checkUserAlreadyExists" this method will run the JPA Query and fetch the user data by UserName {userRepository.findById(userName)}
        // To check the Actual execution time for this method, we will log the current time when Query execution started and we will again log the current time when Query execution ended.
        // Amount of time taken by the method "checkUserAlreadyExists" is equal to "end_Time - start_Time"

        long start_Time = System.currentTimeMillis();
        Object obj = proceedingJoinPoint.proceed();
        long end_Time = System.currentTimeMillis();

        LOGGER.info("Time taken by : " + proceedingJoinPoint.getSignature().getName() + " "+ (end_Time - start_Time) + " ms");

        return obj;
    }
}
