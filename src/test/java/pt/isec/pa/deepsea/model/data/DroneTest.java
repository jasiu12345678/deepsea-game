package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.GameConstants;

import static org.junit.jupiter.api.Assertions.*;

class DroneTest {
    private Drone drone;

    @BeforeEach
    void setUp() {
        Drone.resetIdCounter();
        drone = new Drone();
    }

    @Test
    void testInitialState() {
        assertEquals(GameConstants.DRONE_MAX_FUEL, drone.getFuel(), "Drone should start with full fuel");
        assertEquals(GameConstants.DRONE_MAX_HULL, drone.getHP(), "Drone should start with full hull");
        assertTrue(drone.isAlive(), "A fresh drone should be alive");
        assertTrue(drone.getTransportedResources().isEmpty(), "A fresh drone should carry nothing");
    }


    @Test
    void testDamageSequenceIncreases() {
        drone.takeDamage(); // 1
        drone.takeDamage(); // 2
        drone.takeDamage(); // 3
        assertEquals(GameConstants.DRONE_MAX_HULL - (1 + 2 + 3), drone.getHP(),
                "Damage should follow the increasing sequence 1, 2, 3");
    }

    @Test
    void testHullNeverGoesBelowZero() {
        for (int i = 0; i < 50; i++) {
            drone.takeDamage();
        }
        assertEquals(0, drone.getHP(), "Hull should be clamped at 0");
        assertFalse(drone.isAlive(), "A drone with no hull is not alive");
    }

    @Test
    void testRepairIsCappedAtMaxHull() {
        drone.takeDamage();
        drone.repair(1000);
        assertEquals(GameConstants.DRONE_MAX_HULL, drone.getHP(), "Repair must not exceed the maximum hull");
    }

    @Test
    void testRefuelIsCappedAtMaxFuel() {
        drone.consumeFuel(50);
        drone.refuel(1000);
        assertEquals(GameConstants.DRONE_MAX_FUEL, drone.getFuel(), "Refuel must not exceed the maximum fuel");
    }
}
