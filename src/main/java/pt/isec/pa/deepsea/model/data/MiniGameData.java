package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;

import java.io.Serializable;
import java.util.Random;

// moving puzzle minigame, for now its just a 2d array filled with numbers, but in the future it will be shuffled so
// you will have to move the tiles around to get them in the right order, for example to get a bonus, you will have to
// organise the tiles with limited moves.

public class MiniGameData implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int[][] grid;
    private int movesCount;

    public MiniGameData() {
        this.grid = new int[GameConstants.MINIGAME_GRID_SIZE][GameConstants.MINIGAME_GRID_SIZE];
        this.movesCount = 0;
        initializeGrid();
    }

    private void initializeGrid() {
        int number = 1;
        for (int y = 0; y < GameConstants.MINIGAME_GRID_SIZE; y++) {
            for (int x = 0; x < GameConstants.MINIGAME_GRID_SIZE; x++) {
                if (number <= GameConstants.MINIGAME_PIECES) {
                    grid[y][x] = number;
                    number++;
                } else {
                    grid[y][x] = 0;
                }
            }
        }
    }

    public void shuffle(int moves) {
        for (int i=0;i<moves;i++) {
            Random random = new Random();
            Directions[] directions = Directions.values();
            int idx = random.nextInt(directions.length);
            Directions randomDirection = directions[idx];
            move(randomDirection);
        }
    }

    public void move(Directions dir) {
        int currentX=0;
        int currentY=0;
        for (int x=0;x<GameConstants.MINIGAME_GRID_SIZE;x++) {
            for (int y=0;y<GameConstants.MINIGAME_GRID_SIZE;y++) {
                if (grid[x][y] == 0) {
                    currentX = x;
                    currentY = y;
                }
            }
        }
        switch(dir) {
            case UP: {
                grid[currentX][currentY] = grid[currentX][currentY - 1];
                grid[currentX][currentY - 1] = 0;
                movesCount++;
                break;
            }
            case DOWN: {
                grid[currentX][currentY] = grid[currentX][currentY + 1];
                grid[currentX][currentY + 1] = 0;
                movesCount++;
                break;
            }
            case LEFT: {
                grid[currentX][currentY] = grid[currentX - 1][currentY];
                grid[currentX - 1][currentY] = 0;
                movesCount++;
                break;
            }
            case RIGHT: {
                grid[currentX][currentY] = grid[currentX + 1][currentY];
                grid[currentX + 1][currentY] = 0;
                movesCount++;
                break;
            }
        }
    }

    public boolean checkWinCondtion() {
        int currentX=0;
        int currentY=0;
        for (int x=0;x<GameConstants.MINIGAME_GRID_SIZE;x++) {
            for (int y=0;y<GameConstants.MINIGAME_GRID_SIZE;y++) {
                if (grid[x][y] == 0) {
                    currentX = x;
                    currentY = y;
                }
            }
        }
        if (currentX == GameConstants.MINIGAME_GRID_SIZE && currentY == GameConstants.MINIGAME_GRID_SIZE) {
            return true;
        } return false;
    }

    public int getMovesCount() {
        return movesCount;
    }
}