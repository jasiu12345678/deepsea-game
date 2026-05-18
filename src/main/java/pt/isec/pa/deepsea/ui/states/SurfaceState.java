package pt.isec.pa.deepsea.ui.states;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.ui.utils.ImageLoader;

public class SurfaceState extends Pane {
    private final Manager manager;
    private final Canvas canvas;

    public SurfaceState(Manager manager) {
        this.manager = manager;
        this.canvas = new Canvas(804, 768);
        this.getChildren().add(canvas);

        registerListeners();
        updateVisibility();
    }

    private void registerListeners() {
        manager.addClient("data", evt -> draw());
        manager.addClient("state", evt -> updateVisibility());
    }

    private void updateVisibility() {
        if (manager.getState() == null) return;
        boolean isSurface = manager.getState().name().equals("SURFACE");

        this.setVisible(isSurface);
        if (isSurface) {
            draw();
        }
    }

    private void draw() {
        if (!this.isVisible()) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.rgb(135, 206, 235));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        var waterImg = ImageLoader.getImage("water.png");
        var shipImg = ImageLoader.getImage("ship.png");

        double tileSize = 40.0;

        if (waterImg != null) {
            for (int y = 0; y < canvas.getHeight() / tileSize + 1; y++) {
                for (int x = 0; x < canvas.getWidth() / tileSize + 1; x++) {
                    gc.drawImage(waterImg, x * tileSize, y * tileSize, tileSize, tileSize);
                }
            }
        }

        if (shipImg != null) {
            gc.drawImage(shipImg, manager.getShipX() * tileSize, manager.getShipY() * tileSize, tileSize, tileSize);
        }
    }
}