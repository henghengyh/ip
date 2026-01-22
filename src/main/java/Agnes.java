import java.util.*;

public class Agnes {
    private List<Task> tasks = new ArrayList<>();

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
                t = new ToDo(content);
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
                t = new Deadline(deadlineInfo[0], deadlineInfo[1]);
                addTask(t);
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
                t = new Event(eventInfo[0], fromToInfo[0], fromToInfo[1]);
                addTask(t);
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
    }

    private void deleteTask(int x) {
        Task toBeRemoved = this.tasks.get(x - 1);
        printDottedLine();
        print("Noted. I've removed this task:");
        print("\t" + toBeRemoved);
        tasks.remove(x - 1);
        print(String.format("Now you have %d tasks in the list.", tasks.size()));
        printDottedLine();
    }

    // ALL PRINT STATEMENTS
    private void listItems() {
        printDottedLine();
        for (int i = 1; i <= tasks.size(); i++)
            print(i + ". " + tasks.get(i - 1));
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
}
