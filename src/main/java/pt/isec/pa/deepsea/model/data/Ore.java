package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.Log;

public class Ore extends Collectible {
    private static final long serialVersionUID = 1L;
    private final int quantity;

    public Ore(int quantity) {
        super();
        this.quantity = quantity;
    }

    @Override
    public boolean interact(Drone drone) {
        if (drone != null) {
            drone.addToInventory(this);
            drone.increaseFuelConsumption(quantity);
            return true;
        }
        return false;
    }

    @Override
    public Collectible collect(Drone drone, IGrid grid) {
        Log.getInstance().addLog("Colllecting an ore will increase fuel consumption");
        return this;
    }
    @Override
    public void completeCollection(Drone drone, IGrid grid) {
        drone.addToInventory(this);
        drone.increaseFuelConsumption(quantity);
        grid.removeElement(drone.getX(), drone.getY());
        Log.getInstance().addLog("Ore collected.");
    }
    @Override
    public boolean requiresMiniGame() {
        return false;
    }
}