package agnes.parser;

import agnes.exception.InvalidCommandException;
import agnes.exception.InvalidDescriptionException;
import agnes.exception.InvalidTaskNumberException;
import agnes.exception.TaskIndexOutOfBoundsException;
import agnes.task.*;
import agnes.util.DateTimeUtil;
import agnes.storage.Storage;
import agnes.ui.Ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.List;

/**
 * Parses user input and executes the corresponding commands.
 * <p>
 * The {@code Parser} acts as the command post between the user interface,
 * task list, and storage system. It interprets raw user input, determines the
 * intended command, performs the required task operations, and coordinates
 * saving and displaying results.
 */
public class Parser {
    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;

    /**
     * Creates a {@code Parser} with the required dependencies.
     *
     * @param tasks     The {@code TaskList} storing all the added tasks.
     * @param storage   The Storage logic to edit files in disk.
     * @param ui        The user interface logic used for displaying messages.
     */
    public Parser(TaskList tasks, Storage storage, Ui ui) {
        this.tasks = tasks;
        this.storage = storage;
        this.ui = ui;
    }

    /**
     * Continuously reads user input from {@code System.in} and processes
     * commands until the user issues the {@code BYE} command.
     * <p>
     * Valid commands trigger task operations, while invalid inputs result
     * in error messages displayed through the {@code Ui}.
     */
    public void userInput() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String request = sc.nextLine();
            String keyword = request.split(" ")[0];
            Command command = Command.from(keyword);
            try {
                switch (command) {
                case HI:
                    ui.printReply("Helloss! What can I do for you?");
                    break;
                case BYE:
                    return;
                case LIST:
                    ui.printTasks(tasks);
                    break;
                case ON:
                    handleOnDate(request);
                    break;
                case MARK:
                    handleMark(request, true);
                    break;
                case UNMARK:
                    handleMark(request, false);
                    break;
                case DELETE:
                    handleDelete(request);
                    break;
                default:
                    handleCommands(request);
                }
            } catch (InvalidDescriptionException
                     | InvalidTaskNumberException
                     | TaskIndexOutOfBoundsException
                     | InvalidCommandException e) {
                ui.printError(e);
            }
        }
    }

    /**
     * Handles any {@code Command} given to create a {@code Task}
     *
     * @param request   The full user input string.
     * @throws InvalidDescriptionException  If the task description or format is invalid.
     * @throws InvalidCommandException      If the command is not recognised.
     */
    public void handleCommands(String request) throws InvalidDescriptionException, InvalidCommandException {
        String action = request.split(" ")[0];
        Command cmd = Command.from(action);
        Task t;
        String content;
        switch (cmd) {
        case TODO:
            if (request.length() <= 5) {
                throw new InvalidDescriptionException(
                        "Hellos, tell me what description you want!"
                );
            }

            content = request.substring(5);
            t = new ToDo(content.trim());
            addTask(t);
            break;
        case DEADLINE:
            if (!request.contains(" /by ")) {
                throw new InvalidDescriptionException(
                        "Specify your deadline using '/by'..."
                );
            }

            content = request.substring(8);
            String[] deadlineInfo = content.split(" /by ");
            String datePart = deadlineInfo[1].trim();

            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            try {
                LocalDateTime by = DateTimeUtil.parseDateTime(datePart);
                t = new Deadline(deadlineInfo[0].trim(), by);
                addTask(t);
            } catch (DateTimeParseException e) {
                throw new InvalidDescriptionException(
                        "Date format should be yyyy-MM-dd or yyyy-MM-dd HHmm"
                );
            }
            break;
        case EVENT:
            if (!request.contains(" /from ") || !request.contains(" /to ")) {
                throw new InvalidDescriptionException(
                        "Specify your event duration using '/from' and '/to'..."
                );
            }

            content = request.substring(5);
            String[] eventInfo = content.split(" /from ");
            String[] fromToInfo = eventInfo[1].split(" /to ");
            String fromPart = fromToInfo[0].trim();
            String toPart = fromToInfo[1].trim();

            try {
                LocalDateTime from = DateTimeUtil.parseDateTime(fromPart);
                LocalDateTime to = DateTimeUtil.parseDateTime(toPart);

                t = new Event(eventInfo[0].trim(), from, to);
                addTask(t);
            } catch (DateTimeParseException e) {
                throw new InvalidDescriptionException(
                        "Date format should be yyyy-MM-dd or yyyy-MM-dd HHmm"
                );
            }
            break;
        default:
            throw new InvalidCommandException("I don't understand what you're saying... TYPE PROPERLY LEH");
        }
    }

    /**
     * Executes the adding of a {@code Task} to the {@code TaskList}.
     * @param t The {@code Task} to be added.
     */
    private void addTask(Task t) {
        tasks.addTask(t);
        storage.save(tasks);
        ui.printTaskAdded(t, tasks.size());
    }

    /**
     * Handles any request to mark a {@code Task}.
     *
     * @param request   The full user input string.
     * @throws InvalidTaskNumberException       If the task number is invalid.
     * @throws TaskIndexOutOfBoundsException    If the task index is out of bounds.
     */
    private void handleMark(String request, boolean mark) throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        String[] parts = request.split(" ");
        if (parts.length < 2) {
            throw new InvalidTaskNumberException("Don't play play... Give me a task number!");
        }
        int taskNo = tasks.checkTaskNumber(parts[1]);

        markTask(tasks.get(taskNo - 1), mark);
        storage.save(tasks);
    }

    /**
     * Executes the marking of a {@code Task} and printing of statements.
     *
     * @param task  The {@code Task} to be marked.
     * @param b     {@code true} to mark the task as done, {@code false} to mark as not done.
     */
    private void markTask(Task task, boolean b) {
        if (b) {
            task.mark();
            ui.printTaskMarked(task, b);

        } else {
            task.unmark();
            ui.printTaskMarked(task, b);
        }
    }

    /**
     * Handles any request to delete a {@code Task} from a {@code TaskList}
     *
     * @param request   The full user input string.
     * @throws InvalidTaskNumberException       If the task number is invalid.
     * @throws TaskIndexOutOfBoundsException    If the task index is out of bounds.
     */
    public void handleDelete(String request) throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        String[] parts = request.split(" ");
        if (parts.length < 2) {
            throw new InvalidTaskNumberException("Don't play play... Give me a task number!");
        }

        int taskNo = tasks.checkTaskNumber(parts[1]);
        deleteTask(taskNo);
        storage.save(tasks);
    }

    /**
     * Executes the deleting of a {@code Task} and printing of statements.
     *
     * @param taskNo  The task number to delete (1-based index from the user).
     */
    private void deleteTask(int taskNo) {
        Task removed = tasks.removeTask(taskNo - 1);
        storage.save(tasks);
        ui.printTaskDeleted(removed, tasks.size());
    }

    /**
     * Handles any request to find all tasks on a specified date.
     *
     * @param request   The full user input string containing the date.
     */
    private void handleOnDate(String request) {
        LocalDate date = DateTimeUtil.parseDateTime(request.substring(3)).toLocalDate();
        List<Task> filteredTasks = tasks.getTasksOnDate(date);
        ui.printTasksOnDate(filteredTasks, date);
    }
}
