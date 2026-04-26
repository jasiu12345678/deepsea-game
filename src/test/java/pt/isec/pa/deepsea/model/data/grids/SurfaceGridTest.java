package pt.isec.pa.deepsea.model.data.grids;

import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.GameConstants;
import static org.junit.jupiter.api.Assertions.*;

class SurfaceGridTest {

    @Test
    void testSurfaceGridInit() {
        SurfaceGrid surfaceGrid = new SurfaceGrid();
        assertNotNull(surfaceGrid);

        for (int y = 0; y < GameConstants.SURFACE_HEIGHT; y++) {
            for (int x = 0; x < GameConstants.SURFACE_WIDTH; x++) {
                assertNotNull(surfaceGrid.getCell(x, y), "Cell at " + x + "," + y + " should not be null");
            }
        }
    }

    @Test
    void testGetCellOutOfBounds() {
        SurfaceGrid surfaceGrid = new SurfaceGrid();
        assertNotNull(surfaceGrid);

        assertNull(surfaceGrid.getCell(-1, 0), "Should return null for negative X");
        assertNull(surfaceGrid.getCell(0, GameConstants.SURFACE_HEIGHT), "Should return null for Y out of bounds");
    }
}