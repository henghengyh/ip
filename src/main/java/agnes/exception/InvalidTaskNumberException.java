package agnes.exception;

/**
 * Signals that the task number provided by the user is not a valid positive integer.
 * Note that this does not imply out-of-bounds of the TaskList.
 */
public class InvalidTaskNumberException extends Exception {
    public InvalidTaskNumberException(String message) {
        super(message);
    }
}
