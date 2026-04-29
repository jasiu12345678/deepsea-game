package pt.isec.pa.deepsea.model.data.elements;

public abstract class Obstacle extends GridElement {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1001;

    public Obstacle() {
        super(nextId++);
    }

    public abstract int damage();
    public abstract int fuelMultiplier();

}
