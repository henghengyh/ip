package agnes;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import agnes.task.Task;
import agnes.task.TaskList;
import agnes.ui.Ui;
import agnes.exception.*;
import agnes.storage.Storage;
import agnes.util.DateTimeUtil;

public class Agnes {
    private final TaskList tasks = new TaskList();
    private final Storage storage = new Storage("./data/tasks.txt");
    private final Ui ui = new Ui();

    public static void main(String[] args) {
        Agnes myBot = new Agnes();
        myBot.run();
    }

    private void run() {
        ui.startConversation();
        userInput();
        ui.endConversation();
    }

    private void handleDelete(String request) throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        String[] parts = request.split(" ");
        if (parts.length < 2) {
            throw new InvalidTaskNumberException("Don't play play... Give me a agnes.task number!");
        }

        int taskNo = tasks.checkTaskNumber(parts[1]);
        deleteTask(taskNo);
        storage.save(tasks);
    }

    // ACTIONS TO CALL TO A TASK
    private void markTask(Task task, boolean b) {
        if (b) {
            task.mark();
            ui.printReply(
                    "Nice! I've marked this agnes.task as done:",
                    "\t" + task
            );
        } else {
            task.unmark();
            ui.printReply(
                    "OK, I've marked this agnes.task as not done yet:",
                    "\t" + task
            );
        }
    }

    private void addTask(Task t) {
        tasks.add(t);
        ui.printDottedLine();
        ui.print("New agnes.task received. I've added this agnes.task.");
        ui.print("\t" + t);
        ui.print(String.format("Now you have %d tasks in the list.", tasks.size()));
        ui.printDottedLine();
        storage.save(tasks);
    }

    private void deleteTask(int x) {
        Task toBeRemoved = this.tasks.get(x - 1);
        ui.printDottedLine();
        ui.print("Noted. I've removed this agnes.task:");
        ui.print("\t" + toBeRemoved);
        tasks.remove(x - 1);
        ui.print(String.format("Now you have %d tasks in the list.", tasks.size()));
        ui.printDottedLine();
        storage.save(tasks);
    }

    // ALL PRINT STATEMENTS
    private void listItems() {
        ui.printDottedLine();
        for (int i = 1; i <= tasks.size(); i++)
            ui.print(i + ". " + tasks.get(i - 1));
        ui.printDottedLine();
    }

    private void showTasksOnDate(String dateStr) {
        try {
            LocalDate date = DateTimeUtil.parseDateTime(dateStr).toLocalDate();
            List<Task> tasksOnDate = tasks.getTasksOnDate(date);
            ui.printTasksOnDate(tasksOnDate, date);
        } catch (DateTimeParseException e) {
            ui.printError(new InvalidDescriptionException(
                    "Date format should be yyyy-MM-dd"
            ));
        }
    }
}
