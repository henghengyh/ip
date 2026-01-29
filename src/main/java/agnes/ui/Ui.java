package agnes.ui;

import agnes.Agnes;
import agnes.parser.Command;

import agnes.exception.InvalidCommandException;
import agnes.exception.InvalidDescriptionException;
import agnes.exception.InvalidTaskNumberException;
import agnes.exception.TaskIndexOutOfBoundsException;
import agnes.task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.List;

public class Ui {
    public void startConversation() {
        printReply(
                "Hello thereeee! I'm " + Agnes.class.getName(),
                "What can I do for you?"
        );
    }

    public void endConversation() {
        printReply("Goodbye! Have a wonderful day ahead!");
    }

    public void userInput() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String request = sc.nextLine();
            String keyword = request.split(" ")[0];
            Command command = Command.from(keyword);
            try {
                switch (command) {
                    case Command.HI:
                        printReply("Helloss! What can I do for you?");
                        break;
                    case Command.BYE:
                        return;
                    case Command.LIST:
                        listItems();
                        break;
                    case Command.ON:
                        listItemsOnDate(request);
                        break;
                    case Command.MARK:
                        handleMark(request, true);
                        break;
                    case Command.UNMARK:
                        handleMark(request, false);
                        break;
                    case Command.DELETE:
                        handleDelete(request);
                        break;
                    default:
                        handleCommands(request);
                }
            } catch (InvalidDescriptionException
                     | InvalidTaskNumberException
                     | TaskIndexOutOfBoundsException
                     | InvalidCommandException e) {
                printError(e);
            }
        }
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
}
