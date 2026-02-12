package agnes.task;

import java.time.format.DateTimeParseException;

import agnes.util.DateTimeUtil;

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

    /**
     * Updates a field of a {@code ToDo}.
     *
     * @param field    The field to be updated.
     * @param value    The value to be placed to the field.
     * @throws IllegalArgumentException If the field is invalid.
     */
    @Override
    public void update(String field, String value) throws IllegalArgumentException {
        switch (field.toLowerCase()) {
        case "/description":
            this.setMessage(value);
            break;
        default:
            throw new IllegalArgumentException(
                    "Unknown field for event: " + field
                            + "Available fields: /description"
            );
        }
    }
}
