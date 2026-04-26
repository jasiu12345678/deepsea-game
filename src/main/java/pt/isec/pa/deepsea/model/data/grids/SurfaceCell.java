package pt.isec.pa.deepsea.model.data.grids;

public class SurfaceCell {
    private final TrenchGrid trench;
    private final SeabedGrid seabed;

    public SurfaceCell(boolean isCentralForPresentation) {
        this.trench = new TrenchGrid();
        this.seabed = new SeabedGrid(isCentralForPresentation);
    }

}