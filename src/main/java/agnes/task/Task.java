package agnes.task;

import java.time.LocalDate;

public class Task {
    private final String message;
    private boolean isComplete;

    public Task(String message) {
        this.message = message;
        this.isComplete = false;
    }

    public void mark() {
        this.isComplete = true;
    }

    public void unmark() {
        this.isComplete = false;
    }

    public String getMessage() {
        return this.message;
    }

    public String getStatusIcon() {
        return (isComplete ? "X" : " "); // mark done agnes.task with X
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.getMessage());
    }

    public String toFileFormat() {
        return String.format(
                "%s | %s",
                isComplete ? "1" : 0,
                this.getMessage());
    }

    public boolean fallsOnDate(LocalDate date) {
        return false;
    }
}
