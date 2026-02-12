package agnes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import agnes.task.ToDo;

// This is a trivial test suite
public class ToDoTest {
    @Test
    public void messageTest() {
        String message = "This is a sample message";
        ToDo t = new ToDo(message);

        String expected = "This is a sample message";
        String actual = t.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    public void markTest() {
        ToDo t = new ToDo("");
        t.setMarked();

        String expected = "X";
        String output = t.getStatusIcon();
        assertEquals(expected, output);
    }
}
