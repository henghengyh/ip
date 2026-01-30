package agnes.task;

import java.time.LocalDate;

/**
 * Represents a generic task with a description and completion status.
 * <p>
 * A {@code Task} can be marked as complete or incomplete and provides
 * a basic text. Specific task types (e.g., deadlines or events) may
 * extend this class to include specific behavior.
 */
public class Task {
    private final String message;
    private boolean isComplete;

    /**
     * Creates a task with the specified description.
     * The task is initially marked as incomplete.
     *
     * @param message The description of the task.
     */
    public Task(String message) {
        this.message = message;
        this.isComplete = false;
    }

    /** Marks a task as complete. */
    public void mark() {
        this.isComplete = true;
    }

    /** Marks a task as incomplete. */
    public void unmark() {
        this.isComplete = false;
    }

    /**
     * Returns the description of the {@code Task}.
     *
     * @return The message describing the task.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Returns the completion status icon used in text display.
     *
     * @return "X" if the task is complete, otherwise a space character.
     */
    public String getStatusIcon() {
        return (isComplete ? "X" : " "); // mark done agnes.task with X
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.getMessage());
    }

    /**
     * Converts this task into a string format suitable for file storage.
     *
     * @return The file-string representation of the task.
     */
    public String toFileFormat() {
        return String.format(
                "%s | %s",
                isComplete ? "1" : 0,
                this.getMessage());
    }

    /**
     * Determines whether this task occurs on the specified date.
     * <p>
     * The base {@code Task} does not contain date information and therefore
     * always returns {@code false}. Subclasses with date fields will override
     * this method.
     *
     * @param date The date to check.
     * @return {@code true} if the task occurs on that date, otherwise {@code false}.
     */
    public boolean fallsOnDate(LocalDate date) {
        return false;
    }
}
