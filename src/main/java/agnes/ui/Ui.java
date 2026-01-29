package agnes.ui;

import agnes.Agnes;
import agnes.parser.Command;

import agnes.exception.InvalidCommandException;
import agnes.exception.InvalidDescriptionException;
import agnes.exception.InvalidTaskNumberException;
import agnes.exception.TaskIndexOutOfBoundsException;
import agnes.task.Task;
import agnes.task.TaskList;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.List;

public class Ui {
    public void startConversation() {
        printReply(
                "Hello thereeee! I'm " + Agnes.class.getSimpleName(),
                "What can I do for you?"
        );
    }

    public void endConversation() {
        printReply("Goodbye! Have a wonderful day ahead!");
    }



    public void printDottedLine() {
        System.out.println("\t------------------------------------");
    }

    public void print(Object... objs) {
        for (Object obj : objs) System.out.println("\t" + obj);
    }

    public void printReply(Object... objs) {
        printDottedLine();
        print(objs);
        printDottedLine();
    }

    public void printError(Exception e) {
        printDottedLine();
        print(e.getMessage());
        printDottedLine();
    }

    public void printTasksOnDate(List<Task> tasks, LocalDate date) {
        printDottedLine();

        if (tasks.isEmpty()) {
            print("No tasks found on " + date);
        } else {
            int i = 1;
            for (Task t : tasks) {
                print(i++ + ". " + t);
            }
        }
        printDottedLine();
    }

    public void printTaskAdded(Task t, int totalTasks) {
        printDottedLine();
        print("New task received. I've added this task:");
        print("\t" + t);
        print(String.format("Now you have %d tasks in the list.", totalTasks));
        printDottedLine();
    }

    public void printTaskDeleted(Task t, int totalTasks) {
        printDottedLine();
        print("Noted. I've removed this task:");
        print("\t" + t);
        print(String.format("Now you have %d tasks in the list.", totalTasks));
        printDottedLine();
    }

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

    public void printTasks(TaskList tasks) {
        printDottedLine();
        for (int i = 1; i <= tasks.size(); i++)
            print(i + ". " + tasks.get(i - 1));
        printDottedLine();
    }
}
