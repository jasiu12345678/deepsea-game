package pt.isec.pa.deepsea.ui.states;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.ui.utils.ImageLoader;
import pt.isec.pa.deepsea.model.GameConstants;

public class TrenchState extends Pane {
    private final Manager manager;
    private final Canvas canvas;

    public TrenchState(Manager manager) {
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
        String stateName = manager.getState().name();

        boolean isTrench = stateName.equals("TRENCH");
        this.setVisible(isTrench);
        if (isTrench) {
            draw();
        }
    }

    private void draw() {
        if (!this.isVisible()) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.rgb(20, 50, 80));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int cols = GameConstants.TRENCH_WIDTH;
        int rows = GameConstants.TRENCH_HEIGHT;

        if (cols <= 0 || rows <= 0) return;

        double tileWidth = canvas.getWidth() / cols;
        double tileHeight = canvas.getHeight() / rows;
        double tileSize = Math.min(tileWidth, tileHeight);

        double offsetX = (canvas.getWidth() - (cols * tileSize)) / 2;
        double offsetY = (canvas.getHeight() - (rows * tileSize)) / 2;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                try {
                    String imgName = manager.getTrenchElementImage(x, y);
                    if (imgName != null && !imgName.isEmpty()) {
                        var img = ImageLoader.getImage(imgName);
                        if (img != null) {
                            gc.drawImage(img, offsetX + (x * tileSize), offsetY + (y * tileSize), tileSize, tileSize);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        if (manager.hasActiveDrone()) {
            var droneImg = ImageLoader.getImage("drone.png");
            if (droneImg != null) {
                gc.drawImage(droneImg, offsetX + (manager.getDroneX() * tileSize), offsetY + (manager.getDroneY() * tileSize), tileSize, tileSize);
            }
        }
    }
}