package exceptions;

import java.io.IOException;

public class ManagerSaveException extends IOException {

    public ManagerSaveException(final String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
