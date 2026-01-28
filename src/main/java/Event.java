public class Event extends Task {
    protected String from, to;

    public Event(String message, String from, String to) {
        super(message);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format(
                "[E] %s (from: %s to: %s)",
                super.toString(),
                this.from,
                this.to
        );
    }

    @Override
    public String toFileFormat() {
        return String.format(
                "E | %s | from %s to: %s",
                super.toFileFormat(),
                this.from,
                this.to
        );
    }
}

