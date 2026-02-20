package agnes.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import agnes.task.Deadline;
import agnes.task.Event;
import agnes.task.Task;
import agnes.task.TaskList;
import agnes.task.ToDo;
import agnes.util.DateTimeUtil;

/**
 * Handles saving content to local storage.
 * <p>
 * The {@code Storage} class is responsible for writing data to a file
 * so that data can be retrieved locally, only.
 */
public class Storage {
    private String filePath;

    /**
     * Creates a {@code Storage} with a given filePath.
     *
     * @param filePath  The file path where data will be saved.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves all tasks in the {@code TaskList} to the storage file.
     * <p>
     * Each task is converted to a file-friendly format and written to disk.
     * If an {@code IOException} occurs during writing, the error is caught
     * and handled.
     *
     * @param tasks The {@code TaskList} containing tasks to be saved.
     */
    public void save(TaskList tasks) {
        StringBuilder sb = new StringBuilder();
        for (Task t : tasks.getAll()) {
            sb.append(t.toFileFormat()).append("\n");
        }
        try {
            writeToFile(sb.toString());
        } catch (IOException e) {
            // Since writeToFile conducts defensive programming checks, we
            // Do not expect any agnes.exception thrown by it
            return;
        }
    }

    /**
     * Loads all tasks from the storage file into the provided {@code TaskList}.
     * <p>
     * Reads the file line by line and parses each line into the appropriate
     * Task subtype (ToDo, Deadline, or Event). If the file doesn't exist or
     * the folder doesn't exist, no tasks are loaded. If a line cannot be parsed,
     * it is skipped.
     *
     * @param tasks The {@code TaskList} to load tasks into.
     */
    public void load(TaskList tasks) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                Task task = parseTaskFromLine(line);
                if (task != null) {
                    tasks.addTask(task);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks from file: " + e.getMessage());
        }
    }

    /**
     * Parses a single line from the storage file into a Task object.
     * <p>
     * Expected formats:
     * - ToDo: "T | 0/1 | description"
     * - Deadline: "D | 0/1 | description | formatted_datetime" (or legacy "E | 0/1 | description | formatted_datetime")
     * - Event: "E | 0/1 | description | from formatted_datetime to: formatted_datetime"
     *
     * @param line The line to parse.
     * @return The parsed Task, or null if the line format is invalid.
     */
    private Task parseTaskFromLine(String line) {
        try {
            String[] parts = line.split(" \\| ", 4);
            if (parts.length < 3) {
                return null;
            }

            String taskType = parts[0].trim();
            boolean isComplete = parts[1].trim().equals("1");
            String description = parts[2].trim();

            Task task = null;

            switch (taskType) {
            case "T":
                task = new ToDo(description);
                break;
            case "D":
                if (parts.length >= 4) {
                    String dateTimeStr = parts[3].trim();
                    LocalDateTime dateTime = DateTimeUtil.parseFormattedDateTime(dateTimeStr);
                    task = new Deadline(description, dateTime);
                }
                break;
            case "E":
                if (parts.length >= 4) {
                    String dateTimeOrFromToStr = parts[3].trim();

                    if (dateTimeOrFromToStr.contains(" to: ")) {
                        String[] fromTo = dateTimeOrFromToStr.split(" to: ");
                        String fromStr = fromTo[0].replace("from ", "").trim();
                        String toStr = fromTo[1].trim();
                        LocalDateTime fromDateTime = DateTimeUtil.parseFormattedDateTime(fromStr);
                        LocalDateTime toDateTime = DateTimeUtil.parseFormattedDateTime(toStr);
                        task = new Event(description, fromDateTime, toDateTime);
                    } else {
                        LocalDateTime dateTime = DateTimeUtil.parseFormattedDateTime(dateTimeOrFromToStr);
                        task = new Deadline(description, dateTime);
                    }
                }
                break;
            default:
                return null;
            }

            // Set task completion status if it was marked as complete
            if (task != null && isComplete) {
                task.setMarked();
            }

            return task;
        } catch (Exception e) {
            System.err.println("Error parsing task line: " + line + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Writes the tasks into the file, overriding any existing content.
     * <p>
     * This method uses Defensive Programming to ensure the parent
     * folder exists and the file can be created if it doesn't exist.
     * If the folder already exists, it is not re-created.
     *
     * @param textToAdd     The full text to be written.
     * @throws IOException  If file operations fail.
     */
    private void writeToFile(String textToAdd) throws IOException {
        File file = new File(filePath);

        // Defensive Programming, verify
        // 1. The parent folder exist
        // 2. The file to be overwritten in exists
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        //  Using overwriting mode; Use (filePath, true) if want to append
        FileWriter fw = new FileWriter(filePath);
        fw.write(textToAdd);
        fw.close();
    }
}
