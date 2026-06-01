package pt.isec.pa.deepsea.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class RecentGames {
    private static final String FILE = "recent.txt";
    private static final int MAX = 5;

    private RecentGames() {}

    public static List<String> load() {
        try {
            Path p = Path.of(FILE);
            if (!Files.exists(p)) return new ArrayList<>();
            return new ArrayList<>(Files.readAllLines(p));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void add(String path) {
        List<String> recent = load();
        recent.remove(path);
        recent.add(0, path);
        while (recent.size() > MAX) recent.remove(recent.size() - 1);
        try {
            Files.write(Path.of(FILE), recent);
        } catch (IOException e) {
            System.err.println("Could not save recent games: " + e.getMessage());
        }
    }
}