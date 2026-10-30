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
import pt.isec.pa.deepsea.ui.utils.Sprites;

/**
 * Seabed view: a fog-of-war grid explored by the drone to collect ore and artifacts.
 */
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

        int cols = GameConstants.SEABED_WIDTH;
        int rows = GameConstants.SEABED_HEIGHT;
        double tile = Math.min(getWidth() / cols, getHeight() / rows);
        double offsetX = (getWidth() - cols * tile) / 2;
        double offsetY = (getHeight() - rows * tile) / 2;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                double px = offsetX + x * tile;
                double py = offsetY + y * tile;
                if (manager.isSeabedRevealed(x, y)) {
                    Sprites.cell(gc, manager.getSeabedElementType(x, y), px, py, tile);
                } else {
                    Sprites.fog(gc, px, py, tile);
                }
            }
        }

        if (manager.hasActiveDrone()) {
            double px = offsetX + manager.getDroneX() * tile;
            double py = offsetY + manager.getDroneY() * tile;
            Sprites.drone(gc, px, py, tile);
            drawPrompt(gc, px, py, tile);
        }
    }

    private void drawPrompt(GraphicsContext gc, double px, double py, double tile) {
        if (!manager.isCollectibleUnderDrone()) return;

        String text = "Press SPACE";
        double width = 130;
        double height = 28;
        double x = px + tile / 2 - width / 2;
        double y = Math.max(4, py - height - 8);

        gc.setFill(Theme.BG_RAISED);
        gc.fillRoundRect(x, y, width, height, 8, 8);
        gc.setStroke(Theme.ACCENT_DARK);
        gc.setLineWidth(1);
        gc.strokeRoundRect(x, y, width, height, 8, 8);
        gc.setFill(Theme.ACCENT);
        gc.setFont(Theme.font(12, FontWeight.BOLD));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(text, x + width / 2, y + height / 2);
    }
}
