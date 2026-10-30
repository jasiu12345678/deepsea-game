package pt.isec.pa.deepsea.ui.states;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.State;
import pt.isec.pa.deepsea.ui.Theme;

/**
 * Sliding-tile puzzle shown when an artifact is being recovered.
 */
public class MiniGameState extends CanvasState {

    public MiniGameState(Manager manager) {
        super(manager);
    }

    @Override
    protected State matchingState() {
        return State.MINIGAME;
    }

    @Override
    protected void registerHandlers() {
        setOnKeyPressed(event -> {
            boolean handled = true;
            switch (event.getCode()) {
                case UP, W -> manager.navigate(Directions.UP);
                case DOWN, S -> manager.navigate(Directions.DOWN);
                case LEFT, A -> manager.navigate(Directions.LEFT);
                case RIGHT, D -> manager.navigate(Directions.RIGHT);
                default -> handled = false;
            }
            if (handled) event.consume();
        });
    }

    @Override
    protected void draw() {
        if (!isVisible()) return;

        clear(Theme.BG_DEEP);
        int[][] puzzle = manager.getPuzzleGrid();
        if (puzzle == null) return;

        GraphicsContext gc = getGraphicsContext2D();
        int size = GameConstants.MINIGAME_GRID_SIZE;
        double board = Math.min(getWidth(), getHeight()) * 0.55;
        double tile = board / size;
        double offsetX = (getWidth() - board) / 2;
        double offsetY = (getHeight() - board) / 2;
        double cx = getWidth() / 2;

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.setFill(Theme.MUTED);
        gc.setFont(Theme.font(12, FontWeight.BOLD));
        gc.fillText("RECOVER THE ARTIFACT", cx, offsetY - 62);
        gc.setFill(Theme.ACCENT);
        gc.setFont(Theme.font(26, FontWeight.BOLD));
        gc.fillText(manager.getPuzzleMovesLeft() + " moves left", cx, offsetY - 32);

        gc.setFill(Theme.BG);
        gc.fillRoundRect(offsetX - 14, offsetY - 14, board + 28, board + 28, 16, 16);
        gc.setStroke(Theme.LINE);
        gc.setLineWidth(1);
        gc.strokeRoundRect(offsetX - 14, offsetY - 14, board + 28, board + 28, 16, 16);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int value = puzzle[y][x];
                if (value == 0) continue;

                double tx = offsetX + x * tile + 4;
                double ty = offsetY + y * tile + 4;
                double ts = tile - 8;
                boolean movable = manager.isPuzzleTileMovable(x, y);

                gc.setFill(movable ? Theme.ACCENT_DEEP : Theme.BG_RAISED);
                gc.fillRoundRect(tx, ty, ts, ts, 12, 12);
                gc.setStroke(movable ? Theme.ACCENT_DARK : Theme.LINE);
                gc.setLineWidth(movable ? 1.5 : 1);
                gc.strokeRoundRect(tx, ty, ts, ts, 12, 12);

                gc.setFill(movable ? Theme.ACCENT : Theme.TEXT);
                gc.setFont(Theme.font(tile * 0.34, FontWeight.BOLD));
                gc.fillText(String.valueOf(value), tx + ts / 2, ty + ts / 2);
            }
        }

        gc.setFill(Theme.MUTED);
        gc.setFont(Theme.font(12, FontWeight.NORMAL));
        gc.fillText("Slide the highlighted tiles into the gap to restore the order", cx, offsetY + board + 44);
    }
}
