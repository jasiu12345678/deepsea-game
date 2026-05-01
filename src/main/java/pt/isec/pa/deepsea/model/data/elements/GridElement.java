package pt.isec.pa.deepsea.model.data.elements;

import java.io.Serializable;

public abstract class GridElement implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int id;
    private boolean isRevealed;

    public GridElement(int id) {
        this.id = id;
        this.isRevealed = false;
    }

    public int getId() {
        return id;
    }

    public void reveal() {
        this.isRevealed = true;
    }
    public void unreaveal() {
        this.isRevealed = false;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public abstract ElementType getType();

}
