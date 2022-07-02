package io.datalake.exception;

public class DataLakeException extends RuntimeException {

    private DataLakeException(String message) {
        super(message);
    }

    private DataLakeException(Throwable t) {
        super(t);
    }

    public static void throwException(String message) {
        throw new DataLakeException(message);
    }

    public static DataLakeException getException(String message) {
        throw new DataLakeException(message);
    }

    public static void throwException(Throwable t) {
        throw new DataLakeException(t);
    }
}
