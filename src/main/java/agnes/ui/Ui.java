package agnes.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import agnes.Agnes;
import agnes.task.Task;
import agnes.task.TaskList;

/**
 * Handles all user interactions and output formatting for the Agnes application.
 * <p>
 * Provides methods to returns messages, errors, task lists, and task updates in a
 * consistent and user-friendly manner. Also formats outputs with dotted lines for clarity.
 * </p>
 */
public class Ui {

    private static final String LINE = "\t------------------------------------";

    /**
     * Wraps a message to be printed with the dotted lines for visual.
     * @param lines the message(s) to be processed.
     * @return the message wrapped with LINE.
     */
    private String wrap(String... lines) {
        assert lines != null : "Lines passed to wrap() should not be null";
        return String.join("\n", lines);
    }

    /**
     * Returns a welcome message when the conversation with the user starts.
     * @return the welcome message.
     */
    public List<String> getWelcomeMessage() {
        return List.of(wrap(
                "Hello thereeee! I'm " + Agnes.class.getSimpleName(),
                "What can I do for you?"
        ));
    }

    /**
     * Prints a goodbye message when the conversation with the user ends.
     * @return the goodbye message.
     */
    public List<String> getByeMessage() {
        return List.of(wrap("Goodbye! Have a wonderful day ahead!"));
    }

    /**
     * Returns a list of tasks in String format to be printed.
     * <p>
     * If the list is empty, returns a message indicating no tasks were found.
     * </p>
     *
     * @param tasks the list of tasks on the given date
     */
    public List<String> getTasks(TaskList tasks) {
        assert tasks != null : "TaskList should not be null when displaying tasks";
        if (tasks.size() == 0) {
            return List.of(wrap("No tasks in your list!"));
        }

        List<String> lines =
                java.util.stream.IntStream.range(0, tasks.size())
                        .mapToObj(i -> (i + 1) + ". " + tasks.get(i))
                        .collect(Collectors.toList());

        lines.add(0, "Here's everything you've got so far:");
        return List.of(wrap(lines.toArray(new String[0])));
    }

    /**
     * Returns an error message from an exception.
     *
     * @param e the exception containing the error message
     */
    public List<String> getErrorMessage(Exception e) {
        return List.of(wrap(e.getMessage()));
    }

    /**
     * Returns a String of a list of tasks scheduled for a specific date.
     * <p>
     * If the list is empty, returns a message indicating no tasks were found.
     * </p>
     *
     * @param tasks the list of tasks on the given date
     * @param date  the date for which tasks are required
     */
    public List<String> getTasksOnDate(List<Task> tasks, LocalDate date) {
        assert tasks != null : "Task list for date display should not be null";
        assert date != null : "Date for filtering should not be null";
        if (tasks.isEmpty()) {
            return List.of(wrap("AIYOOOOOO SWEE HENG LAH No tasks found on " + date));
        }

        List<String> lines = java.util.stream.IntStream.range(0, tasks.size())
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i))
                .collect(Collectors.toList());
        lines.add(0, "Here's what I found on " + date + ":");

        return List.of(wrap(lines.toArray(new String[0])));
    }

    /**
     * Returns a message of a list of tasks containing a certain given keyword.
     * <p>
     * If the list is empty, returns a message indicating no tasks were found.
     * </p>
     *
     * @param tasks     the list of tasks containing the given keyword
     * @param keyword   the keyword to search for
     */
    public List<String> getSearchTasks(List<Task> tasks, String keyword) {
        assert tasks != null : "Search results list should not be null";
        assert keyword != null : "Search keyword should not be null";
        if (tasks.isEmpty()) {
            return List.of(wrap("No tasks with keyword: " + keyword));
        }

        List<String> lines = java.util.stream.IntStream.range(0, tasks.size())
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i))
                .collect(Collectors.toList());
        lines.add(0, "\n"
                + "Here's what I found for \"" + keyword + "\":");

        return List.of(wrap(lines.toArray(new String[0])));
    }


    /**
     * Returns a message indicating that a new task has been added.
     *
     * @param t          the task that was added
     * @param totalTasks the total number of tasks after adding
     */
    public List<String> getTaskAdded(Task t, int totalTasks) {
        assert t != null : "Added task should not be null";
        assert totalTasks >= 0 : "Total task count cannot be negative";
        return List.of(wrap(
                "Okie Dokie! I've added this task:",
                t.toString(),
                "Now you have " + totalTasks + " tasks in the list."
        ));
    }

    /**
     * Returns a message indicating that a task has been deleted.
     *
     * @param t          the task that was deleted
     * @param totalTasks the total number of tasks remaining
     */
    public List<String> getTaskDeleted(Task t, int totalTasks) {
        assert t != null : "Deleted task should not be null";
        assert totalTasks >= 0 : "Total task count cannot be negative";
        return List.of(wrap(
                "Siao EH. I've removed this task:",
                t.toString(),
                "Now you have " + totalTasks + " tasks in the list."
        ));
    }

    /**
     * Returns a message indicating whether a task has been marked as done or not done.
     *
     * @param task the task being marked
     * @param isDone     true if the task is marked as done, false otherwise
     */
    public List<String> getTaskMarked(Task task, boolean isDone) {
        assert task != null : "Marked task should not be null";
        String msg = isDone
                ? "Nice! I've marked this task as done:"
                : "WTF... FINE..., I've marked this task as not done yet:";
        return List.of(wrap(
                msg,
                task.toString()
        ));
    }

    /**
     * Returns a list of messages to curse at the user.
     *
     * @param content   the message being sent to Agnes.
     * @return          the list of curse messages to the user.
     */
    public List<String> getKnsResponse(String content) {
        String line1 = "LEE YI HENG you dare to cise at me!";
        String line2 = "CIRSE";
        String line3 = "CRUISE";
        String line4 = "CURSE";
        String line5 = "KNS...";

        return Stream.of(
                line1, line2, line3, line4, line5
        ).map(this::wrap).toList();
    }

    /**
     * Returns a message indicating that a task has been updated.
     *
     * @param t         the task that has been updated
     * @return          the list of display messages to the user.
     */
    public List<String> getTaskUpdated(Task t) {
        assert t != null : "Deleted task should not be null";
        return List.of(wrap(
                "Haode. Here's the updated task:",
                t.toString()
        ));
    }

}
