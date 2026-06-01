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

        root.requestFocus();

        second.setTitle("2");
        second.setScene(new Scene(new RootPane(manager)));
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