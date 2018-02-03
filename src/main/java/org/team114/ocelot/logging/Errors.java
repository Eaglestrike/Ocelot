package org.team114.ocelot.logging;

import java.util.function.Supplier;

public class Errors {
    private static final Errors ourInstance = new Errors();
    public static final Logger<ErrorItem> logger = new Logger<>();

    public static Errors getInstance() {
        return ourInstance;
    }

    private Errors() {

    }

    public static void log(String message) {
        logger.log(new ErrorItem(message));
    }

    public static void assertThat(boolean assertion, Supplier<String> messageSource) {
        if (!assertion) {
            String message = messageSource.get();
            System.out.print("ERROR: ");
            System.out.println(message);
            log(message);
            throw new IllegalStateException(message);
        }
    }
}
