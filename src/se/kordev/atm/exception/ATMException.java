package se.kordev.atm.exception;

public final class ATMException extends RuntimeException {
    public ATMException(String message) {
        super(message);
    }

    public ATMException(String message, Throwable cause) {
        super(message, cause);
    }
}
