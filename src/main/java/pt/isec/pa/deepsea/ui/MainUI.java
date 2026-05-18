package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.Log;

import java.io.File;
import java.io.FileWriter;

public class MainUI extends MenuBar {
    private final Manager manager;
    private Stage logStage;
    private TextArea logTextArea;

    public MainUI(Manager manager) {
        this.manager = manager;
        initLogWindow();
        createMenus();
        registerLogListeners();
    }

    private void initLogWindow() {
        logStage = new Stage();
        logStage.setTitle("Deep Sea Mining - Realtime Logs");
        logTextArea = new TextArea();
        logTextArea.setEditable(false);
        logTextArea.setWrapText(true);

        logTextArea.setStyle(
                "-fx-control-inner-background: #0d1624; " +
                        "-fx-text-fill: #a2b4cd; " +
                        "-fx-font-family: 'Consolas', monospace; " +
                        "-fx-font-size: 12px;"
        );

        logStage.setScene(new Scene(logTextArea, 550, 400));
    }

    private void registerLogListeners() {
        manager.addClient("data", evt -> updateLogText());
        manager.addClient("state", evt -> updateLogText());
        updateLogText();
    }

    private void updateLogText() {
        Platform.runLater(() -> {
            StringBuilder sb = new StringBuilder();
            for (String s : Log.getInstance().getLogs()) {
                sb.append(s).append("\n");
            }
            logTextArea.setText(sb.toString());
            logTextArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void createMenus() {
        Menu menuGame = new Menu("Game");
        MenuItem itemNew = new MenuItem("New");
        MenuItem itemOpen = new MenuItem("Open...");
        Menu menuRecent = new Menu("Open recent");
        MenuItem itemSave = new MenuItem("Save as...");
        MenuItem itemExit = new MenuItem("Exit");

        itemOpen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (file != null) manager.load(file.getAbsolutePath());
        });

        itemSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (file != null) manager.save(file.getAbsolutePath());
        });

        itemExit.setOnAction(e -> Platform.exit());

        menuGame.getItems().addAll(itemNew, itemOpen, menuRecent, itemSave, itemExit);

        Menu menuLog = new Menu("Log");
        MenuItem itemToggleLog = new MenuItem("Show/Hide");
        MenuItem itemSaveLog = new MenuItem("Save logs");
        MenuItem itemClearLog = new MenuItem("Clear logs");

        itemToggleLog.setOnAction(e -> {
            if (logStage.isShowing()) {
                logStage.hide();
            } else {
                updateLogText();
                logStage.show();
            }
        });

        itemSaveLog.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (file != null) {
                try (FileWriter writer = new FileWriter(file)) {
                    for (String s : Log.getInstance().getLogs()) {
                        writer.write(s + "\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        itemClearLog.setOnAction(e -> {
            manager.clearLogs();
            updateLogText();
        });

        menuLog.getItems().addAll(itemToggleLog, itemSaveLog, itemClearLog);

        this.getMenus().addAll(menuGame, menuLog);
    }
}