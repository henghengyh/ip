package agnes.task;

/**
 * Represents a ToDo task with a description, completion status.
 * <p>
 * A {@code ToDo} extends {@link Task}.
 * It supports formatted display of the ToDo.
 */
public class ToDo extends Task {

    public ToDo(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return String.format("[T] %s", super.toString());
    }

    @Override
    public String toFileFormat() {
        return String.format(
                "T | %s",
                super.toFileFormat()
        );
    }
}
