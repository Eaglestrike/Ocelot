package org.team114.ocelot.logging;

import java.util.function.Supplier;

public class Errors {
    public static final Logger<ErrorItem> logger = new Logger<>();

    private Errors() {
    }

    public static void log(String message) {
        logger.log(new ErrorItem(message));
    }

    public static void assertThat(boolean assertion, Supplier<String> messageSource) {
        if (assertion) {
            return;
        }

        String message = messageSource.get();
        //TODO: log should print to console, too
        System.out.println("ERROR: " + message);
        log(message);
        throw new IllegalStateException(message);
    }
}
