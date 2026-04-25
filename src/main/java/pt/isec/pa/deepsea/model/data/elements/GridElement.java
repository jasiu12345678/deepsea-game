package pt.isec.pa.deepsea.model.data.elements;

public abstract class GridElement {

    private final int id;
    private boolean isRevealed;

    public GridElement(int id) {
        this.id = id;
        this.isRevealed = false;
    }

    public void reveal() {
        this.isRevealed = true;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

}
