package pt.isec.pa.deepsea.model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Log {
    private static Log instance = null;
    private final List<String> logs = new ArrayList<>();

    private Log() {}

    public static Log getInstance() {
        if (instance == null) instance = new Log();
        return instance;
    }

    public void addLog(String msg) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logs.add("[" + timestamp + "] " + msg);
    }

    public List<String> getLogs() { return Collections.unmodifiableList(logs); }
    public void clearLogs() {logs.clear();}

    public void saveToFile() {
        try (FileWriter fw = new FileWriter(GameConstants.LOG_FILENAME)) {
            for (String s : logs) {
                fw.write(s + "\n");
            }
        } catch (IOException e) {
            System.out.println("error while saving logs: " + e.getMessage());
        }
    }

    public List<String> getRecentLogs(int count) {
        if (logs.isEmpty()) return Collections.emptyList();
        int size = logs.size();
        return logs.subList(Math.max(0, size - count), size);
    }
}