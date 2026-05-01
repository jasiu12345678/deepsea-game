package pt.isec.pa.deepsea.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.GameData;
import pt.isec.pa.deepsea.model.data.actors.Drone;

import static org.junit.jupiter.api.Assertions.*;

class GameDataTest {

    private GameData data;

    @BeforeEach
    void setUp() {
        Drone.resetIdCounter();
        data = new GameData();
    }

    @Test
    void testShipNavigation() {
        int initialFuel = data.getShipFuel();
        int initialX = data.getShipX();

        boolean moved = data.moveShip(Directions.LEFT);

        assertTrue(moved, "Ship should be able to move LEFT.");
        assertEquals(initialX - 1, data.getShipX(), "Ship X should change (X-1).");
        assertEquals(initialFuel - GameConstants.SHIP_FUEL_CONSUMPTION, data.getShipFuel(),
                "Ship should consume FUEL (as defined in GameConstans)");
    }

    @Test
    void testSetActiveDrone() {
        assertTrue(data.setActiveDrone(1), "Should be able to setActiveDrone with id 1");
        assertTrue(data.hasActiveDrone(), "There should be an active drone");
        assertEquals("Drone#1", data.getActiveDroneName(), "Drone name should be Drone#1 (Drone#id)");
    }

    @Test
    void testRepairDrone() {
        data.setActiveDrone(1);
        int maxHp = data.getActiveDroneMaxHP();
        boolean repaired = data.repairDrone(1);
        assertFalse(repaired, "Shouldn't be able to repair drone with full HP!");
    }

    @Test
    void testPrepareDroneForDescent() {
        data.setActiveDrone(1);
        assertTrue(data.prepareDroneForDescent(), "can't prepare for descent.");
        assertTrue(data.isDroneAtTrenchTop(), "Drone should be at trench top side (for descent)!");
    }

    @Test
    void testHandleDroneLoss() {
        data.setActiveDrone(1);
        data.handleDroneLoss();
        assertFalse(data.hasActiveDrone(), "Drone should be null after loss");
        int remainingDrones = data.getDronesSortedByFuel().size();
        assertEquals(2, remainingDrones, "There should be 2 drones left");
    }

    @Test
    void testImmutability() {
        var drones = data.getDronesSortedByFuel();
        assertNotNull(drones);
        assertEquals(3, drones.size(), "There should be 3 drones at start");
        assertThrows(UnsupportedOperationException.class, () -> {
            drones.remove(0);
        }, "List returned should be unmodifiable!!!");
    }

    @Test
    void testLossCondition() {
        data.setActiveDrone(1);
        data.handleDroneLoss();
        data.setActiveDrone(2);
        data.handleDroneLoss();
        data.setActiveDrone(3);
        data.handleDroneLoss();
        assertTrue(data.checkLossCondition(), "Game must be lost after losing all drones");
    }
}