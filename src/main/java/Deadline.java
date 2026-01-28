public class Deadline extends Task {
    protected String by;

    public Deadline(String message, String by) {
        super(message);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format(
                "[D] %s (by: %s)",
                super.toString(),
                this.by);
    }

    @Override
    public String toFileFormat() {
        return String.format(
                "E | %s | %s",
                super.toFileFormat(),
                this.by
        );
    }
}
