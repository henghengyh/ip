public class Task {
    private String message;
    private boolean complete;

    public Task(String message) {
        this.message = message;
        this.complete = false;
    }

    public void mark() {
        this.complete = true;
    }

    public void unmark() {
        this.complete = false;
    }

    public String getMessage() {
        return this.message;
    }

    public String getStatusIcon() {
        return (complete ? "X" : " "); // mark done task with X
    }

    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.getMessage());
    }

    public String toFileFormat() {
        return String.format(
                "%s | %s",
                complete ? "1" : 0,
                this.getMessage());
    }
}
