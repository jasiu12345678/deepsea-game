package pt.isec.pa.deepsea.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testGameCorrectInitDroneCount() {
        Game game = new Game();
        int droneCount = game.getShip().getActiveDrones().size();
        assertEquals(3, droneCount, "The game should initialize with 3 active drones");
    }

    @Test
    void getDronesSortedByFuel() {
        Game game = new Game();
        var dronesByFuel = game.getDronesSortedByFuel();
        assertNotNull(dronesByFuel);
        assertEquals(3, dronesByFuel.size(), "There should be 3 drones.");
        assertTrue(dronesByFuel.get(0).getCurrentFuel() >= dronesByFuel.get(1).getCurrentFuel(),
                "Drones are supposed to be sorted by fuel in descending order.");
    }

    @Test
    void getDronesSortedByHullIntegrity() {
        Game game = new Game();
        var dronesByHull = game.getDronesSortedByHullIntegrity();
        assertNotNull(dronesByHull);
        assertEquals(3, dronesByHull.size(), "There should be 3 drones.");
        assertTrue(dronesByHull.get(0).getHullIntegrity() >= dronesByHull.get(1).getHullIntegrity(),
                "Drones are supposed to be sorted by hull integrity in descending order.");
    }
}