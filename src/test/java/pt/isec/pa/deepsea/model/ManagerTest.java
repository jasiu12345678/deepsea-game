package pt.isec.pa.deepsea.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.data.DroneInfo;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    private Manager manager;
    private static final String TEST_SAVE_FILE = "test-save.dat";

    @BeforeEach
    void setUp() {
        Drone.resetIdCounter();
        manager = new Manager();
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testInitialState() {
        assertEquals(State.SURFACE, manager.getState(), "Game should start on the surface");
        assertEquals(GameConstants.SHIP_MAX_FUEL, manager.getShipFuel(), "Ship should start with full fuel");
        assertEquals(0, manager.getOreCount(), "Ship should start with no ores");
    }

    @Test
    void testStateTransitions() {
        assertTrue(manager.openWorkshop(), "Should be able to open the workshop on the surface");
        assertEquals(State.WORKSHOP, manager.getState());

        assertTrue(manager.closeWorkshop(), "Should be able to close the workshop");
        assertEquals(State.SURFACE, manager.getState());
    }

    @Test
    void testExposedDroneListIsUnmodifiable() {
        var drones = manager.getDronesSortedByFuel();
        assertEquals(GameConstants.DRONE_MAX_QUANTITY, drones.size(), "All drones should be exposed");
        assertThrows(UnsupportedOperationException.class,
                () -> drones.add(new DroneInfo(99, 100, 100, 10, 10)),
                "The UI must not be able to modify the exposed list");
    }

    @Test
    void testSaveAndLoadKeepsState() {
        manager.openWorkshop();
        manager.selectDrone(1);
        manager.closeWorkshop();
        manager.startDescent();
        assertEquals(State.TRENCH, manager.getState(), "Setup should leave the game in the trench");

        assertTrue(manager.save(TEST_SAVE_FILE), "Saving should succeed");

        Manager other = new Manager();
        assertEquals(State.SURFACE, other.getState(), "A new game starts on the surface");
        assertTrue(other.load(TEST_SAVE_FILE), "Loading should succeed");
        assertEquals(State.TRENCH, other.getState(), "Loaded game should restore the saved state");
    }

    @Test
    void testLoadNonExistentFileReturnsFalse() {
        assertFalse(manager.load("does-not-exist-123.dat"), "Loading a missing file should fail gracefully");
    }

    @Test
    void testClearLogsEmptiesTheLog() {
        manager.openWorkshop();
        assertFalse(manager.getLogs().isEmpty(), "Actions should produce log entries");
        manager.clearLogs();
        assertTrue(manager.getLogs().isEmpty(), "Clearing should remove every log entry");
    }
}
