package agnes.ui;

import java.time.LocalDate;
import java.util.List;

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
     * @param msg the message to be processed.
     * @return the message wrapped with LINE.
     */
    private String wrap(String msg) {
        return Ui.LINE + "\n" + msg + "\n" + Ui.LINE;
    }

    /**
     * Inserts a tab before the message.
     * @param msg the message to be processed.
     * @return the message tabbed.
     */
    private String tab(String msg) {
        return "\t" + msg;
    }

    /**
     * Returns a welcome message when the conversation with the user starts.
     * @return the welcome message.
     */
    public String getWelcomeMessage() {
        return wrap(tab("Hello thereeee! I'm " + Agnes.class.getSimpleName())
                + "\n" + tab("What can I do for you?"));
    }

    /**
     * Prints a goodbye message when the conversation with the user ends.
     * @return the goodbye message.
     */
    public String getByeMessage() {
        return wrap(tab("Goodbye! Have a wonderful day ahead!"));
    }

    /**
     * Returns a list of tasks in String format to be printed.
     * <p>
     * If the list is empty, returns a message indicating no tasks were found.
     * </p>
     *
     * @param tasks the list of tasks on the given date
     */
    public String getTasks(TaskList tasks) {
        if (tasks.size() == 0) {
            return wrap(tab("No tasks in your list!"));
        }

        StringBuilder sb = new StringBuilder(tab("Here are your tasks:\n"));
        for (int i = 1; i <= tasks.size(); i++) {
            sb.append(tab((i) + ". " + tasks.get(i - 1)));
            if (i < tasks.size()) {
                sb.append("\n");
            }
        }
        return wrap(sb.toString());
    }

    /**
     * Returns an error message from an exception.
     *
     * @param e the exception containing the error message
     */
    public String getErrorMessage(Exception e) {
        return wrap(tab(e.getMessage()));
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
    public String getTasksOnDate(List<Task> tasks, LocalDate date) {
        if (tasks.isEmpty()) {
            return wrap(tab("No tasks found on " + date));
        }

        StringBuilder sb = new StringBuilder(tab("Tasks on " + date + ":\n"));
        int i = 1;
        for (Task t : tasks) {
            sb.append(tab(i++ + ". " + t)).append("\n");
        }
        return wrap(sb.toString());
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
    public String getSearchTasks(List<Task> tasks, String keyword) {
        if (tasks.isEmpty()) {
            return wrap(tab("No tasks with keyword: " + keyword));
        }

        StringBuilder sb = new StringBuilder(tab("Matching tasks:\n"));
        int i = 1;
        for (Task t : tasks) {
            sb.append(tab(i++ + ". " + t)).append("\n");
        }
        return wrap(sb.toString());
    }


    /**
     * Returns a message indicating that a new task has been added.
     *
     * @param t          the task that was added
     * @param totalTasks the total number of tasks after adding
     */
    public String getTaskAdded(Task t, int totalTasks) {
        return wrap(tab("New task received. I've added this task:")
                + "\n" + tab(t.toString())
                + "\n" + tab("Now you have " + totalTasks + " tasks in the list."));
    }

    /**
     * Returns a message indicating that a task has been deleted.
     *
     * @param t          the task that was deleted
     * @param totalTasks the total number of tasks remaining
     */
    public String getTaskDeleted(Task t, int totalTasks) {
        return wrap(tab("Noted. I've removed this task:")
                + "\n" + tab(t.toString())
                + "\n" + tab("Now you have " + totalTasks + " tasks in the list."));
    }

    /**
     * Returns a message indicating whether a task has been marked as done or not done.
     *
     * @param task the task being marked
     * @param isDone     true if the task is marked as done, false otherwise
     */
    public String getTaskMarked(Task task, boolean isDone) {
        String msg = isDone
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
        return wrap(tab(msg) + "\n" + tab(task.toString()));
    }

}
