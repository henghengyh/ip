package agnes.task;

public class ToDo extends Task {

    public ToDo(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return String.format("[T] %s", super.toString());
    }

    @Override
    public String toFileFormat() {
        return String.format(
                "T | %s",
                super.toFileFormat()
        );
    }
}
