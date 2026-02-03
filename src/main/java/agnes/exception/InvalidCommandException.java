package agnes.exception;

/**
 * Signals that a command entered by the user is invalid (could be empty).
 */
public class InvalidCommandException extends Exception {
    public InvalidCommandException(String message) {
        super(message);
    }
}
