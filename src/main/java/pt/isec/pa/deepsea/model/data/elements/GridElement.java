package pt.isec.pa.deepsea.model.data.elements;

public abstract class GridElement {

    private static int nextId = 1001;
    private final int id;
    private boolean isReavealed;

    public GridElement() {
        this.id = nextId++;
        this.isReavealed = false;
    }

    public void reveal() {
        this.isReavealed = true;
    }

    public boolean isReavealed() {
        return isReavealed;
    }

}
