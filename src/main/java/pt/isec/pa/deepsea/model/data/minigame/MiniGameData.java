package pt.isec.pa.deepsea.model.data.minigame;

import pt.isec.pa.deepsea.model.GameConstants;

// moving puzzle minigame, for now its just a 2d array filled with numbers, but in the future it will be shuffled so
// you will have to move the tiles around to get them in the right order, for example to get a bonus, you will have to
// organise the tiles with limited moves.

public class MiniGameData {
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

    public int[][] getGrid() {
        return grid;
    }

    public int getMovesCount() {
        return movesCount;
    }
}