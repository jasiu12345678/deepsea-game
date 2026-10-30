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

/**
 * Menu bar (game, log and sound options) and the real-time log window.
 */
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
        manager.addClient(Manager.LOG, evt -> updateLogText());
        updateLogText();
    }

    private void initLogWindow() {
        logStage = new Stage();
        logStage.setTitle("Deep Sea Mining - Log");
        logTextArea = new TextArea();
        logTextArea.setEditable(false);
        logTextArea.setWrapText(true);
        logTextArea.getStyleClass().add("log-area");

        Scene scene = new Scene(logTextArea, 560, 400);
        scene.getStylesheets().add(Theme.STYLESHEET);
        logStage.setScene(scene);
    }

    private void updateLogText() {
        logTextArea.setText(String.join("\n", manager.getLogs()));
        logTextArea.setScrollTop(Double.MAX_VALUE);
    }

    private void refreshRecentMenu(Menu menuRecent) {
        menuRecent.getItems().clear();
        var recent = manager.getRecentGames();
        if (recent.isEmpty()) {
            MenuItem empty = new MenuItem("No recent games");
            empty.setDisable(true);
            menuRecent.getItems().add(empty);
            return;
        }
        for (String path : recent) {
            MenuItem item = new MenuItem(path);
            item.setOnAction(ev -> manager.load(path));
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

        itemNew.setOnAction(e -> manager.newGame());
        itemOpen.setOnAction(e -> {
            File file = new FileChooser().showOpenDialog(getScene().getWindow());
            if (file != null) manager.load(file.getAbsolutePath());
        });
        itemSave.setOnAction(e -> {
            File file = new FileChooser().showSaveDialog(getScene().getWindow());
            if (file != null) manager.save(file.getAbsolutePath());
        });
        itemExit.setOnAction(e -> Platform.exit());
        menuGame.setOnShowing(e -> refreshRecentMenu(menuRecent));
        menuGame.getItems().addAll(itemNew, itemOpen, menuRecent, itemSave, itemExit);

        Menu menuLog = new Menu("Log");
        MenuItem itemToggleLog = new MenuItem("Show / hide");
        MenuItem itemSaveLog = new MenuItem("Save log...");
        MenuItem itemClearLog = new MenuItem("Clear log");

        itemToggleLog.setOnAction(e -> {
            if (logStage.isShowing()) {
                logStage.hide();
            } else {
                updateLogText();
                logStage.show();
            }
        });
        itemSaveLog.setOnAction(e -> {
            File file = new FileChooser().showSaveDialog(getScene().getWindow());
            if (file != null) manager.saveLogs(file.getAbsolutePath());
        });
        itemClearLog.setOnAction(e -> manager.clearLogs());
        menuLog.getItems().addAll(itemToggleLog, itemSaveLog, itemClearLog);

        Menu menuSound = new Menu("Sound");
        CheckMenuItem itemToggleSound = new CheckMenuItem("Sound enabled");
        itemToggleSound.selectedProperty().bindBidirectional(SoundManager.enabledProperty());
        menuSound.getItems().add(itemToggleSound);

        getMenus().addAll(menuGame, menuLog, menuSound);
    }
}
