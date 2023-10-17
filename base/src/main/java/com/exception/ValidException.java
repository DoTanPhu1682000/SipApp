package com.exception;

public class ValidException extends MyException {
    public ValidException() {
    }

    public ValidException(String message) {
        super(message);
    }

    public ValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidException(Throwable cause) {
        super(cause);
    }
}
