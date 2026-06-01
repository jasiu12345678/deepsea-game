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

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.rgb(12, 20, 34));
        gc.fillRect(0, 0, getWidth(), getHeight());

        int[][] puzzle = manager.getPuzzleGrid();
        if (puzzle == null) return;

        int gridSize = GameConstants.MINIGAME_GRID_SIZE;

        double boardSize = Math.min(getWidth(), getHeight()) * 0.55;
        double tileSize = boardSize / gridSize;
        double offsetX = (getWidth() - boardSize) / 2;
        double offsetY = (getHeight() - boardSize) / 2;

        int movesLeft = GameConstants.MAX_PUZZLE_MOVES - manager.getPuzzleMovesCount();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        if (movesLeft <= 5) {
            gc.setFill(Color.rgb(231, 76, 60));
        } else {
            gc.setFill(Color.rgb(46, 204, 113));
        }
        gc.fillText("REMAINING MOVES: " + Math.max(0, movesLeft), getWidth() / 2, offsetY - 50);

        gc.setFill(Color.rgb(22, 34, 56));
        gc.fillRoundRect(offsetX - 15, offsetY - 15, boardSize + 30, boardSize + 30, 20, 20);
        gc.setStroke(Color.rgb(41, 128, 185));
        gc.setLineWidth(3);
        gc.strokeRoundRect(offsetX - 15, offsetY - 15, boardSize + 30, boardSize + 30, 20, 20);

        int emptyX = -1, emptyY = -1;
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                if (puzzle[y][x] == 0) {
                    emptyX = x;
                    emptyY = y;
                    break;
                }
            }
        }

        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                int value = puzzle[y][x];
                double tileX = offsetX + x * tileSize;
                double tileY = offsetY + y * tileSize;

                if (value == 0) {
                    gc.setFill(Color.rgb(16, 26, 43));
                    gc.fillRoundRect(tileX + 4, tileY + 4, tileSize - 8, tileSize - 8, 10, 10);
                    continue;
                }

                boolean isMovable = (Math.abs(x - emptyX) == 1 && y == emptyY) || (Math.abs(y - emptyY) == 1 && x == emptyX);

                if (isMovable) {
                    gc.setFill(Color.rgb(52, 152, 219));
                    gc.setStroke(Color.rgb(46, 204, 113));
                    gc.setLineWidth(2.5);
                } else {
                    gc.setFill(Color.rgb(41, 128, 185));
                    gc.setStroke(Color.rgb(31, 97, 141));
                    gc.setLineWidth(1);
                }

                gc.fillRoundRect(tileX + 4, tileY + 4, tileSize - 8, tileSize - 8, 12, 12);
                gc.strokeRoundRect(tileX + 4, tileY + 4, tileSize - 8, tileSize - 8, 12, 12);

                gc.setFont(Font.font("Arial", FontWeight.BOLD, gridSize == 3 ? 42 : 34));
                gc.setFill(Color.WHITE);
                gc.fillText(String.valueOf(value), tileX + tileSize / 2, tileY + tileSize / 2);
            }
        }

        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        gc.setFill(Color.rgb(140, 159, 191));
        gc.fillText("Use [ARROWS] or [WSAD] to slide highlighted tiles into the empty gap.", getWidth() / 2, offsetY + boardSize + 45);
    }
}