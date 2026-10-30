package pt.isec.pa.deepsea.ui.states;

import javafx.scene.canvas.GraphicsContext;
import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.State;
import pt.isec.pa.deepsea.ui.Theme;
import pt.isec.pa.deepsea.ui.utils.Sprites;

/**
 * Trench view: the vertical corridor the drone descends through to reach the seabed.
 */
public class TrenchState extends CanvasState {

    public TrenchState(Manager manager) {
        super(manager);
    }

    @Override
    protected State matchingState() {
        return State.TRENCH;
    }

    @Override
    protected void registerHandlers() {
        setOnKeyPressed(event -> {
            boolean handled = true;
            switch (event.getCode()) {
                case UP, W -> moveDrone(Directions.UP);
                case DOWN, S -> moveDrone(Directions.DOWN);
                case LEFT, A -> moveDrone(Directions.LEFT);
                case RIGHT, D -> moveDrone(Directions.RIGHT);
                default -> handled = false;
            }
            if (handled) event.consume();
        });
    }

    private void moveDrone(Directions dir) {
        manager.navigate(dir);
        playMoveSound();
    }

    @Override
    protected void draw() {
        if (!isVisible()) return;

        clear(Theme.BG_DEEP);
        GraphicsContext gc = getGraphicsContext2D();

        int cols = GameConstants.TRENCH_WIDTH;
        int rows = GameConstants.TRENCH_HEIGHT;
        double tile = Math.min(getWidth() / cols, getHeight() / rows);
        double offsetX = (getWidth() - cols * tile) / 2;
        double offsetY = (getHeight() - rows * tile) / 2;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Sprites.cell(gc, manager.getTrenchElementType(x, y), offsetX + x * tile, offsetY + y * tile, tile);
            }
        }

        if (manager.hasActiveDrone()) {
            Sprites.drone(gc, offsetX + manager.getDroneX() * tile, offsetY + manager.getDroneY() * tile, tile);
        }
    }
}
