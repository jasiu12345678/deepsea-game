package pt.isec.pa.deepsea.model.data.grids;

import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.elements.*;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class SeabedGrid implements Serializable, IGrid {
    private static final long serialVersionUID = 1L;
    private final GridElement[][] grid;
    private final boolean[] revealed;

    public SeabedGrid(boolean isCentralForPresentation) {
        this.grid = new GridElement[GameConstants.SEABED_HEIGHT][GameConstants.SEABED_WIDTH];
        this.revealed = new boolean[GameConstants.SEABED_HEIGHT * GameConstants.SEABED_WIDTH];
        if (GameConstants.PRESENTATION_MODE) {
            generateSeabed(isCentralForPresentation);
        } else {
            generateArtifact();
            populate();
        }
    }

    public void revealAll() {
        for (int i = 0; i < revealed.length; i++) {
            revealed[i] = true;
        }
    }

    @Override
    public GridElement getElement(int x, int y) {
        if (isOutOfBounds(x,y)) {
            return null;
        }
        return grid[y][x];
    }

    public boolean hasArtifact() {
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                GridElement element = this.getElement(x, y);
                if (element != null && element.getType() == ElementType.ARTIFACT) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean revealAt(int x, int y) {
        if (isOutOfBounds(x, y)) return false;
        revealed[y * getWidth() + x] = true;
        return true;
    }

    public boolean isRevealed(int x, int y) {
        if (isOutOfBounds(x, y)) return false;
        return revealed[y * getWidth() + x];
    }

    private void generateArtifact() {
        Random rand = new Random();
        int x = rand.nextInt(getWidth());
        int y = rand.nextInt(getHeight());
        grid[y][x] = new Artifact();
    }

    private void populate() {
        Random rand = new Random();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (grid[y][x] != null) continue;
                double roll = rand.nextDouble();
                if (roll < 0.10) {
                    grid[y][x] = new Monster();
                } else if (roll < 0.25) {
                    grid[y][x] = new Ore(25);
                }
            }
        }
    }

    private void generateSeabed(boolean isCentralForPresentation) {
        grid[0][0] = new Monster();
        grid[1][0] = new Ore(GameConstants.MIN_ORE_VALUE_PRESENTATION);
        grid[2][0] = new Ore(GameConstants.MAX_ORE_VALUE);

        if (isCentralForPresentation) {
            grid[GameConstants.SEABED_HEIGHT - 2][GameConstants.SEABED_WIDTH / 2] = new Artifact();
            grid[GameConstants.SEABED_HEIGHT - 1][GameConstants.SEABED_WIDTH / 2] = new Artifact();
        }
        revealAll();
    }

    public void activateZone() {
    }

    public Collectible getCollectibleById(int id) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                GridElement element = grid[y][x];
                if (element != null && element.getId() == id && element instanceof Collectible collectible)
                    return collectible;
            }
        }
        return null;
    }

    public boolean removeElementAt(int x, int y) {
        if (!isOutOfBounds(x,y)) {
            grid[y][x] = null;
            return true;
        }
        return false;
    }

    @Override
    public int getWidth() {
        return grid[0].length;
    }
    @Override
    public int getHeight() {
        return grid.length;
    }

    public void dropLoot(List<Collectible> lostLoot) {
        if (lostLoot == null || lostLoot.isEmpty()) return;

        java.util.Random rand = new java.util.Random();

        for (Collectible item : lostLoot) {
            boolean placed = false;

            while (!placed) {
                int ranX = rand.nextInt(getWidth());
                int ranY = rand.nextInt(getHeight());

                if (grid[ranY][ranX] == null) {
                    grid[ranY][ranX] = item;
                    grid[ranY][ranX].unreaveal();
                    placed = true;
                }
            }
        }
    }
}