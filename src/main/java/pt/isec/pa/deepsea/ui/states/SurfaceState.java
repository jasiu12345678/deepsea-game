package pt.isec.pa.deepsea.ui.states;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.State;
import pt.isec.pa.deepsea.ui.res.SoundManager;
import pt.isec.pa.deepsea.ui.utils.ImageLoader;

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

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.rgb(135, 206, 235));
        gc.fillRect(0, 0, getWidth(), getHeight());

        int cols = GameConstants.SURFACE_WIDTH;
        int rows = GameConstants.SURFACE_HEIGHT;
        if (cols <= 0 || rows <= 0) return;

        double tileSize = Math.min(getWidth() / cols, getHeight() / rows);
        double offsetX = (getWidth() - cols * tileSize) / 2;
        double offsetY = (getHeight() - rows * tileSize) / 2;

        var waterImg = ImageLoader.getImage("water.png");
        if (waterImg != null) {
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    gc.drawImage(waterImg, offsetX + x * tileSize, offsetY + y * tileSize, tileSize, tileSize);
                }
            }
        }

        var shipImg = ImageLoader.getImage("ship.png");
        if (shipImg != null) {
            gc.drawImage(shipImg, offsetX + manager.getShipX() * tileSize, offsetY + manager.getShipY() * tileSize, tileSize, tileSize);
        }
    }
}