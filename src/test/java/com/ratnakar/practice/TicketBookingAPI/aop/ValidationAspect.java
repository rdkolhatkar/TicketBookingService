package com.ratnakar.practice.TicketBookingAPI.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ValidationAspect {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ValidationAspect.class);

    @Around("execution(* com.ratnakar.practice.TicketBookingAPI.service.*.getUsers(..)) && args(userId)")
    public Object validateAndUpdateMethodGetUserById(
            ProceedingJoinPoint joinPoint,
            String userId) throws Throwable {

        // ============================
        // INPUT VALIDATION
        // ============================
        if (userId == null || userId.isBlank()) {
            LOGGER.error("userId is empty or null in the API request");

            // STOP execution and inform caller
            throw new IllegalArgumentException("UserId must not be null or empty");
        }

        // ============================
        // PROCEED WITH METHOD EXECUTION
        // ============================
        return joinPoint.proceed();
    }
}
