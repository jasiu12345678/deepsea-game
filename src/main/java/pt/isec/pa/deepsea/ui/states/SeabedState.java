package pt.isec.pa.deepsea.ui.states;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.State;
import pt.isec.pa.deepsea.ui.res.SoundManager;
import pt.isec.pa.deepsea.ui.utils.ImageLoader;

public class SeabedState extends CanvasState {

    public SeabedState(Manager manager) {
        super(manager);
    }

    @Override
    protected State matchingState() {
        return State.SEABED;
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
                case SPACE -> manager.collectItem();
                case BACK_SPACE -> manager.startAscent();
                case Y -> { if (manager.isAwaitingConfirmation()) manager.confirmCollection(true); }
                case N -> { if (manager.isAwaitingConfirmation()) manager.confirmCollection(false); }
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
            if (("marineanimal.png".equals(manager.getSeabedElementImage(tx, ty))) || ("monster.png").equals(manager.getSeabedElementImage(tx,ty))) {
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
        gc.setFill(Color.rgb(10, 20, 40));
        gc.fillRect(0, 0, getWidth(), getHeight());

        int cols = GameConstants.SEABED_WIDTH;
        int rows = GameConstants.SEABED_HEIGHT;
        if (cols <= 0 || rows <= 0) return;

        double tileSize = Math.min(getWidth() / cols, getHeight() / rows);
        double offsetX = (getWidth() - cols * tileSize) / 2;
        double offsetY = (getHeight() - rows * tileSize) / 2;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                double drawX = offsetX + x * tileSize;
                double drawY = offsetY + y * tileSize;

                if (!manager.isSeabedRevealed(x, y)) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(drawX, drawY, tileSize, tileSize);
                    continue;
                }

                String imgName = manager.getSeabedElementImage(x, y);
                if (imgName != null && !imgName.isEmpty()) {
                    var img = ImageLoader.getImage(imgName);
                    if (img != null) {
                        gc.drawImage(img, drawX, drawY, tileSize, tileSize);
                    }
                }
            }
        }

        if (manager.hasActiveDrone()) {
            double dronePixelX = offsetX + manager.getDroneX() * tileSize;
            double dronePixelY = offsetY + manager.getDroneY() * tileSize;
            var droneImg = ImageLoader.getImage("drone.png");
            if (droneImg != null) {
                gc.drawImage(droneImg, dronePixelX, dronePixelY, tileSize, tileSize);
            }
            drawInteractionPrompt(gc, manager.getDroneX(), manager.getDroneY(), dronePixelX, dronePixelY, tileSize);
        }
    }

    private void drawInteractionPrompt(GraphicsContext gc, int gridX, int gridY, double px, double py, double tileSize) {
        String currentElement = manager.getSeabedElementImage(gridX, gridY);
        if (currentElement == null || currentElement.isEmpty() || currentElement.equals("monster.png") || currentElement.equals("water.png")) {
            return;
        }
        boolean isInteractable = currentElement.equals("ore.png") || currentElement.equals("artifact.png");
        if (!isInteractable) return;

        String promptText;
        if (manager.isAwaitingConfirmation()) {
            promptText = "Collect? (Y / N)";
            gc.setFill(Color.rgb(231, 76, 60, 0.9));
        } else {
            promptText = "Press [SPACE]";
            gc.setFill(Color.rgb(46, 204, 113, 0.9));
        }
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        double textWidth = 120;
        double textHeight = 25;
        double boxX = px + tileSize / 2 - textWidth / 2;
        double boxY = py - textHeight - 10;
        gc.fillRoundRect(boxX, boxY, textWidth, textHeight, 10, 10);
        gc.setFill(Color.WHITE);
        gc.fillText(promptText, px + tileSize / 2, boxY + textHeight / 2);
    }
}