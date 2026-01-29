package agnes.parser;

import agnes.exception.InvalidCommandException;
import agnes.exception.InvalidDescriptionException;
import agnes.exception.InvalidTaskNumberException;
import agnes.exception.TaskIndexOutOfBoundsException;
import agnes.task.*;
import agnes.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {
    private void handleCommands(String request) throws InvalidDescriptionException, InvalidCommandException {
        String action = request.split(" ")[0];
        Command cmd = Command.from(action);
        Task t;
        String content;
        switch (cmd) {
            case Command.TODO:
                if (request.length() <= 5) {
                    throw new InvalidDescriptionException(
                            "Hellos, tell me what description you want!"
                    );
                }

                content = request.substring(5);
                t = new ToDo(content.trim());
                addTask(t);
                break;
            case Command.DEADLINE:
                if (!request.contains(" /by ")) {
                    throw new InvalidDescriptionException(
                            "Specify your deadline using '/by'..."
                    );
                }

                content = request.substring(9);
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
            case Command.EVENT:
                if (!request.contains(" /from ") || !request.contains(" /to ")) {
                    throw new InvalidDescriptionException(
                            "Specify your event duration using '/from' and '/to'..."
                    );
                }

                content = request.substring(6);
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

    private void handleMark(String request, boolean mark) throws InvalidTaskNumberException, TaskIndexOutOfBoundsException {
        String[] parts = request.split(" ");
        if (parts.length < 2) {
            throw new InvalidTaskNumberException("Don't play play... Give me a agnes.task number!");
        }
        int taskNo = checkTaskNumber(parts[1]);
        markTask(tasks.get(taskNo - 1), mark);
        storage.save(tasks);
    }
}
