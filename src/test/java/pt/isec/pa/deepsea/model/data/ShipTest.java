package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    private Ship ship;

    @BeforeEach
    void setUp() {
        Drone.resetIdCounter();
        ship = new Ship();
    }

    @Test
    void testInitialState() {
        assertEquals(GameConstants.SHIP_MAX_FUEL, ship.getCurrentFuel(), "Ship should start with full fuel");
        assertEquals(GameConstants.DRONE_MAX_QUANTITY, ship.getAvailableDrones().size(),
                "Ship should start with the configured number of drones");
        assertEquals(0, ship.getOreQuantity(), "Ship should start with no ores");
        assertEquals(0, ship.getArtifactCount(), "Ship should start with no artifacts");
        assertNull(ship.getActiveDrone(), "No drone should be active at the start");
    }

    @Test
    void testMoveConsumesFuel() {
        SurfaceGrid map = new SurfaceGrid();
        int fuelBefore = ship.getCurrentFuel();
        boolean moved = ship.move(Directions.LEFT, map);

        assertTrue(moved, "Ship should be able to move LEFT from the centre");
        assertEquals(fuelBefore - GameConstants.SHIP_FUEL_CONSUMPTION, ship.getCurrentFuel(),
                "Moving should consume ship fuel");
    }

    @Test
    void testMoveBlockedByBounds() {
        SurfaceGrid map = new SurfaceGrid();
        boolean moved = ship.move(Directions.UP, map); // ship starts at y = 0
        assertFalse(moved, "Ship cannot move above the top edge");
    }

    @Test
    void testSetActiveDrone() {
        assertTrue(ship.setActiveDrone(1), "Drone with id 1 should exist");
        assertNotNull(ship.getActiveDrone(), "There should now be an active drone");
        assertEquals(1, ship.getActiveDrone().getId(), "The active drone should be the selected one");
    }

    @Test
    void testRepairFailsOnFullHullDrone() {
        ship.setActiveDrone(1);
        assertFalse(ship.repairDrone(1), "A drone at full hull cannot be repaired");
    }

    @Test
    void testRefuelFailsOnFullFuelDrone() {
        assertFalse(ship.refuelDrone(1), "A drone at full fuel cannot be refuelled");
    }

    @Test
    void testUpgradeRequiresOres() {
        assertFalse(ship.upgradeDrone(1), "Upgrading should fail without enough ores");
        ship.addOre(10);
        assertTrue(ship.upgradeDrone(1), "Upgrading should succeed once enough ores are available");
        assertEquals(0, ship.getOreQuantity(), "Upgrade should consume the ores");
    }

    @Test
    void testRemoveActiveDrone() {
        ship.setActiveDrone(1);
        assertTrue(ship.removeActiveDrone(), "Removing the active drone should succeed");
        assertNull(ship.getActiveDrone(), "There should be no active drone after removal");
        assertEquals(GameConstants.DRONE_MAX_QUANTITY - 1, ship.getAvailableDrones().size(),
                "The drone count should drop by one");
    }

    @Test
    void testAddOreAndArtifact() {
        ship.addOre(3);
        assertEquals(3, ship.getOreQuantity(), "Ores should be added to the ship");
        ship.addArtifact(new Artifact());
        assertEquals(1, ship.getArtifactCount(), "Artifacts should be added to the ship");
    }
}
