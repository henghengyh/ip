package agnes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import agnes.parser.Parser;
import agnes.storage.Storage;
import agnes.task.TaskList;
import agnes.ui.Ui;

class ParserTest {
    private Parser parser;
    private TaskList tasks;
    private Storage storage;
    private Ui ui;

    @BeforeEach
    void setUp() {
        tasks = new TaskList();
        storage = new Storage("test_data.txt");
        ui = new Ui();
        parser = new Parser(tasks, storage, ui);
    }

    @Test
    void testParseInvalidCommand() {
        var result = parser.parse("invalid command");
        assertNotNull(result);
        assertTrue(result.get(0).contains("don't understand"));
    }

    @Test
    void testParseTodoSuccess() {
        var result = parser.parse("todo buy groceries");
        assertNotNull(result);
        assertEquals(1, tasks.size());
    }
}
