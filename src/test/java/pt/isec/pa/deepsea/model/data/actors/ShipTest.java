package pt.isec.pa.deepsea.model.data.actors;

import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.GameConstants;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    void testShipInitialization() {
        Ship ship = new Ship();
        assertNotNull(ship);
        assertEquals(GameConstants.SHIP_MAX_FUEL, ship.getCurrentFuel(), "Ship should start with max fuel");
        assertEquals(0, ship.getOreQuantity(), "Ship should start with 0 ores");
        assertNotNull(ship.getActiveDrones(), "Drones should not be null");
        assertEquals(GameConstants.DRONE_MAX_QUANTITY, ship.getActiveDrones().size(), "Should have 3 drones");
        assertTrue(ship.getCollectedArtifacts().isEmpty(), "Should start with no artifacts");
        assertTrue(ship.getCollectedArtifacts().isEmpty(), "Ship should start with no artifacts");
        assertNotNull(ship.getCollectedArtifacts(), "Artifacts list should not be null");
    }

    @Test
    void testDronesInitialization() {
        Ship ship = new Ship();

        for (Drone drone : ship.getActiveDrones()) {
            assertEquals(GameConstants.DRONE_MAX_FUEL, drone.getCurrentFuel(), "Each drone should have max fuel");
            assertEquals(GameConstants.DRONE_MAX_HULL, drone.getHullIntegrity(), "Each drone should have max hull");
            assertEquals(0, drone.getX(), "Drone should start at ship (x=0)");
            assertEquals(0, drone.getY(), "Drone should start at ship (y=0)");
            assertTrue(drone.getTransportedResources().isEmpty(), "Drone should have no resources");
        }
    }

}