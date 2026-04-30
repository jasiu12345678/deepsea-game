package pt.isec.pa.deepsea.model.data.grids;

import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.elements.*;
import java.io.Serializable;
import java.util.Random;

public class TrenchGrid implements Serializable, IGrid {
    private static final long serialVersionUID = 1L;
    private final GridElement[][] grid;

    public TrenchGrid() {
        this.grid = new GridElement[GameConstants.TRENCH_HEIGHT][GameConstants.TRENCH_WIDTH];
        generateRocks();
        populate();
    }

    private void generateRocks() {
        if (GameConstants.PRESENTATION_MODE) {
            for (int y = 0; y < GameConstants.TRENCH_HEIGHT; y++) {
                grid[y][0] = new Rock();
                grid[y][GameConstants.TRENCH_WIDTH - 1] = new Rock();
            }
            return;
        }

        Random rand = new Random();
        int maxRocksTotal = (int) (GameConstants.TRENCH_WIDTH * GameConstants.MAX_ROCKS_PERCENTAGE);
        int prevLeft = rand.nextInt(maxRocksTotal / 2 + 1);
        int prevRight = rand.nextInt(maxRocksTotal / 2 + 1);

        for (int y = 0; y < GameConstants.TRENCH_HEIGHT; y++) {
            int currentLeft = Math.max(0, prevLeft + rand.nextInt(3) - 1);
            int currentRight = Math.max(0, prevRight + rand.nextInt(3) - 1);

            while (currentLeft + currentRight > maxRocksTotal) {
                if (currentLeft > 0) currentLeft--;
                if (currentLeft + currentRight <= maxRocksTotal) break;
                if (currentRight > 0) currentRight--;
            }

            for (int x = 0; x < currentLeft; x++) grid[y][x] = new Rock();
            for (int x = GameConstants.TRENCH_WIDTH - currentRight; x < GameConstants.TRENCH_WIDTH; x++) grid[y][x] = new Rock();

            prevLeft = currentLeft;
            prevRight = currentRight;
        }
    }

    private void populate() {
        Random rand = new Random();

        for (int y = 0; y < GameConstants.TRENCH_HEIGHT; y++) {
            for (int x = 0; x < GameConstants.TRENCH_WIDTH; x++) {

                if (grid[y][x] != null) continue;
                if (y == 0 && x == GameConstants.TRENCH_WIDTH/2 ||
                        y == 0 && x == GameConstants.TRENCH_WIDTH/2-1 ||
                        y == 0 && x == GameConstants.TRENCH_WIDTH/2 +1) continue;

                double roll = rand.nextDouble();

                if (roll < 0.08) {
                    grid[y][x] = new OceanCurrent();
                }
                else if (roll < 0.16) {
                    grid[y][x] = new MarineAnimal();
                }
            }
        }
    }

    @Override
    public GridElement getElement(int x, int y) {
        return grid[y][x];
    }

    @Override
    public int getWidth() {
        return grid[0].length;
    }
    @Override
    public int getHeight() {
        return grid.length;
    }


}