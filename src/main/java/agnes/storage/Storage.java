package agnes.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import agnes.task.Task;
import agnes.task.TaskList;

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
     * Writes the tasks into the file, overriding any existing content.
     * <p>
     *     This method uses Defensive Programming to ensure the parent
     *     folder exists and the file to be overwritten exists.
     * @param textToAdd     The full text to be written.
     * @throws IOException  never because Defensive Programming is done.
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

        // Using overwriting mode; Use (filePath, true) if want to append
        FileWriter fw = new FileWriter(filePath);
        fw.write(textToAdd);
        fw.close();
    }
}
