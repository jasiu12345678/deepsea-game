package pt.isec.pa.deepsea.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.GameData;
import pt.isec.pa.deepsea.model.data.Drone;

import static org.junit.jupiter.api.Assertions.*;

public class ContextTest {
    private Context context;
    private GameData data;

    @BeforeEach
    void setUp() {
        Drone.resetIdCounter();
        data = new GameData();
        context = new Context(data);
    }

    @Test
    void testInit() {
        assertEquals(State.SURFACE, context.getState(), "game always begins on surface");
    }
    @Test
    void testFromSurfaceToWorkshop() {
        boolean result = context.openWorkshop();
        assertTrue(result);
        assertEquals(State.WORKSHOP, context.getState(), "it is possible to open workshop on surface");
    }
    @Test
    void testLaunchWithoutDrone() {
        boolean result = context.startDescent();
        assertFalse(result, "should return false, because we can't start descent without a drone");
        assertEquals(State.SURFACE, context.getState(), "State should still be surface, because we didnt select drone");
    }
    @Test
    void testLaunchWithDrone() {
        context.openWorkshop();
        assertEquals(State.WORKSHOP, context.getState(), "state should change to workshop");
        boolean test = context.selectDrone(1);
        assertTrue(test, "you should be able to select drone with id 1");
        context.closeWorkshop();
        assertEquals(State.SURFACE, context.getState(), "state should change back to surface");
        test = context.startDescent();
        assertTrue(test, "should be true, since we picked a drone");
        assertEquals(State.TRENCH, context.getState(), "State should change to trench");
    }
    @Test
    void testCollectItemOnSurface() {
        boolean result = context.collectItem();
        assertFalse(result, "should return false, because you can't do that on surface");
    }
    @Test
    void testNavigateInTrench() {
        context.openWorkshop();
        context.selectDrone(1);
        context.closeWorkshop();
        context.startDescent();

        boolean moved = context.navigate(Directions.DOWN);
        assertTrue(moved, "drone should be able to navigate in trench and have a little free space at start");
        assertEquals(State.TRENCH, context.getState(), "Drone should be in trench");
    }

    @Test
    void testWorkshopBlocksIllegalActions() {
        context.openWorkshop();
        assertFalse(context.startDescent(), "can't launch drone in workshop");
        assertFalse(context.collectItem(), "can't collect items in workshop");
        assertFalse(context.navigate(Directions.DOWN), "you shouldnt be able to navigate in workshop");
    }
    @Test
    void testTrenchBlocksIllegalActions() {
        context.openWorkshop();
        context.selectDrone(1);
        context.closeWorkshop();
        context.startDescent();
        assertFalse(context.openWorkshop(), "can't open workshop in trench");
        assertFalse(context.selectDrone(2), "can't select drone out of workshop");
    }
}

