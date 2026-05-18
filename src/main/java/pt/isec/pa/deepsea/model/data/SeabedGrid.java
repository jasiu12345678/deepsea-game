package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.GameConstants;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class SeabedGrid implements Serializable, IGrid {
    private static final long serialVersionUID = 1L;
    private final GridElement[][] grid;

    public SeabedGrid(boolean isCentralForPresentation) {
        this.grid = new GridElement[GameConstants.SEABED_HEIGHT][GameConstants.SEABED_WIDTH];
        fillWithWater();
        if (GameConstants.PRESENTATION_MODE) {
            generateSeabed(isCentralForPresentation);
        } else {
            populate();
        }
    }

    private void fillWithWater() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                grid[y][x] = new Water();
            }
        }
    }

    @Override
    public boolean processInteraction(int x, int y, Drone drone) {
        if (isOutOfBounds(x, y)) {
            return true;
        }
        GridElement element = grid[y][x];
        if (element != null) {
            element.reveal();
            return element.interact(drone);
        }
        return false;
    }

    @Override
    public Collectible processCollection(int x, int y, Drone drone) {
        if (isOutOfBounds(x, y)) return null;
        GridElement element = grid[y][x];
        if (element != null) {
            return element.collect(drone, this);
        }
        return null;
    }

    @Override
    public void removeElement(int x,int y) {
        if (!isOutOfBounds(x,y)) {
            grid[y][x] = new Water();
            grid[y][x].reveal();
        }
    }

    private void populate() {
        Random rand = new Random();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (!grid[y][x].isEmpty()) continue;
                double roll = rand.nextDouble();
                if (roll < 0.05) {
                    grid[y][x] = new Monster();
                } else if (roll < 0.15) {
                    int randomQuantity = rand.nextInt(GameConstants.MAX_ORE_VALUE) + 1;
                    grid[y][x] = new Ore(randomQuantity);
                } else if (roll < 0.20) {
                    grid[y][x] = new Artifact();
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

        // kind of an reveal all function but its only used once so i implement it in here
        for (int y=0;y<GameConstants.SEABED_HEIGHT; y++) {
            for (int x=0;x<GameConstants.SEABED_WIDTH;x++) {
                grid[y][x].reveal();
            }
        }
    }

    public void activateZone() {
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
                if (grid[ranY][ranX].isEmpty()) {
                    grid[ranY][ranX] = item;
                    grid[ranY][ranX].unreaveal();
                    placed = true;
                }
            }
        }
    }

    public boolean isRevealed(int x, int y) {
        if (isOutOfBounds(x, y)) return false;
        GridElement element = grid[y][x];
        return element != null && element.isRevealed();
    }

    @Override
    public String getElementImage(int x, int y) {
        if (isOutOfBounds(x, y)) return "water.png";
        GridElement element = grid[y][x];
        if (element == null) return "water.png";
        return element.getImageName();
    }

}