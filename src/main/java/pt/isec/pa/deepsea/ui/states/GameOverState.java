package pt.isec.pa.deepsea.ui.states;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import pt.isec.pa.deepsea.model.Manager;

public class GameOverState extends Pane {
    private final Manager manager;
    private final Canvas canvas;

    public GameOverState(Manager manager) {
        this.manager = manager;
        this.canvas = new Canvas(804, 768);
        this.getChildren().add(canvas);
        registerListeners();
        updateVisibility();
    }

    private void registerListeners() {
        manager.addClient("state", evt -> updateVisibility());
    }

    private void updateVisibility() {
        if (manager.getState() == null) return;
        boolean isGameOver = manager.getState().name().equals("GAME_OVER");
        this.setVisible(isGameOver);
        if (isGameOver) {
            draw();
        }
    }

    private void draw() {
        if (!this.isVisible()) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.rgb(25, 10, 10));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 54));
        gc.setFill(Color.rgb(231, 76, 60));
        gc.fillText("GAME OVER", canvas.getWidth() / 2, canvas.getHeight() / 2 - 40);


    }
}