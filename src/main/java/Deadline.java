public class Deadline extends Task {
    protected String by;

    public Deadline(String message, String by) {
        super(message);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format("[D][%s] %s (by: %s)", super.getStatusIcon(), super.getMessage(), this.by);
    }
}
