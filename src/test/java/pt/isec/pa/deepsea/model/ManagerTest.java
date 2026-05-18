package pt.isec.pa.deepsea.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.DroneInfo;
import pt.isec.pa.deepsea.model.data.Drone;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {
    private Manager manager;
    private final String TEST_SAVE_FILE = "test.dat";

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
    void testInitialization() {
        assertEquals(State.SURFACE.toString(), manager.getCurrentStateName(), "manager should read the state name");
        assertEquals(GameConstants.SHIP_MAX_FUEL, manager.getShipFuel(), "ship should have max fuel at start");
        assertEquals(0, manager.getOreCount(), "ship should have no ores at start");
    }

    @Test
    void testStateTransitions() {
        assertTrue(manager.openWorkshop(), "openworkshop manager");
        assertEquals(State.WORKSHOP.toString(), manager.getCurrentStateName());

        assertTrue(manager.closeWorkshop(), "manager close workshop");
        assertEquals(State.SURFACE.toString(), manager.getCurrentStateName());
    }

    @Test
    void testImmutabilityOfExposedData() {
        var drones = manager.getDronesSortedByFuel();
        assertNotNull(drones, "drones is null");
        assertEquals(3, drones.size(), "should be 3 drones");
        assertThrows(UnsupportedOperationException.class, () -> {
            drones.add(new DroneInfo(99, 100, 100, 10, 10));
        }, "UI should't be able to modify the drones list!");
    }

    @Test
    void testSaveAndLoadConsistency() {
        manager.openWorkshop();
        manager.selectDrone(1);
        manager.closeWorkshop();
        manager.startDescent();
        assertEquals(State.TRENCH.toString(), manager.getCurrentStateName());
        assertTrue(manager.save(TEST_SAVE_FILE), "can't save file");
        Manager newManager = new Manager();
        assertEquals(State.SURFACE.toString(), newManager.getCurrentStateName(), "new game should start at surface");
        assertTrue(newManager.load(TEST_SAVE_FILE), "can't load file");

        assertEquals(State.TRENCH.toString(), newManager.getCurrentStateName(),
                "State should be in trench. Game saving doesn't work");
    }

    @Test
    void testLoadNonExistentFile() {
        assertFalse(manager.load("non-existing-file-123.dat"), "should return false since the file doesn't exist");
    }
}