package pt.isec.pa.deepsea.model.data.grids;

import pt.isec.pa.deepsea.model.GameConstants;

import java.io.Serializable;

public class SurfaceGrid implements Serializable {
    private static final long serialVersionUID = 1L;
    private final SurfaceCell[][] surface;

    // initializing surface grid with surfacecell, which holds the trench and seabed grids
    // so i dont have to make many independent grids.
    public SurfaceGrid() {
        this.surface = new SurfaceCell[GameConstants.SURFACE_HEIGHT][GameConstants.SURFACE_WIDTH];
        for (int y = 0; y < GameConstants.SURFACE_HEIGHT; y++) {
            for (int x = 0; x < GameConstants.SURFACE_WIDTH; x++) {
                boolean isCentral = (x == GameConstants.SURFACE_WIDTH / 2) &&
                        (y == GameConstants.SURFACE_HEIGHT / 2 || y == (GameConstants.SURFACE_HEIGHT / 2) - 1);
                surface[y][x] = new SurfaceCell(isCentral);
            }
        }
    }

    public SurfaceCell getCell(int x, int y) {
        if (isOutOfBounds(x,y)) {
            return null;
        }
        return surface[y][x];
    }

    public int getWidth() {
        return surface[0].length;
    }
    public int getHeight() {
        return surface.length;
    }
    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= getWidth() || y < 0 || y >= getHeight();
    }
}