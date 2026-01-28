import java.time.LocalDateTime;

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
}

