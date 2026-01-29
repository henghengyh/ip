package agnes.task;

import agnes.exception.InvalidTaskNumberException;
import agnes.exception.TaskIndexOutOfBoundsException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task t) {
        tasks.add(t);
    }

    public Task removeTask(int index) {
        return tasks.remove(index);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public List<Task> getAll() {
        return tasks;
    }

    public List<Task> getTasksOnDate(LocalDate date) {
        return this.tasks.stream()
                .filter(t -> t.fallsOnDate(date))
                .toList();
    }

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

    public List<Task> find(String keyword) {
        List<Task> results = new ArrayList<>();
        for (Task t : this.tasks) {
            if (t.getMessage().contains(keyword)) {
                results.add(t);
            }
        }
        return results;
    }
}
