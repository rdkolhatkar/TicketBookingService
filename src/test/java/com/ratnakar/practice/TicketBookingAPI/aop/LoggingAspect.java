package com.ratnakar.practice.TicketBookingAPI.aop;

import org.aspectj.lang.JoinPoint;
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
    /**
     * ============================
     * JOINPOINT IN SPRING AOP
     * ============================
     *
     * JoinPoint represents a point during the execution of a program
     * where an aspect can be applied.
     *
     * In Spring AOP, a JoinPoint typically represents:
     * - A method execution
     *
     * --------------------------------
     * WHEN IS JoinPoint USED?
     * --------------------------------
     * JoinPoint is used inside advice methods (@Before, @After, @Around, etc.)
     * to access runtime information about the intercepted method.
     *
     * --------------------------------
     * WHAT INFORMATION DOES JoinPoint PROVIDE?
     * --------------------------------
     * Using JoinPoint, you can access:
     *
     * 1. Method Name
     *    -> joinPoint.getSignature().getName()
     *
     * 2. Class Name
     *    -> joinPoint.getTarget().getClass().getSimpleName()
     *
     * 3. Fully Qualified Method Signature
     *    -> joinPoint.getSignature().toString()
     *
     * 4. Method Arguments
     *    -> joinPoint.getArgs()
     *
     * 5. Target Object (Actual Bean)
     *    -> joinPoint.getTarget()
     *
     * 6. Proxy Object (Spring-created proxy)
     *    -> joinPoint.getThis()
     *
     * --------------------------------
     * IMPORTANT RULES
     * --------------------------------
     * - JoinPoint can be used only as a method parameter
     *   in advice methods.
     *
     * - JoinPoint is OPTIONAL:
     *   If you don’t need method details, you can omit it.
     *
     * - JoinPoint is READ-ONLY:
     *   You can inspect data but cannot change method arguments
     *   (use ProceedingJoinPoint for that in @Around advice).
     *
     * --------------------------------
     * EXAMPLE POINTCUT USED HERE
     * --------------------------------
     *
     * execution(* com.ratnakar.practice.TicketBookingAPI.service.UserRegistrationService*(..))
     *
     * This matches:
     * - Any return type
     * - Any method
     * - Any arguments
     * - Any class starting with UserRegistrationService
     * - Inside the specified service package
     *
     * --------------------------------
     * WHAT THIS ADVICE DOES
     * --------------------------------
     * - Runs BEFORE the matched service method
     * - Logs the method name using JoinPoint
     *
     * --------------------------------
     * SAMPLE OUTPUT
     * --------------------------------
     * Method is called registerUser
     * Method is called validateUser
     *
     */
    @Before(
            "execution(* com.ratnakar.practice.TicketBookingAPI.service.UserRegistrationService*(..))"
    )
    public void logMethodCall(JoinPoint joinPoint) {

        // Logs the name of the intercepted method
        LOGGER.info("Method is called " + joinPoint.getSignature().getName());
    }

}
