package com.exception;

public class TokenRefreshException extends MyException {
    public TokenRefreshException() {
    }

    public TokenRefreshException(String message) {
        super(message);
    }

    public TokenRefreshException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenRefreshException(Throwable cause) {
        super(cause);
    }
}
