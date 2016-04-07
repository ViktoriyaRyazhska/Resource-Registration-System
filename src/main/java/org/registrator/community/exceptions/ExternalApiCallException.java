package org.registrator.community.exceptions;

/**
 * Thrown when there is exception when calling external API
 */
public class ExternalApiCallException extends Exception {

    public ExternalApiCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalApiCallException(String message) {
        super(message);
    }
}
