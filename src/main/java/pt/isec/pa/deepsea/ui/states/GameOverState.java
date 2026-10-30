package pt.isec.pa.deepsea.ui.states;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.State;
import pt.isec.pa.deepsea.ui.Theme;

/**
 * End screen shown after the game is won or lost.
 */
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

        clear(Theme.BG_DEEP);
        GraphicsContext gc = getGraphicsContext2D();
        double cx = getWidth() / 2;
        double cy = getHeight() / 2;
        boolean victory = manager.isVictory();

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.setFill(Theme.MUTED);
        gc.setFont(Theme.font(12, FontWeight.BOLD));
        gc.fillText(victory ? "MISSION COMPLETE" : "MISSION FAILED", cx, cy - 56);

        gc.setFill(victory ? Theme.ACCENT : Theme.TEXT);
        gc.setFont(Theme.font(44, FontWeight.BOLD));
        gc.fillText(victory ? "All artifacts recovered" : "Game over", cx, cy - 16);

        gc.setFill(Theme.ACCENT_DARK);
        gc.fillRect(cx - 24, cy + 22, 48, 2);

        gc.setFill(Theme.MUTED);
        gc.setFont(Theme.font(13, FontWeight.NORMAL));
        gc.fillText("Artifacts  " + manager.getArtifactCount() + " / " + manager.getTotalArtifacts()
                + "      Ore  " + manager.getOreCount(), cx, cy + 50);
        gc.fillText("Start again from  Game > New", cx, cy + 76);
    }
}
