package agnes.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import agnes.util.DateTimeUtil;

/**
 * Represents an Event task with a description, completion status and
 * from and to date.
 * <p>
 * A {@code Event} extends {@link Task} by adding a from and to date-time field.
 * It supports date-based filtering and formatted display of the event.
 * Event are considered to fall on a date if that calendar date falls
 * in the event's duration (time is ignored for comparison).
 */
public class Event extends Task {
    protected LocalDateTime from, to;

    public Event(String message, LocalDateTime from, LocalDateTime to) {
        super(message);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format(
                "[E] %s (from: %s to: %s)",
                super.toString(),
                DateTimeUtil.formatDateTime(from),
                DateTimeUtil.formatDateTime(to)
        );
    }

    @Override
    public String toFileFormat() {
        return String.format(
                "E | %s | from %s to: %s",
                super.toFileFormat(),
                DateTimeUtil.formatDateTime(from),
                DateTimeUtil.formatDateTime(to)
        );
    }

    /**
     * Determines whether this event occurs on the specified date.
     * <p>
     * An {@code Event} is considered to fall on a date if that date is within
     * the event's duration, inclusive of both the start ({@code from}) and
     * end ({@code to}) dates. The time component is ignored during comparison;
     * only the calendar date is considered.
     *
     * @param date The date to check against the event's start and end dates.
     * @return {@code true} if the given date is on or between the event's
     *         start and end dates, otherwise {@code false}.
     */
    @Override
    public boolean fallsOnDate(LocalDate date) {
        LocalDate start = this.from.toLocalDate();
        LocalDate end = this.to.toLocalDate();

        return (date.equals(start) || date.isAfter(start)) &&
                (date.equals(end) || date.isBefore(end));
    }
}

