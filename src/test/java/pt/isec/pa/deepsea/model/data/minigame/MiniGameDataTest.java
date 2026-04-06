package pt.isec.pa.deepsea.model.data.minigame;

import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.GameConstants;
import static org.junit.jupiter.api.Assertions.*;

class MiniGameDataTest {

    @Test
    void testMiniGameInitialization() {
        MiniGameData miniGame = new MiniGameData();
        int[][] grid = miniGame.getGrid();

        assertNotNull(grid, "grid cannot be null");
        assertEquals(GameConstants.MINIGAME_GRID_SIZE, grid.length, "incorrent rows");
        assertEquals(GameConstants.MINIGAME_GRID_SIZE, grid[0].length, "incorrect columns");
        assertEquals(0, miniGame.getMovesCount(), "the move counter must be 0 at start");
    }

    @Test
    void testMiniGamePiecesCount() {
        MiniGameData miniGame = new MiniGameData();
        int[][] grid = miniGame.getGrid();
        int emptySpaces = 0;

        for (int y = 0; y < GameConstants.MINIGAME_GRID_SIZE; y++) {
            for (int x = 0; x < GameConstants.MINIGAME_GRID_SIZE; x++) {
                if (grid[y][x] == 0) emptySpaces++;
            }
        }

        assertEquals(1, emptySpaces, "there should be exactly one empty space in the grid");
    }
}