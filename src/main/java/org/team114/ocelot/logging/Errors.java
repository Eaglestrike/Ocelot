package org.team114.ocelot.logging;

import java.util.function.Supplier;

/**
 * Global error logging system.
 */
public class Errors {
    /**
     * The global logger.
     */
    public static final Logger<ErrorItem> logger = new Logger<>();

    private Errors() {
    }

    /**
     * Logs a new error on the global logger.
     * @param message message for the error
     */
    public static void log(String message) {
        logger.log(new ErrorItem(message));
    }

    /**
     * Asserts that something is true, logging and throwing an error if it isn't.
     * @param assertion condition which must be true
     * @param messageSource a supplied for a message to log if assertion is false.
     * @throws AssertionError if assertion is false
     */
    public static void assertThat(boolean assertion, Supplier<String> messageSource) {
        if (assertion) {
            return;
        }

        String message = messageSource.get();
        //TODO: log should print to console, too
        System.out.println("ERROR: " + message);
        log(message);
        throw new AssertionError(message);
    }
}
