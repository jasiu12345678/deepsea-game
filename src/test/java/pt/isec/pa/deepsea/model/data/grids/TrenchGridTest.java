package pt.isec.pa.deepsea.model.data.grids;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.Rock;
import pt.isec.pa.deepsea.model.data.TrenchGrid;

import static org.junit.jupiter.api.Assertions.*;

class TrenchGridTest {

    @BeforeEach
    void setUp() {
        GameConstants.PRESENTATION_MODE = false;
    }

    @AfterEach
    void tearDown() {
        GameConstants.PRESENTATION_MODE = false;
    }

    @Test
    void testRocksNeverExceedFiftyPercentOfWidth() {
        TrenchGrid trench = new TrenchGrid();
        int maxAllowedRocks = (int) (GameConstants.TRENCH_WIDTH * GameConstants.MAX_ROCKS_PERCENTAGE);

        for (int y = 0; y < GameConstants.TRENCH_HEIGHT; y++) {
            int rockCount = 0;
            for (int x = 0; x < GameConstants.TRENCH_WIDTH; x++) {
                if (trench.getElement(x, y) instanceof Rock) {
                    rockCount++;
                }
            }
            assertTrue(rockCount <= maxAllowedRocks, "Rocks exceed 50% limit in row " + y);
        }
    }

    @Test
    void testPresentationModeRockGeneration() {
        GameConstants.PRESENTATION_MODE = true;
        TrenchGrid trench = new TrenchGrid();

        for (int y = 0; y < GameConstants.TRENCH_HEIGHT; y++) {
            assertTrue(trench.getElement(0, y) instanceof Rock, "Missing left rock in row " + y);
            assertTrue(trench.getElement(GameConstants.TRENCH_WIDTH - 1, y) instanceof Rock, "Missing right rock in row " + y);
            assertNull(trench.getElement(1, y), "Unexpected rock at column 1");
        }
    }
}