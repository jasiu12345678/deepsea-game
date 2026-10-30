package pt.isec.pa.deepsea.ui.states;

import javafx.scene.canvas.GraphicsContext;
import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.State;
import pt.isec.pa.deepsea.ui.Theme;
import pt.isec.pa.deepsea.ui.res.SoundManager;
import pt.isec.pa.deepsea.ui.utils.Sprites;

/**
 * Surface view: the open water where the ship sails, dives and visits the workshop.
 */
public class SurfaceState extends CanvasState {

    public SurfaceState(Manager manager) {
        super(manager);
    }

    @Override
    protected State matchingState() {
        return State.SURFACE;
    }

    @Override
    protected void registerHandlers() {
        setOnKeyPressed(event -> {
            boolean handled = true;
            switch (event.getCode()) {
                case UP, W -> moveShip(Directions.UP);
                case DOWN, S -> moveShip(Directions.DOWN);
                case LEFT, A -> moveShip(Directions.LEFT);
                case RIGHT, D -> moveShip(Directions.RIGHT);
                case ENTER -> manager.startDescent();
                case TAB -> manager.openWorkshop();
                default -> handled = false;
            }
            if (handled) event.consume();
        });
    }

    private void moveShip(Directions dir) {
        if (manager.navigate(dir)) {
            SoundManager.play(SoundManager.SHIP_MOVE);
        }
    }

    @Override
    protected void draw() {
        if (!isVisible()) return;

        clear(Theme.BG_DEEP);
        GraphicsContext gc = getGraphicsContext2D();

        int cols = GameConstants.SURFACE_WIDTH;
        int rows = GameConstants.SURFACE_HEIGHT;
        double tile = Math.min(getWidth() / cols, getHeight() / rows);
        double offsetX = (getWidth() - cols * tile) / 2;
        double offsetY = (getHeight() - rows * tile) / 2;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Sprites.water(gc, offsetX + x * tile, offsetY + y * tile, tile);
            }
        }
        Sprites.ship(gc, offsetX + manager.getShipX() * tile, offsetY + manager.getShipY() * tile, tile);
    }
}
