package agnes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import agnes.task.Task;
import agnes.task.ToDo;
import agnes.ui.Ui;

// This is a NON-trivial test suite
public class UiPrintTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private Ui ui;

    @BeforeEach
    void setUp() {
        // Redirect System.out
        System.setOut(new PrintStream(outContent));
        ui = new Ui();
    }

    @AfterEach
    void tearDown() {
        // Reset System.out
        System.setOut(originalOut);
    }

    @Test
    public void printReplyTest() {
        String message = "Hello world!";

        ui.printReply(message);

        String expected =
                "\t------------------------------------\n"
                        + "\tHello world!\n"
                        + "\t------------------------------------\n";
        String output = outContent.toString();
        assertEquals(expected, output);
    }

    @Test
    public void printTaskAddedTest() {
        Task t = new ToDo("ToDo Task Sample");

        ui.printTaskAdded(t, 10);

        String expected =
                "\t------------------------------------\n"
                        + "\tNew task received. I've added this task:\n"
                        + "\t" + "\t" + t + "\n"
                        + "\tNow you have 10 tasks in the list.\n"
                        + "\t------------------------------------\n";
        String output = outContent.toString();
        assertEquals(expected, output);
    }
}
