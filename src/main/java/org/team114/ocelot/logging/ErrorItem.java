package org.team114.ocelot.logging;

public class ErrorItem extends AbstractLoggable  {
    public final String message;

    ErrorItem(String message) {
        this.message = message;
    }
}
