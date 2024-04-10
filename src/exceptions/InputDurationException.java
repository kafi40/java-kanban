package exceptions;

public class InputDurationException extends NumberFormatException {

    private final Object inputValue;

    public InputDurationException (String message, final String inputValue) {
        super(message);
        this.inputValue = inputValue;
    }

    public InputDurationException (String message, final int inputValue) {
        super(message);
        this.inputValue = inputValue;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " " + inputValue;
    }
}
