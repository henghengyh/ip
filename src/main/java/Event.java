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
                "[E][%s] %s (from: %s to: %s)",
                super.getStatusIcon(),
                super.getMessage(),
                this.from,
                this.to
        );
    }
}
