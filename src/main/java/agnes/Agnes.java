package agnes;

import agnes.parser.Parser;
import agnes.storage.Storage;
import agnes.task.TaskList;
import agnes.ui.Ui;

/**
 * Entry point of the Agnes task management application. (Originally Duke)
 * Has dependencies on the UI, Parser, Storage and TaskList.
 */
public class Agnes {
    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;
    private final Parser parser;

    /**
     * Creates a new Agnes application with initialized components.
     */
    public Agnes() {
        this.tasks = new TaskList();
        this.storage = new Storage("./data/tasks.txt");
        this.ui = new Ui();

        parser = new Parser(tasks, storage, ui);
    }

    /**
     * Launches the Agnes application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Agnes myBot = new Agnes();
        myBot.run();
    }

    /**
     * Starts the conversation loop between the user and the application.
     */
    private void run() {
        ui.startConversation();
        parser.userInput();
        ui.endConversation();
    }
}
