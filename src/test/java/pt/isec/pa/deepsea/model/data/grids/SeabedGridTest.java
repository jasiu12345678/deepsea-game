package pt.isec.pa.deepsea.model.data.grids;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.elements.Monster;
import pt.isec.pa.deepsea.model.data.elements.Ore;
import static org.junit.jupiter.api.Assertions.*;

class SeabedGridTest {

    @BeforeEach
    void setUp() {
        GameConstants.PRESENTATION_MODE = false;
    }

    @AfterEach
    void tearDown() {
        GameConstants.PRESENTATION_MODE = false;
    }

    @Test
    void testSeabedInitialVisibilityInNormalMode() {
        SeabedGrid seabed = new SeabedGrid(false);
        boolean[][] revealed = seabed.getRevealed();

        for (int y = 0; y < GameConstants.SEABED_HEIGHT; y++) {
            for (int x = 0; x < GameConstants.SEABED_WIDTH; x++) {
                assertFalse(revealed[y][x], "Cell at " + x + "," + y + " should be hidden in normal mode");
            }
        }
    }

    @Test
    void testSeabedPresentationModeSetup() {
        GameConstants.PRESENTATION_MODE = true;
        SeabedGrid seabed = new SeabedGrid(true);
        var grid = seabed.getGrid();
        var revealed = seabed.getRevealed();

        for (int y = 0; y < GameConstants.SEABED_HEIGHT; y++) {
            for (int x = 0; x < GameConstants.SEABED_WIDTH; x++) {
                assertTrue(revealed[y][x], "Cell at " + x + "," + y + " should be revealed in presentation mode");
            }
        }

        assertTrue(grid[0][0] instanceof Monster, "Monster should be at 0,0 in presentation mode");
        assertTrue(grid[0][1] instanceof Ore, "Ore should be at 0,1 in presentation mode");
    }
}