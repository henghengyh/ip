package agnes;

import agnes.task.TaskList;
import agnes.ui.Ui;
import agnes.parser.Parser;
import agnes.exception.*;
import agnes.storage.Storage;

public class Agnes {
    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;
    private final Parser parser;

    public Agnes() {
        this.tasks = new TaskList();
        this.storage = new Storage("./data/tasks.txt");
        this.ui = new Ui();

        parser = new Parser(tasks, storage, ui);
    }

    public static void main(String[] args) {
        Agnes myBot = new Agnes();
        myBot.run();
    }

    private void run() {
        ui.startConversation();
        parser.userInput();
        ui.endConversation();
    }
}
