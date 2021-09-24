package io.github.springroe.data.core.exception;

public class DataException extends AbstractException{

    public DataException(String message, String errorCode) {
        super(message, errorCode);
    }

    public DataException(String message, Throwable throwable, String errorCode) {
        super(message, throwable, errorCode);
    }

    public DataException(Throwable cause, String errorCode) {
        super(cause, errorCode);
    }

    public DataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }

    public DataException(String errorCode) {
        super(errorCode);
    }
}
