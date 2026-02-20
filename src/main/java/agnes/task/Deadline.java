package agnes.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import agnes.util.DateTimeUtil;

/**
 * Represents a Deadline task with a description, completion status and
 * due date.
 * <p>
 * A {@code Deadline} extends {@link Task} by adding a due date-time field.
 * It supports date-based filtering and formatted display of the deadline.
 * Deadlines are considered to fall on a date if their due date matches
 * that calendar date (time is ignored for comparison).
 */
public class Deadline extends Task {
    protected LocalDateTime by;
    private final DateTimeFormatter formatDth = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /**
     * Constructs a Deadline task with a description and due date-time.
     *
     * @param message   Description of the task.
     * @param by        Due date (and time; optional) of the task.
     */
    public Deadline(String message, LocalDateTime by) {
        super(message);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format(
                "[D] %s (by: %s)",
                super.toString(),
                DateTimeUtil.formatDateTime(this.by)
        );
    }

    @Override
    public String toFileFormat() {
        return String.format(
                "D | %s | %s",
                super.toFileFormat(),
                DateTimeUtil.formatDateTime(this.by)
        );
    }

    /**
     * Determines whether this deadline occurs on the specified date.
     * <p>
     * A {@code Deadline} is considered to fall on a date if its due
     * date-time ({@code by}) has the same calendar date, ignoring the time.
     *
     * @param date The date to compare against the deadline's due date.
     * @return {@code true} if the deadline is due on the given date,
     *         otherwise {@code false}.
     */
    @Override
    public boolean fallsOnDate(LocalDate date) {
        return this.by.toLocalDate().equals(date);
    }

    /**
     * Updates a field of a {@code Deadline}.
     *
     * @param field    The field to be updated.
     * @param value    The value to be placed to the field.
     * @throws IllegalArgumentException If the field or value is invalid.
     */
    @Override
    public void update(String field, String value) throws IllegalArgumentException {
        try {
            switch (field.toLowerCase()) {
            case "/description":
                this.setMessage(value);
                break;
            case "/by":
                this.by = DateTimeUtil.parseDateTime(value);
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown field for deadline: " + field
                        + "Available fields: /description, /by"
                );
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Date format should be yyyy-MM-dd or yyyy-MM-dd HHmm"
            );
        }

    }
}
