package agnes.ui;

import agnes.Agnes;
import agnes.task.Task;
import agnes.task.TaskList;

import java.time.LocalDate;
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
        for (Object obj : objs) {
            System.out.println("\t" + obj);
        }
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
            printTasksInSeq(tasks);
        }
        printDottedLine();
    }

    public void printSearchTasks(List<Task> tasks, String keyword) {
        printDottedLine();
        if (tasks.isEmpty()) {
            print("No tasks with keyword: " + keyword);
        } else {
            printTasksInSeq(tasks);
        }
        printDottedLine();
    }

    private void printTasksInSeq(List<Task> tasks) {
        int i = 1;
        for (Task t : tasks) {
            print(i++ + ". " + t);
        }
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
        for (int i = 1; i <= tasks.size(); i++) {
            print(i + ". " + tasks.get(i - 1));
        }
        printDottedLine();
    }
}
