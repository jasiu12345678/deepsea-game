package pt.isec.pa.deepsea.model.data.elements;

public abstract class Obstacle extends GridElement {
    private static int nextId = 1001;

    public Obstacle() {
        super(nextId++);
    }
}
