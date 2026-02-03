package agnes.exception;

/**
 * Signals that the task index provided by the user does not exist in the TaskList.
 */
public class TaskIndexOutOfBoundsException extends Exception {
    public TaskIndexOutOfBoundsException(String message) {
        super(message);
    }
}
