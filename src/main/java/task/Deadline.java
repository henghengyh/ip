package task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected LocalDateTime by;
    private final DateTimeFormatter formatDTH = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

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
                "E | %s | %s",
                super.toFileFormat(),
                DateTimeUtil.formatDateTime(this.by)
        );
    }

    @Override
    public boolean fallsOnDate(LocalDate date) {
        return this.by.toLocalDate().equals(date);
    }
}
