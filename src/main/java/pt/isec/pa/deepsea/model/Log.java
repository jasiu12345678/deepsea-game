package pt.isec.pa.deepsea.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Log implements Serializable {
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

    public void saveToFile(String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            for (String s : logs) out.println(s);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<String> getRecentLogs(int count) {
        if (logs.isEmpty()) return Collections.emptyList();
        int size = logs.size();
        return logs.subList(Math.max(0, size - count), size);
    }
}