package pt.isec.pa.deepsea.model.data.actors;

import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.GameConstants;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    void testShipInit() {
        Ship ship = new Ship();
        assertNotNull(ship);
        assertEquals(GameConstants.SHIP_MAX_FUEL, ship.getCurrentFuel(), "The ship should start with max fuel.");
        assertEquals(0, ship.getOreQuantity(), "The ship should start with 0 ores.");
        assertNotNull(ship.getActiveDrones(), "The ship should start with 3 active drones.");
        assertEquals(GameConstants.STARTING_DRONES, ship.getActiveDrones().size(),
                "The ship should start with 3 active drones.");
        assertNotNull(ship.getCollectedArtifacts(), "List of artifacts should be initialized.");
        assertTrue(ship.getCollectedArtifacts().isEmpty(), "List of artifacts should not be empty at start.");
        System.out.println(ship.getActiveDrones().toString());
    }

    @Test
    void testShipArtifactsList() {
        Ship ship = new Ship();
        assertTrue(ship.getCollectedArtifacts().isEmpty(), "Ship should start with no artifacts");
        assertNotNull(ship.getCollectedArtifacts(), "Artifacts list should not be null");
    }
}
