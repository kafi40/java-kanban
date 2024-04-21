package exceptions;

public class TimeIntersectionException extends RuntimeException {

    public TimeIntersectionException(final String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
