package agnes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import agnes.task.Task;
import agnes.task.TaskList;
import agnes.task.ToDo;
import agnes.ui.Ui;

/**
 * AI updated the test suites based on the latest
 * implementation of the Ui class.
 * The test cases are designed to cover the various methods
 * in the Ui class, ensuring that they return the expected
 * messages based on different scenarios.
 *
 * Looking at your current Ui.java, the old test methods
 * like printReply() and printTaskAdded() no longer exist.
 * They've been replaced with methods that return List<String>
 * instead of printing directly.
 */
public class UiTest {

    private Ui ui;

    @BeforeEach
    void setUp() {
        ui = new Ui();
    }

    @Test
    public void getWelcomeMessageTest() {
        List<String> messages = ui.getWelcomeMessage();
        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("Hello thereeee"));
        assertTrue(messages.get(0).contains("Agnes"));
    }

    @Test
    public void getByeMessageTest() {
        List<String> messages = ui.getByeMessage();
        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("Goodbye"));
    }

    @Test
    public void getTaskAddedTest() {
        Task t = new ToDo("ToDo Task Sample");
        List<String> messages = ui.getTaskAdded(t, 10);

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("New task received"));
        assertTrue(messages.get(0).contains("ToDo Task Sample"));
        assertTrue(messages.get(0).contains("10 tasks"));
    }

    @Test
    public void getTaskDeletedTest() {
        Task t = new ToDo("ToDo Task Sample");
        List<String> messages = ui.getTaskDeleted(t, 5);

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("removed this task"));
        assertTrue(messages.get(0).contains("5 tasks"));
    }

    @Test
    public void getTasksEmptyTest() {
        TaskList tasks = new TaskList();
        List<String> messages = ui.getTasks(tasks);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("No tasks"));
    }

    @Test
    public void getTasksWithItemsTest() {
        TaskList tasks = new TaskList();
        tasks.addTask(new ToDo("Task 1"));
        tasks.addTask(new ToDo("Task 2"));
        List<String> messages = ui.getTasks(tasks);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("Here are your tasks"));
        assertTrue(messages.get(0).contains("1. "));
        assertTrue(messages.get(0).contains("2. "));
    }

    @Test
    public void getTaskMarkedDoneTest() {
        Task t = new ToDo("Complete assignment");
        List<String> messages = ui.getTaskMarked(t, true);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("marked this task as done"));
    }

    @Test
    public void getTaskMarkedNotDoneTest() {
        Task t = new ToDo("Complete assignment");
        List<String> messages = ui.getTaskMarked(t, false);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("marked this task as not done"));
    }

    @Test
    public void getSearchTasksEmptyTest() {
        List<String> messages = ui.getSearchTasks(List.of(), "keyword");

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("No tasks with keyword"));
    }

    @Test
    public void getSearchTasksWithResultsTest() {
        List<Task> tasks = List.of(new ToDo("Buy groceries"));
        List<String> messages = ui.getSearchTasks(tasks, "Buy");

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("Matching tasks"));
        assertTrue(messages.get(0).contains("1. "));
    }

    @Test
    public void getTasksOnDateEmptyTest() {
        LocalDate date = LocalDate.now();
        List<String> messages = ui.getTasksOnDate(List.of(), date);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("No tasks found on"));
    }

    @Test
    public void getKnsResponseTest() {
        List<String> messages = ui.getKnsResponse("kns");

        assertNotNull(messages);
        assertTrue(messages.size() >= 5);
        assertTrue(messages.get(0).contains("LEE YI HENG"));
        assertTrue(messages.get(4).contains("KNS"));
    }

    @Test
    public void getTaskUpdatedTest() {
        Task t = new ToDo("Updated task");
        List<String> messages = ui.getTaskUpdated(t);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("Existing task updated"));
    }
}