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
    public void addToShip(Ship ship) {
        ship.addOre(quantity);
    }

    @Override
    public boolean interact(Drone drone) {
        return false;
    }

    @Override
    public Collectible collect(Drone drone, IGrid grid) {
        Log.getInstance().addLog("Collecting an ore will increase fuel consumption.");
        return this;
    }

    @Override
    public void completeCollection(Drone drone, IGrid grid) {
        drone.addToInventory(this);
        drone.increaseFuelConsumption(Math.max(2, quantity/10));
        grid.removeElement(drone.getX(), drone.getY());
        Log.getInstance().addLog("Ore collected.");
    }

    @Override
    public boolean requiresMiniGame() {
        return false;
    }

    @Override
    public String getImageName() {
        return "ore.png";
    }
}