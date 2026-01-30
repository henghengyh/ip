package agnes.ui;

import agnes.Agnes;
import agnes.task.Task;
import agnes.task.TaskList;

import java.time.LocalDate;
import java.util.List;

/**
 * Handles all user interactions and output formatting for the Agnes application.
 * <p>
 * Provides methods to print messages, errors, task lists, and task updates in a
 * consistent and user-friendly manner. Also formats outputs with dotted lines for clarity.
 * </p>
 */
public class Ui {
    /**
     * Prints a welcome message when the conversation with the user starts.
     */
    public void startConversation() {
        printReply(
                "Hello thereeee! I'm " + Agnes.class.getSimpleName(),
                "What can I do for you?"
        );
    }

    /**
     * Prints a goodbye message when the conversation with the user ends.
     */
    public void endConversation() {
        printReply("Goodbye! Have a wonderful day ahead!");
    }


    /**
     * Prints a horizontal dotted line for visual separation.
     */
    public void printDottedLine() {
        System.out.println("\t------------------------------------");
    }

    /**
     * Prints each object provided as a separate line with a tab prefix.
     *
     * @param objs the objects to print
     */
    public void print(Object... objs) {
        for (Object obj : objs) {
            System.out.println("\t" + obj);
        }
    }

    /**
     * Prints a reply to the user surrounded by dotted lines.
     *
     * @param objs the objects to print as a reply
     */
    public void printReply(Object... objs) {
        printDottedLine();
        print(objs);
        printDottedLine();
    }

    /**
     * Prints an error message from an exception, surrounded by dotted lines.
     *
     * @param e the exception containing the error message
     */
    public void printError(Exception e) {
        printDottedLine();
        print(e.getMessage());
        printDottedLine();
    }


    /**
     * Prints a list of tasks scheduled for a specific date.
     * <p>
     * If the list is empty, prints a message indicating no tasks were found.
     * </p>
     *
     * @param tasks the list of tasks on the given date
     * @param date  the date for which tasks are printed
     */
    public void printTasksOnDate(List<Task> tasks, LocalDate date) {
        printDottedLine();

        if (tasks.isEmpty()) {
            print("No tasks found on " + date);
        } else {
            printTasksInSeq(tasks);
        }
        printDottedLine();
    }

    /**
     * Prints a list of tasks containing a certain given keyword.
     * <p>
     * If the list is empty, prints a message indicating no tasks were found.
     * </p>
     *
     * @param tasks     the list of tasks containing the given keyword
     * @param keyword   the keyword to search for
     */
    public void printSearchTasks(List<Task> tasks, String keyword) {
        printDottedLine();
        if (tasks.isEmpty()) {
            print("No tasks with keyword: " + keyword);
        } else {
            printTasksInSeq(tasks);
        }
        printDottedLine();
    }

    /**
     * Prints a list of tasks with numbering format
     * <p>
     * If the list is empty, prints nothing
     * </p>
     *
     * @param tasks the list of tasks to be printed
     */
    private void printTasksInSeq(List<Task> tasks) {
        int i = 1;
        for (Task t : tasks) {
            print(i++ + ". " + t);
        }
    }

    /**
     * Prints a message indicating that a new task has been added.
     *
     * @param t          the task that was added
     * @param totalTasks the total number of tasks after adding
     */
    public void printTaskAdded(Task t, int totalTasks) {
        printDottedLine();
        print("New task received. I've added this task:");
        print("\t" + t);
        print(String.format("Now you have %d tasks in the list.", totalTasks));
        printDottedLine();
    }

    /**
     * Prints a message indicating that a task has been deleted.
     *
     * @param t          the task that was deleted
     * @param totalTasks the total number of tasks remaining
     */
    public void printTaskDeleted(Task t, int totalTasks) {
        printDottedLine();
        print("Noted. I've removed this task:");
        print("\t" + t);
        print(String.format("Now you have %d tasks in the list.", totalTasks));
        printDottedLine();
    }

    /**
     * Prints a message indicating whether a task has been marked as done or not done.
     *
     * @param task the task being marked
     * @param b     true if the task is marked as done, false otherwise
     */
    public void printTaskMarked(Task task, boolean b) {
        if (b) {
            printReply(
                    "Nice! I've marked this task as done:",
                    "\t" + task
            );
        } else {
            printReply(
                    "OK, I've marked this task as not done yet:",
                    "\t" + task
            );
        }
    }

    /**
     * Prints all tasks in the provided TaskList with ordered-numbering.
     *
     * @param tasks the TaskList to print
     */
    public void printTasks(TaskList tasks) {
        printDottedLine();
        for (int i = 1; i <= tasks.size(); i++) {
            print(i + ". " + tasks.get(i - 1));
        }
        printDottedLine();
    }
}
