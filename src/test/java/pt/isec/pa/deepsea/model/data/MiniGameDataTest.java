package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MiniGameDataTest {

    private MiniGameData minigame;

    @BeforeEach
    void setUp() {
        minigame = new MiniGameData();
    }

    @Test
    void testMovesCountStartsAtZero() {
        assertEquals(0, minigame.getMovesCount(), "Move count should be reset after shuffling");
    }

}
