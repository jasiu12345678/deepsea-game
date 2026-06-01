package pt.isec.pa.deepsea.ui.states;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.State;
import pt.isec.pa.deepsea.ui.res.SoundManager;
import pt.isec.pa.deepsea.ui.utils.ImageLoader;

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
        boolean hadDrone = manager.hasActiveDrone();
        int hpBefore = manager.getActiveDroneHP();
        boolean moved = manager.navigate(dir);
        if (hadDrone && !manager.hasActiveDrone()) {
            SoundManager.playForever(SoundManager.DRONE_DESTROYED);
        } else if (manager.hasActiveDrone() && manager.getActiveDroneHP() < hpBefore) {
            int tx = manager.getDroneX();
            int ty = manager.getDroneY();
            switch (dir) {
                case UP -> ty--;
                case DOWN -> ty++;
                case LEFT -> tx--;
                case RIGHT -> tx++;
            }
            if ("marineanimal.png".equals(manager.getTrenchElementImage(tx, ty))) {
                SoundManager.playThen(SoundManager.DRONE_MOVE, SoundManager.DRONE_ANIMAL);
            } else {
                SoundManager.play(SoundManager.DRONE_HIT);
            }
        } else if (moved) {
            SoundManager.play(SoundManager.DRONE_MOVE);
        }
    }

    @Override
    protected void draw() {
        if (!isVisible()) return;

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.rgb(20, 50, 80));
        gc.fillRect(0, 0, getWidth(), getHeight());

        int cols = GameConstants.TRENCH_WIDTH;
        int rows = GameConstants.TRENCH_HEIGHT;
        if (cols <= 0 || rows <= 0) return;

        double tileSize = Math.min(getWidth() / cols, getHeight() / rows);
        double offsetX = (getWidth() - cols * tileSize) / 2;
        double offsetY = (getHeight() - rows * tileSize) / 2;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                String imgName = manager.getTrenchElementImage(x, y);
                if (imgName != null && !imgName.isEmpty()) {
                    var img = ImageLoader.getImage(imgName);
                    if (img != null) {
                        gc.drawImage(img, offsetX + x * tileSize, offsetY + y * tileSize, tileSize, tileSize);
                    }
                }
            }
        }

        if (manager.hasActiveDrone()) {
            var droneImg = ImageLoader.getImage("drone.png");
            if (droneImg != null) {
                gc.drawImage(droneImg, offsetX + manager.getDroneX() * tileSize, offsetY + manager.getDroneY() * tileSize, tileSize, tileSize);
            }
        }
    }
}