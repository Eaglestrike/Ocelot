package org.team114.ocelot.logging;

/**
 * Represents an error and message.
 */
public class ErrorItem extends AbstractLoggable  {
    public final String message;

    ErrorItem(String message) {
        this.message = message;
    }
}
