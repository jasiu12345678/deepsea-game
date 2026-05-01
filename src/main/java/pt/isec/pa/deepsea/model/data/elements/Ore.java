package pt.isec.pa.deepsea.model.data.elements;

import pt.isec.pa.deepsea.model.data.actors.Drone;

public class Ore extends Collectible {
    private static final long serialVersionUID = 1L;
    private final int quantity;

    public Ore(int quantity) {
        super();
        this.quantity = quantity;
    }

    @Override
    public ElementType onInteract(Drone drone) {
        if (drone == null)
            return ElementType.NONE;

        drone.addToInventory(this);
        drone.increaseFuelConsumption(quantity);
        return ElementType.ORE;
    }

    @Override
    public ElementType getType() {return ElementType.ORE;}
}