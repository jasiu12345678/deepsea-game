package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;

import java.io.Serializable;
import java.util.Random;

public class MiniGameData implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int[][] grid;
    private int movesCount;

    public MiniGameData() {
        this.grid = new int[GameConstants.MINIGAME_GRID_SIZE][GameConstants.MINIGAME_GRID_SIZE];
        this.movesCount = 0;
        initializeGrid();
        shuffle(10);
        this.movesCount = 0;
    }

    private void initializeGrid() {
        int gridSize = GameConstants.MINIGAME_GRID_SIZE;
        int maxPieces = (gridSize * gridSize) - 1;
        int number = 1;

        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                if (number <= maxPieces) {
                    grid[y][x] = number;
                    number++;
                } else {
                    grid[y][x] = 0;
                }
            }
        }
    }

    public void shuffle(int moves) {
        Random random = new Random();
        Directions[] directions = Directions.values();
        for (int i = 0; i < moves; i++) {
            int idx = random.nextInt(directions.length);
            move(directions[idx]);
        }
    }

    public boolean move(Directions dir) {
        int currentX = -1;
        int currentY = -1;

        for (int y = 0; y < GameConstants.MINIGAME_GRID_SIZE; y++) {
            for (int x = 0; x < GameConstants.MINIGAME_GRID_SIZE; x++) {
                if (grid[y][x] == 0) {
                    currentX = x;
                    currentY = y;
                }
            }
        }

        int newX = currentX;
        int newY = currentY;

        switch(dir) {
            case UP: newY = currentY - 1; break;
            case DOWN: newY = currentY + 1; break;
            case LEFT: newX = currentX - 1; break;
            case RIGHT: newX = currentX + 1; break;
        }

        if (newX >= 0 && newX < GameConstants.MINIGAME_GRID_SIZE && newY >= 0 && newY < GameConstants.MINIGAME_GRID_SIZE) {
            grid[currentY][currentX] = grid[newY][newX];
            grid[newY][newX] = 0;
            movesCount++;
            return true;
        }
        return false;
    }

    public boolean checkWinCondition() {
        int expected = 1;
        int gridSize = GameConstants.MINIGAME_GRID_SIZE;

        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                if (y == gridSize - 1 && x == gridSize - 1) {
                    return grid[y][x] == 0;
                }
                if (grid[y][x] != expected++) {
                    return false;
                }
            }
        }
        return true;
    }

    int[][] getGrid() { return grid; }
    public int getMovesCount() { return movesCount; }
}