package pt.isec.pa.deepsea.model.data.elements;

import pt.isec.pa.deepsea.model.data.actors.Drone;

public abstract class Collectible extends GridElement {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    public Collectible() {
        super(nextId++);
    }

    public abstract ElementType onInteract(Drone drone);

}
