package pt.isec.pa.deepsea.model.data.actors;

import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.GameConstants;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    void testDronesInitialization() {
        Ship ship = new Ship();

        for (Drone drone : ship.getAvailableDrones()) {
            assertEquals(GameConstants.DRONE_MAX_FUEL, drone.getCurrentFuel(), "Each drone should have max fuel");
            assertEquals(GameConstants.DRONE_MAX_HULL, drone.getHP(), "Each drone should have max hull");
            assertEquals(0, drone.getX(), "Drone should start at ship (x=0)");
            assertEquals(0, drone.getY(), "Drone should start at ship (y=0)");
            assertTrue(drone.getTransportedResources().isEmpty(), "Drone should have no resources");
        }
    }

}
