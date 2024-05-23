package it.polimi.ingsw.am32.Utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is a custom uncaught exception handler that logs the critical error and terminates the application.
 *
 * <p>
 * The purpose of this class is to handle uncaught exceptions that are critical and unrecoverable. In such cases,
 * it's often a good idea to stop the server because:
 * <ul>
 * <li>Continuing the execution might lead to inconsistent state of the application which can be harder to debug and fix.</li>
 * <li>It prevents the propagation of the error, which might cause more damage to the system.</li>
 * <li>It allows the system to be restarted in a known, stable state.</li>
 * </ul>
 * </p>
 *
 * <p>
 * We have implemented various measures to avoid RuntimeExceptions, but there are still some critical cases that are
 * unavoidable. In these cases, it's better to fail fast and stop the server, rather than allowing it to continue running
 * in an erroneous state.
 * </p>
 *
 * <p>
 * This class implements the {@link Thread.UncaughtExceptionHandler} interface, which means it can be used as a default
 * handler for uncaught exceptions by calling {@link Thread#setDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler)}.
 * </p>
 *
 * @see Thread.UncaughtExceptionHandler
 */
public class CriticalExceptionHandler implements Thread.UncaughtExceptionHandler {
    // Logger for logging the critical error
    private final Logger logger = LogManager.getLogger(CriticalExceptionHandler.class);

    /**
     * This method is called when an uncaught exception occurs in a thread.
     * It logs the exception and terminates the application.
     * @param t The thread in which the exception occurred.
     * @param e The exception that was thrown.
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // Log the exception
        logger.fatal("Critical error in thread: {}", t.getName(), e);
        // Exit the application
        System.exit(1);
    }
}
