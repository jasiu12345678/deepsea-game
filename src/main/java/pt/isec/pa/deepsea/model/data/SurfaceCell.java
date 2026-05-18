package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;

public class SurfaceCell implements Serializable {
    private static final long serialVersionUID = 1L;
    private final TrenchGrid trench;
    private final SeabedGrid seabed;

    public SurfaceCell(boolean isCentralForPresentation) {
        this.trench = new TrenchGrid();
        this.seabed = new SeabedGrid(isCentralForPresentation);
    }

    public TrenchGrid getTrench() {
        return trench;
    }

    public SeabedGrid getSeabed() {
        return seabed;
    }
}