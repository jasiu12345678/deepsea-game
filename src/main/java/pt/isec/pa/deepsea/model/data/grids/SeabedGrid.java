package pt.isec.pa.deepsea.model.data.grids;

import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.elements.*;
import java.util.Arrays;

public class SeabedGrid {
    private final GridElement[][] grid;
    private final boolean[][] isRevealed;

    public SeabedGrid(boolean isCentralForPresentation) {
        this.grid = new GridElement[GameConstants.SEABED_HEIGHT][GameConstants.SEABED_WIDTH];
        this.isRevealed = new boolean[GameConstants.SEABED_HEIGHT][GameConstants.SEABED_WIDTH];
        generateSeabed(isCentralForPresentation);
    }

    private void generateSeabed(boolean isCentralForPresentation) {
        if (GameConstants.PRESENTATION_MODE) {
            for (boolean[] row : isRevealed) {
                Arrays.fill(row, true);
            }
            grid[0][0] = new Monster();
            grid[0][1] = new Ore(GameConstants.MIN_ORE_VALUE_PRESENTATION);
            grid[0][2] = new Ore(GameConstants.MAX_ORE_VALUE);

            if (isCentralForPresentation) {
                grid[GameConstants.SEABED_HEIGHT - 2][GameConstants.SEABED_WIDTH / 2] = new Artifact();
                grid[GameConstants.SEABED_HEIGHT - 1][GameConstants.SEABED_WIDTH / 2] = new Artifact();
            }
        }
    }

    public void activateZone() {
    }

    public GridElement[][] getGrid() {
        return grid;
    }

    public boolean[][] getRevealed() {
        return isRevealed;
    }
}