import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class Agnes {
    private List<Task> tasks = new ArrayList<>();
    private final static String FILE_PATH = "./data/tasks.txt";

    public static void main(String[] args) {
        Agnes myBot = new Agnes();
        myBot.run();
    }

    private void run() {
        startConversation();
        userInput();
        endConversation();
    }

    // DEFAULT CONVERSATIONS
    private void startConversation() {
        printReply(
                "Hello thereeee! I'm " + Agnes.class.getName(),
                "What can I do for you?"
        );
    }

    private void endConversation() {
        printReply("Goodbye! Have a wonderful day ahead!");
    }

    // USER INPUT
    private void userInput() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String request = sc.nextLine();
            String keyword = request.split(" ")[0];
            Command command = Command.from(keyword);
            try {
                switch (command) {
                case HI:
                    printReply("Helloss! What can I do for you?");
                    break;
                case BYE:
                    return;
                case LIST:
                    listItems();
                    break;
                case ON:
                    listItemsOnDate(request);
                    break;
                case MARK:
                    handleMark(request, true);
                    break;
                case UNMARK:
                    handleMark(request, false);
                    break;
                case DELETE:
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

    // ERROR HANDLING
    private void handleCommands(String request) throws InvalidDescriptionException, InvalidCommandException {
        String action = request.split(" ")[0];
        Command cmd = Command.from(action);
        Task t;
        String content;
        switch (cmd) {
        case TODO:
            if (request.length() <= 5) {
                throw new InvalidDescriptionException(
                        "Hellos, tell me what description you want!"
                );
            }

            content = request.substring(5);
            t = new ToDo(content.trim());
            addTask(t);
            break;
        case DEADLINE:
            if (!request.contains(" /by ")) {
                throw new InvalidDescriptionException(
                        "Specify your deadline using '/by'..."
                );
            }

            content = request.substring(9);
            String[] deadlineInfo = content.split(" /by ");
            String datePart = deadlineInfo[1].trim();

            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            try {
                LocalDateTime by = DateTimeUtil.parseDateTime(datePart);
                t = new Deadline(deadlineInfo[0].trim(), by);
                addTask(t);
            } catch (DateTimeParseException e) {
                throw new InvalidDescriptionException(
                        "Date format should be yyyy-MM-dd or yyyy-MM-dd HHmm"
                );
            }
            break;
        case EVENT:
            if (!request.contains(" /from ") || !request.contains(" /to ")) {
                throw new InvalidDescriptionException(
                        "Specify your event duration using '/from' and '/to'..."
                );
            }

            content = request.substring(6);
            String[] eventInfo = content.split(" /from ");
            String[] fromToInfo = eventInfo[1].split(" /to ");
            String fromPart = fromToInfo[0].trim();
            String toPart = fromToInfo[1].trim();

            try {
                LocalDateTime from = DateTimeUtil.parseDateTime(fromPart);
                LocalDateTime to = DateTimeUtil.parseDateTime(toPart);

                t = new Event(eventInfo[0].trim(), from, to);
                addTask(t);
            } catch (DateTimeParseException e) {
                throw new InvalidDescriptionException(
                        "Date format should be yyyy-MM-dd or yyyy-MM-dd HHmm"
                );
            }
            break;
        default:
            throw new InvalidCommandException("I don't understand what you're saying... TYPE PROPERLY LEH");
        }
    }

    private void handleMark(String request, boolean mark) throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        String[] parts = request.split(" ");
        if (parts.length < 2) {
            throw new InvalidTaskNumberException("Don't play play... Give me a task number!");
        }
        int taskNo = checkTaskNumber(parts[1]);
        markTask(tasks.get(taskNo - 1), mark);
        printTasksToFile(Agnes.FILE_PATH);
    }

    private int checkTaskNumber(String number) throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        int taskNo;
        try {
            taskNo = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException("Can count 123 or not... Give me a proper number!");
        }

        if (taskNo < 1 || taskNo > tasks.size()) {
            throw new TaskIndexOutOfBoundsException("Your task number is out of my range! Try the command 'list' to know how many task you have :))");
        }
        return taskNo;
    }

    private void handleDelete(String request) throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        String[] parts = request.split(" ");
        if (parts.length < 2) {
            throw new InvalidTaskNumberException("Don't play play... Give me a task number!");
        }

        int taskNo = checkTaskNumber(parts[1]);
        deleteTask(taskNo);
        printTasksToFile(Agnes.FILE_PATH);
    }

    // ACTIONS TO CALL TO A TASK
    private void markTask(Task task, boolean b) {
        if (b) {
            task.mark();
            printReply(
                    "Nice! I've marked this task as done:",
                    "\t" + task
            );
        } else {
            task.unmark();
            printReply(
                    "OK, I've marked this task as not done yet:",
                    "\t" + task
            );
        }
    }

    private void addTask(Task t) {
        tasks.add(t);
        printDottedLine();
        print("New task received. I've added this task.");
        print("\t" + t);
        print(String.format("Now you have %d tasks in the list.", tasks.size()));
        printDottedLine();
        printTasksToFile(Agnes.FILE_PATH);
    }

    private void deleteTask(int x) {
        Task toBeRemoved = this.tasks.get(x - 1);
        printDottedLine();
        print("Noted. I've removed this task:");
        print("\t" + toBeRemoved);
        tasks.remove(x - 1);
        print(String.format("Now you have %d tasks in the list.", tasks.size()));
        printDottedLine();
        printTasksToFile(Agnes.FILE_PATH);
    }

    // ALL PRINT STATEMENTS
    private void listItems() {
        printDottedLine();
        for (int i = 1; i <= tasks.size(); i++)
            print(i + ". " + tasks.get(i - 1));
        printDottedLine();
    }

    private void listItemsOnDate(String request) throws InvalidDescriptionException {
        String content = request.substring(3);
        String datePart = content.trim();

        LocalDate date;

        try {
            date = DateTimeUtil.parseDateTime(datePart).toLocalDate();
        } catch (DateTimeParseException e) {
            throw new InvalidDescriptionException(
                    "Date format should be yyyy-MM-dd"
            );
        }
        printDottedLine();
        int i = 1;
        for (Task t : tasks) {
            if (t.fallsOnDate(date)) {
                print(i + ". " + tasks.get(i - 1));
                i++;
            }
        }
        printDottedLine();
    }

    private void printDottedLine() {
        System.out.println("\t------------------------------------");
    }

    private void print(Object... objs) {
        for (Object obj : objs) System.out.println("\t" + obj);
    }

    private void printReply(Object... objs) {
        printDottedLine();
        print(objs);
        printDottedLine();
    }

    private void printError(Exception e) {
        printDottedLine();
        print(e.getMessage());
        printDottedLine();
    }

    private static void writeToFile(String filePath, String textToAdd) throws IOException {
        File file = new File(filePath);

        // Defensive Programming, verify
        // 1. The parent folder exist
        // 2. The file to be overwritten in exists
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
        if (!file.exists()) file.createNewFile();

        // Using overwriting mode; Use (filePath, true) if want to append
        FileWriter fw = new FileWriter(filePath);
        fw.write(textToAdd);
        fw.close();
    }

    private void printTasksToFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= tasks.size(); i++) {
            sb.append(tasks.get(i - 1).toFileFormat())
                    .append("\n");
        }
        try {
            Agnes.writeToFile(FILE_PATH, sb.toString());
        } catch (IOException e) {
            // Since writeToFile conducts defensive programming checks, we
            // Do not expect any exception thrown by it
            print(e);
        }
    }

}
