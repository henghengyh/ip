package agnes.exception;

/**
 * Signals that a task description provided by the user is invalid (could be empty).
 */
public class InvalidDescriptionException extends Exception {
    public InvalidDescriptionException(String message) {
        super(message);
    }
}
