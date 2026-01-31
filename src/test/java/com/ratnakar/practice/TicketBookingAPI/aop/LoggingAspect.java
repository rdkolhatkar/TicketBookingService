package com.ratnakar.practice.TicketBookingAPI.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * LoggingAspect
 * --------------
 * This class is an Aspect that handles logging concerns
 * using Spring AOP (Aspect-Oriented Programming).
 */
@Component // Marks this class as a Spring-managed bean
@Aspect    // Indicates this class contains AOP logic
public class LoggingAspect {

    /**
     * Logger instance used for logging method execution details.
     * SLF4J is used as a logging abstraction.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * ============================
     * STANDARD @Before ADVICE SYNTAX
     * ============================
     *
     * @Before("execution(return-type package.class.method(arguments))")
     *
     * ----------- Breakdown ----------
     *
     * @Before           -> Type of advice (runs BEFORE method execution)
     *
     * execution(...)    -> Pointcut designator that matches method execution
     *
     * return-type       -> *
     *                      (matches any return type: void, int, String, Object)
     *
     * package           -> Fully qualified package name
     *
     * class             -> Target class name
     *                      Wildcards (*) are allowed
     *
     * method            -> Target method name
     *                      Wildcards (*) are allowed
     *
     * arguments         -> Method parameters
     *                      ()   -> no arguments
     *                      (..) -> any number of arguments
     *
     * --------------------------------
     * Example (Generic):
     *
     * @Before("execution(* com.example.service.MyService.save(..))")
     *
     * --------------------------------
     * Example (Your Case):
     *
     * @Before("execution(* com.ratnakar.practice.TicketBookingAPI.service.UserServiceImpl*(..))")
     *
     * This matches:
     * - Any return type
     * - Any method
     * - Any arguments
     * - In any class whose name starts with UserServiceImpl
     * - Inside the specified package
     */
    @Before(
            "execution(* com.ratnakar.practice.TicketBookingAPI.service.UserServiceImpl*(..))"
    )
    public void logMethodCall() {

        // This log statement executes BEFORE the target service method
        LOGGER.info("Method is called");
    }
}
