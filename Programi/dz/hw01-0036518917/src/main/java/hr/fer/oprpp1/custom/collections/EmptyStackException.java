package hr.fer.oprpp1.custom.collections;

public class EmptyStackException extends RuntimeException {
    private static final long SERIAL_VERSION_UUID = 1L;

    public EmptyStackException() {
        super();
    }

    public EmptyStackException(String message) {
        super(message);
    }
}
