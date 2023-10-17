package com.exception;

public class FileCompressException extends MyException {
    public FileCompressException() {
    }

    public FileCompressException(String message) {
        super(message);
    }

    public FileCompressException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileCompressException(Throwable cause) {
        super(cause);
    }
}
