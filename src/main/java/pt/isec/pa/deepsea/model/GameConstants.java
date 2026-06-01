package pt.isec.pa.deepsea.model;

public final class GameConstants {

    private GameConstants() {
        throw new UnsupportedOperationException("This is a utility class");
    }

    public static boolean PRESENTATION_MODE = false;

    public static final int SHIP_MAX_FUEL = 1000;
    public static final int SHIP_FUEL_CONSUMPTION = 2;

    public static final int DRONE_MAX_FUEL = 300;
    public static final int DRONE_MAX_QUANTITY = 3;
    public static final int DRONE_START_HULL = 100;
    public static final int DRONE_MAX_HULL = 500;
    public static final int DRONE_FUEL_CONSUMPTION = 1;
    public static final String LOG_FILENAME = "logs.txt";

    public static final int SURFACE_WIDTH = 30;
    public static final int SURFACE_HEIGHT = 15;

    public static final int TRENCH_WIDTH = 10;
    public static final int TRENCH_HEIGHT = 15;

    public static final int SEABED_WIDTH = 10;
    public static final int SEABED_HEIGHT = 10;

    public static final double MAX_ROCKS_PERCENTAGE = 0.50;

    public static final int MAX_ORE_VALUE = 50;
    public static final int TOTAL_ARTIFACTS = 5;

    public static final int MIN_ORE_VALUE_PRESENTATION = 1;

    public static final int MINIGAME_GRID_SIZE = 3;
    public static final int MAX_PUZZLE_MOVES = 100;
}