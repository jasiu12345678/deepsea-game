package pt.isec.pa.deepsea.model.data;

public abstract class Collectible extends GridElement {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    public Collectible() {
        super(nextId++);
    }

    public abstract void completeCollection(Drone drone, IGrid grid);

    public abstract boolean requiresMiniGame();
    public abstract void addToShip(Ship ship);
}
