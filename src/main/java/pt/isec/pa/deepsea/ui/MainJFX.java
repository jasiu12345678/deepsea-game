package pt.isec.pa.deepsea.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.Manager;

/**
 * Main JavaFX application. Creates the window, the {@link Manager} and the {@link RootPane}.
 */

public class MainJFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        Stage second = new Stage();

        Manager manager = new Manager();
        RootPane root = new RootPane(manager);
        Scene scene = new Scene(root, 1024, 768);

        scene.getStylesheets().add(Theme.STYLESHEET);
        root.requestFocus();

        second.setTitle("Deep Sea Mining - Second View");
        Scene secondScene = new Scene(new RootPane(manager), 1024, 768);
        secondScene.getStylesheets().add(Theme.STYLESHEET);
        second.setScene(secondScene);
        second.show();

        primaryStage.setTitle("Deep Sea Mining");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}