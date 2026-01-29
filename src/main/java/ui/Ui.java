package ui;

public class Ui {
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
