package pt.isec.pa.deepsea.model.data.grids;

import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.elements.*;
import java.util.Random;

public class TrenchGrid {
    private final GridElement[][] grid;

    public TrenchGrid() {
        this.grid = new GridElement[GameConstants.TRENCH_HEIGHT][GameConstants.TRENCH_WIDTH];
        generateRocks();
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


    public void activateZone() {
        if (GameConstants.PRESENTATION_MODE) {
            grid[GameConstants.TRENCH_HEIGHT / 2][GameConstants.TRENCH_WIDTH / 2] = new MarineAnimal();
            grid[(GameConstants.TRENCH_HEIGHT / 2) + 1][GameConstants.TRENCH_WIDTH / 2] = new OceanCurrent();
        }
    }

    public GridElement[][] getGrid() {
        return grid;
    }
}