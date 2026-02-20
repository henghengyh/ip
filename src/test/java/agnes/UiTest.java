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
 * They've been replaced with methods that return a List of Strings
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
        assertTrue(messages.get(0).contains("Okie Dokie"));
        assertTrue(messages.get(0).contains("added this task"));
        assertTrue(messages.get(0).contains("ToDo Task Sample"));
        assertTrue(messages.get(0).contains("10 tasks"));
    }

    @Test
    public void getTaskDeletedTest() {
        Task t = new ToDo("ToDo Task Sample");
        List<String> messages = ui.getTaskDeleted(t, 5);

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("Siao EH"));
        assertTrue(messages.get(0).contains("removed this task"));
        assertTrue(messages.get(0).contains("5 tasks"));
    }

    @Test
    public void getTasksEmptyTest() {
        TaskList tasks = new TaskList();
        List<String> messages = ui.getTasks(tasks);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("No tasks in your list"));
    }

    @Test
    public void getTasksWithItemsTest() {
        TaskList tasks = new TaskList();
        tasks.addTask(new ToDo("Task 1"));
        tasks.addTask(new ToDo("Task 2"));
        List<String> messages = ui.getTasks(tasks);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("Here's everything you've got so far"));
        assertTrue(messages.get(0).contains("1. "));
        assertTrue(messages.get(0).contains("2. "));
    }

    @Test
    public void getTaskMarkedDoneTest() {
        Task t = new ToDo("Complete assignment");
        List<String> messages = ui.getTaskMarked(t, true);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("Nice"));
        assertTrue(messages.get(0).contains("marked this task as done"));
    }

    @Test
    public void getTaskMarkedNotDoneTest() {
        Task t = new ToDo("Complete assignment");
        List<String> messages = ui.getTaskMarked(t, false);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("WTF"));
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
        assertTrue(messages.get(0).contains("Here's what I found"));
        assertTrue(messages.get(0).contains("Buy"));
        assertTrue(messages.get(0).contains("1. "));
    }

    @Test
    public void getTasksOnDateEmptyTest() {
        LocalDate date = LocalDate.now();
        List<String> messages = ui.getTasksOnDate(List.of(), date);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("AIYOOOOOO"));
        assertTrue(messages.get(0).contains("No tasks found on"));
    }

    @Test
    public void getTasksOnDateWithItemsTest() {
        LocalDate date = LocalDate.of(2026, 2, 20);
        List<Task> taskList = List.of(new ToDo("Task on this date"));
        List<String> messages = ui.getTasksOnDate(taskList, date);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("Here's what I found on"));
        assertTrue(messages.get(0).contains("1. "));
    }

    @Test
    public void getTaskMarkedTest() {
        Task t = new ToDo("Complete assignment");
        List<String> messages = ui.getTaskMarked(t, true);

        assertNotNull(messages);
        assertTrue(messages.get(0).contains("Nice"));
    }

    @Test
    public void getKnsResponseTest() {
        List<String> messages = ui.getKnsResponse("");

        assertNotNull(messages);
        assertEquals(5, messages.size());
        assertTrue(messages.get(0).contains("LEE YI HENG"));
        assertTrue(messages.get(0).contains("cise at me"));
        assertTrue(messages.get(1).contains("CIRSE"));
        assertTrue(messages.get(2).contains("CRUISE"));
        assertTrue(messages.get(3).contains("CURSE"));
        assertTrue(messages.get(4).contains("KNS"));
    }

    @Test
    public void getTaskUpdatedTest() {
        Task t = new ToDo("Updated task");
        List<String> messages = ui.getTaskUpdated(t);

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("Haode"));
        assertTrue(messages.get(0).contains("updated task"));
    }
}


