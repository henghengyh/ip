public class ToDo extends Task {

    public ToDo(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return String.format("[T][%s] %s", super.getStatusIcon(), super.getMessage());
    }
}
