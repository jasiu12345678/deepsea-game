package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;

import static org.junit.jupiter.api.Assertions.*;

class GameDataTest {

    private GameData data;

    @BeforeEach
    void setUp() {
        Drone.resetIdCounter();
        data = new GameData();
    }

    @Test
    void testShipNavigationConsumesFuel() {
        int initialFuel = data.getShipFuel();
        int initialX = data.getShipX();

        boolean moved = data.moveShip(Directions.LEFT);

        assertTrue(moved, "Ship should be able to move LEFT");
        assertEquals(initialX - 1, data.getShipX(), "Ship X should decrease by one");
        assertEquals(initialFuel - GameConstants.SHIP_FUEL_CONSUMPTION, data.getShipFuel(),
                "Ship should consume the configured fuel amount");
    }

    @Test
    void testSetActiveDrone() {
        assertTrue(data.setActiveDrone(1), "Should be able to activate drone with id 1");
        assertTrue(data.hasActiveDrone(), "There should be an active drone");
        assertEquals("Drone#1", data.getActiveDroneName(), "Active drone name should follow the Drone#id pattern");
    }

    @Test
    void testPrepareDroneForDescent() {
        data.setActiveDrone(1);
        assertTrue(data.prepareDroneForDescent(), "Descent preparation should succeed with an alive drone");
        assertTrue(data.isDroneAtTrenchTop(), "The drone should be placed at the top of the trench");
    }

    @Test
    void testPrepareDescentFailsWithoutDrone() {
        assertFalse(data.prepareDroneForDescent(), "Descent preparation should fail with no active drone");
    }

    @Test
    void testHandleDroneLossRemovesDrone() {
        data.setActiveDrone(1);
        data.handleDroneLoss();
        assertFalse(data.hasActiveDrone(), "The active drone should be gone after a loss");
        assertEquals(GameConstants.DRONE_MAX_QUANTITY - 1, data.getDronesSortedByFuel().size(),
                "One drone should have been removed");
    }

    @Test
    void testLossConditionAfterLosingAllDrones() {
        for (int id = 1; id <= GameConstants.DRONE_MAX_QUANTITY; id++) {
            data.setActiveDrone(id);
            data.handleDroneLoss();
        }
        assertTrue(data.checkLossCondition(), "Losing every drone must trigger the loss condition");
    }

    @Test
    void testLossConditionWhenOutOfFuel() {
        while (data.moveShip(Directions.LEFT) || data.moveShip(Directions.RIGHT)) {
            // drain the ship fuel by moving back and forth
        }
        assertTrue(data.getShipFuel() <= 0, "Ship should be out of fuel");
        assertTrue(data.checkLossCondition(), "No fuel must trigger the loss condition");
    }
}
