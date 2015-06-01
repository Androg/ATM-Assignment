package se.kordev.atm.exception;

public final class ATMSecurityException extends RuntimeException {
    public ATMSecurityException(String message) {
        super(message);
    }

    public ATMSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
