package pt.isec.pa.deepsea.model.data.elements;

public abstract class Collectible extends GridElement {
    private static int nextId = 1;

    public Collectible() {
        super(nextId++);
    }
}
