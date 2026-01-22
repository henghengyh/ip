import java.util.*;

public class Agnes {
    private List<String> items = new ArrayList<>();

    public static void main(String[] args) {
        Agnes myBot = new Agnes();
        myBot.run();
    }

    private void run() {
        startConversation();
        userInput();
        endConversation();
    }

    private void startConversation() {
        printReply(
                "Hello! I'm " + Agnes.class.getName(),
                "What can I do for you?"
        );
    }

    private void endConversation() {
        printReply("Goodbye! Have a wonderful day ahead!");
    }

    private void userInput() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String request = sc.nextLine();
            if (request.equals("bye")) {
                break;
            } else if (request.equals("list")) {
                listItems();
            } else {
                items.add(request);
                printReply("added: " + request);
            }
        }
        sc.close();
    }

    private void listItems() {
        printDottedLine();
        for (int i = 1; i <= items.size(); i++)
            print(i + ". " + items.get(i - 1));
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
