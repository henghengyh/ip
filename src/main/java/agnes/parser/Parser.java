package agnes.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import agnes.exception.InvalidCommandException;
import agnes.exception.InvalidDescriptionException;
import agnes.exception.InvalidTaskNumberException;
import agnes.exception.TaskIndexOutOfBoundsException;
import agnes.storage.Storage;
import agnes.task.Deadline;
import agnes.task.Event;
import agnes.task.Task;
import agnes.task.TaskList;
import agnes.task.ToDo;
import agnes.ui.Ui;
import agnes.util.DateTimeUtil;

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
     *
     * @return          The message to be shown to user.
     */
    public String parse(String request) {
        try {
            String keyword = request.split(" ")[0];
            Command command = Command.from(keyword);
            switch (command) {
            case HI:
                return ui.getWelcomeMessage();
            case BYE:
                return ui.getByeMessage();
            case LIST:
                return ui.getTasks(tasks);
            case ON:
                return handleOnDate(request);
            case MARK:
                return handleMark(request, true);
            case UNMARK:
                return handleMark(request, false);
            case DELETE:
                return handleDelete(request);
            case FIND:
                return handleFind(request);
            default:
                return handleCommands(request);
            }
        } catch (InvalidDescriptionException
                 | InvalidTaskNumberException
                 | TaskIndexOutOfBoundsException
                 | InvalidCommandException e) {
            return ui.getErrorMessage(e);
        }
    }

    /**
     * Handles any {@code Command} given to create a {@code Task}
     *
     * @param request   The full user input string.
     * @throws InvalidDescriptionException  If the task description or format is invalid.
     * @throws InvalidCommandException      If the command is not recognised.
     * @return          The message to be shown to user.
     */
    public String handleCommands(String request) throws InvalidDescriptionException, InvalidCommandException {
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
            return addTask(t);
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
                return addTask(t);
            } catch (DateTimeParseException e) {
                throw new InvalidDescriptionException(
                        "Date format should be yyyy-MM-dd or yyyy-MM-dd HHmm"
                );
            }
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
                return addTask(t);
            } catch (DateTimeParseException e) {
                throw new InvalidDescriptionException(
                        "Date format should be yyyy-MM-dd or yyyy-MM-dd HHmm"
                );
            }
        default:
            throw new InvalidCommandException("I don't understand what you're saying... TYPE PROPERLY LEH");
        }
    }

    /**
     * Executes the adding of a {@code Task} to the {@code TaskList}.
     * @param t The {@code Task} to be added.
     * @return          The message to be shown to user.
     */
    private String addTask(Task t) {
        tasks.addTask(t);
        storage.save(tasks);
        return ui.getTaskAdded(t, tasks.size());
    }

    /**
     * Handles any request to mark a {@code Task}.
     *
     * @param request   The full user input string.
     * @throws InvalidTaskNumberException       If the task number is invalid.
     * @throws TaskIndexOutOfBoundsException    If the task index is out of bounds.
     * @return          The message to be shown to user.
     */
    private String handleMark(String request, boolean mark)
            throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        String[] parts = request.split(" ");
        if (parts.length < 2) {
            throw new InvalidTaskNumberException("Don't play play... Give me a task number!");
        }
        int taskNo = tasks.checkTaskNumber(parts[1]);
        Task task = tasks.get(taskNo - 1);
        if (mark) {
            task.mark();
        } else {
            task.unmark();
        }
        storage.save(tasks);
        return ui.getTaskMarked(task, mark);
    }

    /**
     * Handles any request to delete a {@code Task} from a {@code TaskList}
     *
     * @param request   The full user input string.
     * @throws InvalidTaskNumberException       If the task number is invalid.
     * @throws TaskIndexOutOfBoundsException    If the task index is out of bounds.
     * @return          The message to be shown to user.
     */
    public String handleDelete(String request) throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        String[] parts = request.split(" ");
        if (parts.length < 2) {
            throw new InvalidTaskNumberException("Don't play play... Give me a task number!");
        }

        int taskNo = tasks.checkTaskNumber(parts[1]);
        Task removed = tasks.removeTask(taskNo - 1);
        storage.save(tasks);
        return ui.getTaskDeleted(removed, tasks.size());
    }


    /**
     * Handles any request to find all tasks on a specified date.
     *
     * @param request   The full user input string containing the date.
     * @return          The message to be shown to user.
     */
    private String handleOnDate(String request) {
        LocalDate date = DateTimeUtil.parseDateTime(request.substring(3)).toLocalDate();
        List<Task> filteredTasks = tasks.getTasksOnDate(date);
        return ui.getTasksOnDate(filteredTasks, date);
    }

    /**
     * Handles any request to find all tasks with a keyword.
     *
     * @param request   The full user input string containing the keyword.
     * @return          The message to be shown to user.
     */
    private String handleFind(String request) {
        String content = request.substring(5);
        return ui.getSearchTasks(tasks.find(content), content);
    }
}
