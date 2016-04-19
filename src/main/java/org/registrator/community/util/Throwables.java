package org.registrator.community.util;

/**
 * Returns the innermost cause of {@code throwable}. The first throwable in a chain provides
 * context from when the error or exception was initially detected. Example usage:
 *
 * <pre>
 * assertEquals("Unable to assign a customer id", Throwables.getRootCause(e).getMessage());
 * </pre>
 */
public class Throwables {
    public static Throwable getRootCause(Throwable throwable) {
        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
        }
        return throwable;
    }
}
