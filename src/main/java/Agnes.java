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
                "Hello! I'm " + Agnes.class.getName(),
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
            if (request.equals("bye")) {
                break;
            } else if (request.equals("list")) {
                listItems();
            } else if (request.split(" ")[0].equals("mark")) {
                int taskNo = Integer.parseInt(request.split(" ")[1]);
                markTask(tasks.get(taskNo - 1), true);
            } else if (request.split(" ")[0].equals("unmark")) {
                int taskNo = Integer.parseInt(request.split(" ")[1]);
                markTask(tasks.get(taskNo - 1), false);
            } else {
                tasks.add(new Task(request));
                printReply("added: " + request);
            }
        }
        sc.close();
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
}
