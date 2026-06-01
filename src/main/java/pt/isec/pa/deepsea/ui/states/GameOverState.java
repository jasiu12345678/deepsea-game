package pt.isec.pa.deepsea.ui.states;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.State;

public class GameOverState extends CanvasState {

    public GameOverState(Manager manager) {
        super(manager);
    }

    @Override
    protected State matchingState() {
        return State.GAME_OVER;
    }

    @Override
    protected void draw() {
        if (!isVisible()) return;

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.rgb(25, 10, 10));
        gc.fillRect(0, 0, getWidth(), getHeight());

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 54));
        gc.setFill(Color.rgb(231, 76, 60));
        gc.fillText("GAME OVER", getWidth() / 2, getHeight() / 2 - 40);
    }
}