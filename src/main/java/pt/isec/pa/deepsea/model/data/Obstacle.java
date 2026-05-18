package pt.isec.pa.deepsea.model.data;

public abstract class Obstacle extends GridElement {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1001;

    public Obstacle() {
        super(nextId++);
    }

}
