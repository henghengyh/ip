package agnes.storage;

import agnes.task.TaskList;
import agnes.task.Task;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }
    public void save(TaskList tasks) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Task t : tasks.getAll()) {
            sb.append(t.toFileFormat()).append("\n");
        }

        // Since writeToFile conducts defensive programming checks, we
        // Do not expect any agnes.exception thrown by it
        writeToFile(sb.toString());
    }

    private void writeToFile(String textToAdd) throws IOException {
        File file = new File(filePath);

        // Defensive Programming, verify
        // 1. The parent folder exist
        // 2. The file to be overwritten in exists
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
        if (!file.exists()) file.createNewFile();

        // Using overwriting mode; Use (filePath, true) if want to append
        FileWriter fw = new FileWriter(filePath);
        fw.write(textToAdd);
        fw.close();
    }
}