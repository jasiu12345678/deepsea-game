package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.ui.res.SoundManager;

import java.io.File;
import java.io.FileWriter;

public class MainUI extends MenuBar {
    private final Manager manager;
    private Stage logStage;
    private TextArea logTextArea;

    public MainUI(Manager manager) {
        this.manager = manager;
        createViews();
        registerListeners();
    }

    private void createViews() {
        initLogWindow();
        createMenus();
    }

    private void registerListeners() {
        manager.addClient("log", evt -> updateLogText());
        updateLogText();
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

    private void updateLogText() {
        StringBuilder sb = new StringBuilder();
        for (String s : manager.getLogs()) sb.append(s).append("\n");
        logTextArea.setText(sb.toString());
        logTextArea.setScrollTop(Double.MAX_VALUE);
    }

    private void refreshRecentMenu(Menu menuRecent) {
        menuRecent.getItems().clear();
        var recent = RecentGames.load();
        if (recent.isEmpty()) {
            MenuItem empty = new MenuItem("(no recent games)");
            empty.setDisable(true);
            menuRecent.getItems().add(empty);
            return;
        }
        for (String path : recent) {
            MenuItem item = new MenuItem(path);
            item.setOnAction(ev -> { if (manager.load(path)) RecentGames.add(path); });
            menuRecent.getItems().add(item);
        }
    }

    private void createMenus() {
        Menu menuGame = new Menu("Game");
        MenuItem itemNew = new MenuItem("New");
        MenuItem itemOpen = new MenuItem("Open...");
        Menu menuRecent = new Menu("Open recent");
        MenuItem itemSave = new MenuItem("Save as...");
        MenuItem itemExit = new MenuItem("Exit");

        itemOpen.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(getScene().getWindow());
            if (file != null && manager.load(file.getAbsolutePath()))
                RecentGames.add(file.getAbsolutePath());
        });
        itemSave.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            File file = fc.showSaveDialog(getScene().getWindow());
            if (file != null && manager.save(file.getAbsolutePath()))
                RecentGames.add(file.getAbsolutePath());
        });

        menuGame.setOnShowing(e-> refreshRecentMenu(menuRecent));
        itemExit.setOnAction(e -> Platform.exit());
        itemNew.setOnAction(e -> manager.newGame());

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
                    for (String s : manager.getLogs()) {
                        writer.write(s + "\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        itemClearLog.setOnAction(e -> {
            manager.clearLogs();
        });

        menuLog.getItems().addAll(itemToggleLog, itemSaveLog, itemClearLog);

        Menu menuSound = new Menu("Sound");
        CheckMenuItem itemToggleSound = new CheckMenuItem("Sound enabled");
        itemToggleSound.selectedProperty().bindBidirectional(SoundManager.enabledProperty());
        menuSound.getItems().add(itemToggleSound);
        this.getMenus().addAll(menuGame, menuLog, menuSound);

    }
}