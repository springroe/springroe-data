package io.github.springroe.data.core.exception;

public abstract class AbstractException extends RuntimeException {
    String errorCode = null;

    public AbstractException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AbstractException(String message, Throwable throwable, String errorCode) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    public AbstractException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public AbstractException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public AbstractException(String errorCode) {
        this.errorCode = errorCode;
    }
}
