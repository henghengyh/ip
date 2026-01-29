package agnes.ui;

import agnes.Agnes;

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
}
