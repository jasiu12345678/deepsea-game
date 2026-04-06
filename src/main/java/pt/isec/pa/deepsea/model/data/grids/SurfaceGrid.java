package pt.isec.pa.deepsea.model.data.grids;

import pt.isec.pa.deepsea.model.GameConstants;

public class SurfaceGrid {
    private final SurfaceCell[][] surface;

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
        if (x < 0 || x >= GameConstants.SURFACE_WIDTH || y < 0 || y >= GameConstants.SURFACE_HEIGHT) {
            return null;
        }
        return surface[y][x];
    }
}