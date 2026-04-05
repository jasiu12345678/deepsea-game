package pt.isec.pa.deepsea.model.data.actors;

import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.GameConstants;

import static org.junit.jupiter.api.Assertions.*;

class DroneTest {

    @Test
    void testDroneInitialization() {
        Drone drone = new Drone();

        assertEquals(GameConstants.DRONE_MAX_FUEL, drone.getCurrentFuel());
        assertNull(drone.getLocation(), "Drone should have null location (onship)");
        assertNotNull(drone.getTransportedResources(), "Drone resources cannot be null");
        assertTrue(drone.getTransportedResources().isEmpty(), "Drone should start with no transported resources");
    }
    @Test
    void testDroneHullIntegrity() {
        Drone drone = new Drone();
        assertEquals(GameConstants.DRONE_MAX_HULL, drone.getHullIntegrity(), "Drone should initialize with full hull integrity");
    }
}