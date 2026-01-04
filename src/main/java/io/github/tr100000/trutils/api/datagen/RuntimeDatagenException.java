package io.github.tr100000.trutils.api.datagen;

public class RuntimeDatagenException extends RuntimeException {
    public RuntimeDatagenException() {
        super();
    }

    public RuntimeDatagenException(String message) {
        super(message);
    }

    public RuntimeDatagenException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeDatagenException(Throwable cause) {
        super(cause);
    }

    public RuntimeDatagenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
