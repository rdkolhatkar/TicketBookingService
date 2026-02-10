package com.ratnakar.practice.TicketBookingAPI.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * CommonPointcuts
 * ----------------
 * This class contains reusable Pointcut expressions.
 *
 * WHY THIS CLASS IS REQUIRED:
 * ----------------------------
 * - Defining pointcuts directly inside multiple Aspect classes
 *   can cause AspectJ parsing issues at application startup.
 *
 * - In your case, Spring failed with:
 *   "invalidAbsoluteTypeName"
 *
 * - To avoid this, we define pointcuts ONCE and reuse them.
 *
 * BENEFITS:
 * ----------
 * ✔ Clean separation of concerns
 * ✔ Reusable pointcuts
 * ✔ Avoids AspectJ startup failures
 * ✔ Easy maintenance
 */
public class CommonPointcuts {

    /**
     * serviceLayer()
     * ----------------
     * This Pointcut matches:
     *
     * - Any return type (*)
     * - Any method
     * - Any number of arguments
     * - Inside ANY class
     * - Under package:
     *   com.ratnakar.practice.TicketBookingAPI.service
     *   and its sub-packages
     *
     * SYNTAX EXPLANATION:
     * -------------------
     * execution(* package..*(..))
     *
     * ..  -> matches sub-packages
     * *   -> matches any class / method / return type
     */
    @Pointcut("execution(* com.ratnakar.practice.TicketBookingAPI.service..*(..))")
    public void serviceLayer() {}
}
