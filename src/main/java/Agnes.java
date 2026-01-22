import java.util.*;

public class Agnes {
    public static void main(String[] args) {
        Agnes myBot = new Agnes();
        myBot.run();
    }

    private void run() {
        startConversation();
        endConversation();
    }

    private void startConversation() {
        printDottedLine();
        System.out.println("\tHello! I'm " + Agnes.class.getName() + "\n\tWhat can I do for you?");
        printDottedLine();
    }

    private void endConversation() {
        System.out.println("\tGoodbye! Have a wonderful day ahead!");
        printDottedLine();
    }

    private void printDottedLine() {
        System.out.println("\t------------------------------------");
    }

    private void print(Object obj) {
        System.out.println("\t" + obj);
    }
}
