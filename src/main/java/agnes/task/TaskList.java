package agnes.task;

import agnes.exception.InvalidTaskNumberException;
import agnes.exception.TaskIndexOutOfBoundsException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of tasks that can be managed, retrieved, or filtered.
 * <p>
 * Provides methods to add, remove, and access tasks, as well as to filter tasks by date.
 * Also validates task numbers to ensure they are within the proper range.
 * Design of this class draws inspiration from java.util.List.
 * </p>
 */
public class TaskList {
    private List<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the TaskList.
     *
     * @param t the Task to add
     */
    public void addTask(Task t) {
        tasks.add(t);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index the index of the task to remove (0-based)
     * @return the Task that was removed
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Task removeTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Retrieves the task at the specified index without removing it.
     *
     * @param index the index of the task to retrieve (0-based)
     * @return the Task at the given index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in this {@code TaskList}.
     *
     * @return the size of the {@code TaskList}
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns all tasks in the {@code TaskList} as a {@code List}.
     *
     * @return a {@code List} containing all tasks
     */
    public List<Task> getAll() {
        return tasks;
    }

    /**
     * Returns a list of tasks that occur on the specified date.
     *
     * @param date the date to filter tasks by
     * @return a List of tasks that fall on the given date
     */
    public List<Task> getTasksOnDate(LocalDate date) {
        return this.tasks.stream()
                .filter(t -> t.fallsOnDate(date))
                .toList();
    }

    /**
     * Validates a task number provided as a string.
     * <p>
     * Converts the string to an integer and checks if it is within the range of existing tasks.
     * </p>
     *
     * @param number the task number as a String
     * @return the validated task number as an integer
     * @throws InvalidTaskNumberException if the string is not a valid integer
     * @throws TaskIndexOutOfBoundsException if the task number is less than 1 or greater than the number of tasks
     */
    public int checkTaskNumber(String number) throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        int taskNo;
        try {
            taskNo = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException("Can count 123 or not... Give me a proper number!");
        }

        if (taskNo < 1 || taskNo > tasks.size()) {
            throw new TaskIndexOutOfBoundsException("Your task number is out of my range! Try the command 'list' to know how many tasks you have :))");
        }
        return taskNo;
    }
}
