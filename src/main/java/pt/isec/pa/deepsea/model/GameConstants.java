package pt.isec.pa.deepsea.model;

public final class GameConstants {

    private GameConstants() {
        throw new UnsupportedOperationException("This is a utility class");
    }

    public static boolean PRESENTATION_MODE = false;

    public static final int SHIP_MAX_FUEL = 100;

    public static final int DRONE_MAX_FUEL = 30;
    public static final int DRONE_MAX_QUANTITY = 3;
    public static final int DRONE_MAX_HULL = 100;

    public static final int SURFACE_WIDTH = 30;
    public static final int SURFACE_HEIGHT = 15;

    public static final int TRENCH_WIDTH = 10;
    public static final int TRENCH_HEIGHT = 15;

    public static final int SEABED_WIDTH = 10;
    public static final int SEABED_HEIGHT = 10;

    public static final double MAX_ROCKS_PERCENTAGE = 0.50;
    public static int calculateMaxRocksPerLine(int gridWidth) {
        return (int) (gridWidth * MAX_ROCKS_PERCENTAGE);
    }

    public static final int MAX_ORE_VALUE = 50;

    public static final int MIN_ORE_VALUE_PRESENTATION = 1;

    public static final int MINIGAME_GRID_SIZE = 4;
    public static final int MINIGAME_PIECES = 15;
}