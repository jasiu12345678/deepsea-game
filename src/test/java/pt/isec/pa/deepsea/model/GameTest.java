package pt.isec.pa.deepsea.model;

import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.GameData;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void getDronesSortedByFuel() {
        GameData game = new GameData();
        var dronesByFuel = game.getDronesSortedByFuel();
        assertNotNull(dronesByFuel);
        assertEquals(3, dronesByFuel.size(), "There should be 3 drones.");
        assertTrue(dronesByFuel.get(0).getCurrentFuel() >= dronesByFuel.get(1).getCurrentFuel(),
                "Drones are supposed to be sorted by fuel in descending order.");
    }

    @Test
    void getDronesSortedByHullIntegrity() {
        GameData game = new GameData();
        var dronesByHull = game.getDronesSortedByHullIntegrity();
        assertNotNull(dronesByHull);
        assertEquals(3, dronesByHull.size(), "There should be 3 drones.");
        assertTrue(dronesByHull.get(0).getHullIntegrity() >= dronesByHull.get(1).getHullIntegrity(),
                "Drones are supposed to be sorted by hull integrity in descending order.");
    }
}