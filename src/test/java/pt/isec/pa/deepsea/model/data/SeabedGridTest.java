package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.GameConstants;

import static org.junit.jupiter.api.Assertions.*;

class SeabedGridTest {

    @AfterEach
    void tearDown() {
        GameConstants.PRESENTATION_MODE = false;
    }

    @Test
    void testDimensions() {
        SeabedGrid grid = new SeabedGrid(false);
        assertEquals(GameConstants.SEABED_WIDTH, grid.getWidth(), "Width should match the configured value");
        assertEquals(GameConstants.SEABED_HEIGHT, grid.getHeight(), "Height should match the configured value");
    }

    @Test
    void testOutOfBoundsReturnsWater() {
        SeabedGrid grid = new SeabedGrid(false);
        assertEquals("water.png", grid.getElementImage(-1, 0), "Out of bounds should be treated as water");
        assertEquals("water.png", grid.getElementImage(0, GameConstants.SEABED_HEIGHT), "Out of bounds should be treated as water");
    }

    @Test
    void testPresentationLayoutHasMonsterAtOrigin() {
        GameConstants.PRESENTATION_MODE = true;
        SeabedGrid grid = new SeabedGrid(false);

        assertEquals("monster.png", grid.getElementImage(0, 0), "Presentation layout places a monster at the origin");
        assertTrue(grid.isRevealed(0, 0), "Presentation layout reveals every cell");
    }

    @Test
    void testMonsterInteractionDamagesDrone() {
        GameConstants.PRESENTATION_MODE = true;
        SeabedGrid grid = new SeabedGrid(false);
        Drone drone = new Drone();
        int hpBefore = drone.getHP();

        boolean blocks = grid.processInteraction(0, 0, drone);

        assertTrue(blocks, "Stepping onto a monster should block movement");
        assertTrue(drone.getHP() < hpBefore, "A monster should damage the drone");
    }
}
