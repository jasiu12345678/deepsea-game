package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;

public abstract class GridElement implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int id;
    private boolean isReavealed;

    public GridElement(int id) {
        this.id = id;
        this.isReavealed=false;
    }

    abstract boolean interact(Drone drone);

    Collectible collect(Drone drone, IGrid grid) {
        return null;
    }

    public int getId() {
        return id;
    }

    public boolean reveal() {
        isReavealed = true;
        return true;
    }

    public boolean isRevealed() {
        return isReavealed;
    }
    public boolean unreaveal() {
        isReavealed = false;
        return true;
    }

    public boolean isEmpty() {return false;}
}
