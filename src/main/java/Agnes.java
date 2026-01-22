public class Agnes {
    public static void main(String[] args) {
        String hiMessage = "Hello! I'm " + Agnes.class.getName() + "\nWhat can I do for you?";
        String line = "---------------------------------";
        String byeMessage = "Bye. Hope to see you again soon!";

        System.out.println(line);
        System.out.println(hiMessage);
        System.out.println(line);
        System.out.println(byeMessage);
        System.out.println(line);
    }
}
