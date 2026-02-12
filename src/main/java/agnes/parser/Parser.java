package agnes.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private static final int TODO_CMD_LENGTH = 5;
    private static final int DEADLINE_CMD_LENGTH = 8;
    private static final int EVENT_CMD_LENGTH = 5;
    private static final int ON_CMD_LENGTH = 3;
    private static final int FIND_CMD_LENGTH = 5;
    private static final int KNS_CMD_LENGTH = 3;
    private static final int UPDATE_LENGTH = 7;

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
    public List<String> parse(String request) {
        assert request != null : "User request should never be null";
        assert !request.isBlank() : "User request should not be blank";
        try {
            String keyword = request.split(" ")[0];
            assert !keyword.isBlank() : "Command keyword should exist";
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
            case UPDATE:
                return handleUpdate(request);
            case KNS:
                return handleKns(request);
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
     * @return          The list of messages to be shown to user.
     * @throws InvalidDescriptionException  If the task description or format is invalid.
     * @throws InvalidCommandException      If the command is not recognised.
     */
    public List<String> handleCommands(String request) throws InvalidDescriptionException, InvalidCommandException {
        String action = request.split(" ")[0];
        Command cmd = Command.from(action);
        assert cmd == Command.TODO || cmd == Command.DEADLINE || cmd == Command.EVENT
                : "handleCommands should only process task-creation commands";
        switch (cmd) {
        case TODO:
            return handleToDo(request);
        case DEADLINE:
            return handleDeadline(request);
        case EVENT:
            return handleEvent(request);
        default:
            throw new InvalidCommandException("I don't understand what you're saying...");
        }
    }

    /**
     * Handles creation and addition of a ToDo task.
     *
     * @param request The full user input string starting with the "todo" keyword.
     * @return        The list of messages to be shown to user.
     * @throws InvalidDescriptionException If the task description or format is invalid.
     */
    private List<String> handleToDo(String request) throws InvalidDescriptionException {
        if (request.length() <= TODO_CMD_LENGTH) {
            throw new InvalidDescriptionException("Tell me what description you want!");
        }
        String content = request.substring(TODO_CMD_LENGTH).trim();
        Task t = new ToDo(content);
        return addTask(t);
    }

    /**
     * Handles creation and addition of a Deadline task.
     *
     * @param request The full user input string starting with the "deadline" keyword.
     * @return        The list of messages to be shown to user.
     * @throws InvalidDescriptionException If the task description or format is invalid.
     */
    private List<String> handleDeadline(String request) throws InvalidDescriptionException {
        if (!request.contains(" /by ")) {
            throw new InvalidDescriptionException("Specify your deadline using '/by'...");
        }
        String content = request.substring(DEADLINE_CMD_LENGTH);
        String[] deadlineInfo = content.split(" /by ");
        try {
            LocalDateTime by = DateTimeUtil.parseDateTime(deadlineInfo[1].trim());
            Task t = new Deadline(deadlineInfo[0].trim(), by);
            return addTask(t);
        } catch (DateTimeParseException e) {
            throw new InvalidDescriptionException("Date format should be yyyy-MM-dd or yyyy-MM-dd HHmm");
        }
    }

    /**
     * Handles creation and addition of an Event task.
     *
     * @param request The full user input string starting with the "event" keyword.
     * @return        The list of messages to be shown to user.
     * @throws InvalidDescriptionException If the task description or format is invalid.
     */
    private List<String> handleEvent(String request) throws InvalidDescriptionException {
        if (!request.contains(" /from ") || !request.contains(" /to ")) {
            throw new InvalidDescriptionException("Specify event duration using '/from' and '/to'...");
        }
        String content = request.substring(EVENT_CMD_LENGTH);
        String[] eventInfo = content.split(" /from ");
        String[] fromToInfo = eventInfo[1].split(" /to ");
        try {
            LocalDateTime from = DateTimeUtil.parseDateTime(fromToInfo[0].trim());
            LocalDateTime to = DateTimeUtil.parseDateTime(fromToInfo[1].trim());
            Task t = new Event(eventInfo[0].trim(), from, to);
            return addTask(t);
        } catch (DateTimeParseException e) {
            throw new InvalidDescriptionException("Date format should be yyyy-MM-dd or yyyy-MM-dd HHmm");
        }
    }

    /**
     * Executes the adding of a {@code Task} to the {@code TaskList}.
     * @param t The {@code Task} to be added.
     * @return          The message to be shown to user.
     */
    private List<String> addTask(Task t) {
        assert t != null : "Task being added should never be null";
        int oldSize = tasks.size();
        tasks.addTask(t);
        assert tasks.size() == oldSize + 1 : "TaskList size should increase after adding";
        storage.save(tasks);
        return ui.getTaskAdded(t, tasks.size());
    }

    /**
     * Handles any request to setMarked a {@code Task}.
     *
     * @param request   The full user input string.
     * @return          The list of messages to be shown to user.
     * @throws InvalidTaskNumberException       If the task number is invalid.
     * @throws TaskIndexOutOfBoundsException    If the task index is out of bounds.
     */
    private List<String> handleMark(String request, boolean mark)
            throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        String[] parts = request.split(" ");
        if (parts.length < 2) {
            throw new InvalidTaskNumberException("Don't play play... Give me a task number!");
        }
        int taskNo = tasks.checkTaskNumber(parts[1]);
        assert taskNo > 0 && taskNo <= tasks.size() : "Task number must be within list bounds";
        Task task = tasks.get(taskNo - 1);
        if (mark) {
            task.setMarked();
        } else {
            task.setUnmarked();
        }
        storage.save(tasks);
        return ui.getTaskMarked(task, mark);
    }

    /**
     * Handles any request to delete a {@code Task} from a {@code TaskList}
     *
     * @param request   The full user input string.
     * @return          The list of messages to be shown to user.
     * @throws InvalidTaskNumberException       If the task number is invalid.
     * @throws TaskIndexOutOfBoundsException    If the task index is out of bounds.
     */
    public List<String> handleDelete(String request) throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        String[] parts = request.split(" ");
        if (parts.length < 2) {
            throw new InvalidTaskNumberException("Don't play play... Give me a task number!");
        }

        int taskNo = tasks.checkTaskNumber(parts[1]);
        assert taskNo > 0 && taskNo <= tasks.size() : "Task number must be valid before deletion";
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
    private List<String> handleOnDate(String request) {
        LocalDate date = DateTimeUtil.parseDateTime(request.substring(ON_CMD_LENGTH)).toLocalDate();
        assert date != null : "Parsed date should not be null";
        List<Task> filteredTasks = tasks.getTasksOnDate(date);
        return ui.getTasksOnDate(filteredTasks, date);
    }

    /**
     * Handles any request to find all tasks with a keyword.
     *
     * @param request   The full user input string containing the keyword.
     * @return          The message to be shown to user.
     */
    private List<String> handleFind(String request) {
        String content = request.substring(FIND_CMD_LENGTH);
        assert !content.isBlank() : "Find keyword should not be blank";
        return ui.getSearchTasks(tasks.find(content), content);
    }

    /**
     * Handles a curse word request.
     *
     * @param request   The full user input string containing the curse word.
     * @return          The message to be shown to user.
     */
    private List<String> handleKns(String request) {
        String content = request.substring(KNS_CMD_LENGTH).strip();
        return ui.getKnsResponse(content);
    }

    /**
     * Handles an update request.
     *
     * @param request   The full user input string containing the field to be updated and value to input.
     * @return          The message to be shown to user.
     */
    private List<String> handleUpdate(String request) throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        String content = request.substring(UPDATE_LENGTH).strip();
        String index = content.substring(0, content.indexOf(" ")).strip();
        String remaining = content.substring(content.indexOf(" ")).strip();
        String field = remaining.substring(0, content.indexOf(" ")).strip();
        String value = remaining.substring(content.indexOf(" ")).strip();
        this.tasks.updateTask(index, field, value);

        return ui.getTaskUpdated(task);
    }

}
