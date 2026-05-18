package pt.isec.pa.deepsea.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.deepsea.model.Manager;

public class MainJFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        Manager manager = new Manager();
        RootPane root = new RootPane(manager);
        Scene scene = new Scene(root, 1024, 768);

        root.requestFocus();

        primaryStage.setTitle("Deep Sea Mining");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}