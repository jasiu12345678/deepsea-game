package pt.isec.pa.deepsea.ui.states;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.ui.utils.ImageLoader;
import pt.isec.pa.deepsea.model.GameConstants;

public class SeabedState extends Pane {
    private final Manager manager;
    private final Canvas canvas;

    public SeabedState(Manager manager) {
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
        boolean isSeabed = manager.getState().name().equals("SEABED");
        this.setVisible(isSeabed);
        if (isSeabed) {
            draw();
        }
        javafx.application.Platform.runLater(() -> {
            if (this.getScene() != null && this.getScene().getRoot() != null) {
                this.getScene().getRoot().requestFocus();
            }
        });
    }

    private void draw() {
        if (!this.isVisible()) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.rgb(10, 20, 40));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int cols = GameConstants.SEABED_WIDTH;
        int rows = GameConstants.SEABED_HEIGHT;
        if (cols <= 0 || rows <= 0) return;

        double tileWidth = canvas.getWidth() / cols;
        double tileHeight = canvas.getHeight() / rows;
        double tileSize = Math.min(tileWidth, tileHeight);

        double offsetX = (canvas.getWidth() - (cols * tileSize)) / 2;
        double offsetY = (canvas.getHeight() - (rows * tileSize)) / 2;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                double drawX = offsetX + (x * tileSize);
                double drawY = offsetY + (y * tileSize);

                if (!manager.isSeabedRevealed(x, y)) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(drawX, drawY, tileSize, tileSize);
                    continue;
                }

                try {
                    String imgName = manager.getSeabedElementImage(x, y);
                    if (imgName != null && !imgName.isEmpty()) {
                        var img = ImageLoader.getImage(imgName);
                        if (img != null) {
                            gc.drawImage(img, drawX, drawY, tileSize, tileSize);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }

        if (manager.hasActiveDrone()) {
            double dronePixelX = offsetX + (manager.getDroneX() * tileSize);
            double dronePixelY = offsetY + (manager.getDroneY() * tileSize);
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
        if (isInteractable) {
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
            double boxX = px + (tileSize / 2) - (textWidth / 2);
            double boxY = py - textHeight - 10;
            gc.fillRoundRect(boxX, boxY, textWidth, textHeight, 10, 10);
            gc.setFill(Color.WHITE);
            gc.fillText(promptText, px + (tileSize / 2), boxY + (textHeight / 2));
        }
    }
}