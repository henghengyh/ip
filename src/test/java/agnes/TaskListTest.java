package agnes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import agnes.exception.InvalidTaskNumberException;
import agnes.exception.TaskIndexOutOfBoundsException;
import agnes.task.Deadline;
import agnes.task.Event;
import agnes.task.TaskList;
import agnes.task.ToDo;

// This is a NON-trivial test suite
public class TaskListTest {

    @Test
    public void sizeTest() {
        String message = "This is a sample message";

        LocalDate date = LocalDate.of(2026, 1, 29);
        LocalTime time = LocalTime.of(14, 45);
        LocalDateTime t1 = LocalDateTime.of(date, time);
        LocalDateTime t2 = LocalDateTime.of(date, time);

        ToDo t = new ToDo(message);
        Deadline d = new Deadline(message, t1);
        Event e = new Event(message, t1, t2);

        TaskList l = new TaskList();
        l.addTask(t);
        l.addTask(d);
        l.addTask(e);

        int expected = 3;
        int actual = l.size();
        assertEquals(expected, actual);
    }

    @Test
    public void checkTaskNumber_valid_test() {
        TaskList l = new TaskList();
        l.addTask(new ToDo("Sample task"));
        l.addTask(new ToDo("Another task"));

        int expected = 2;
        try {
            int output = l.checkTaskNumber("2");
            assertEquals(expected, output);
        } catch (InvalidTaskNumberException | TaskIndexOutOfBoundsException e) {
            fail("Exception should not have been thrown for a valid task number");
        }
    }

    @Test
    public void checkTaskNumber_invalid_test() {
        TaskList l = new TaskList();
        l.addTask(new ToDo("Sample task"));

        try {
            l.checkTaskNumber("2");
            fail("Exception should be thrown for an invalid task number");
        } catch (InvalidTaskNumberException | TaskIndexOutOfBoundsException e) {
            assertTrue(
                    e.getMessage().equals("Can count 123 or not... Give me a proper number!")
                            || e.getMessage().equals(
                                    "Your task number is out of my range! "
                                            + "Try the command 'list' to know how many tasks you have :))"
                    )
            );
        }
    }
}
