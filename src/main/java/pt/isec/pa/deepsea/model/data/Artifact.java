package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.Log;

public class Artifact extends Collectible {
    private static final long serialVersionUID = 1L;

    public Artifact() {
        super();
    }

    @Override
    public boolean interact(Drone drone) {
        return false;
    }

    @Override
    public void addToShip(Ship ship) {
        ship.addArtifact(this);
    }

    @Override
    public Collectible collect(Drone drone, IGrid grid) {
        Log.getInstance().addLog("Artifact found, win the minigame to collect.");
        return this;
    }
    @Override
    public void completeCollection(Drone drone, IGrid grid) {
        drone.addToInventory(this);
        grid.removeElement(drone.getX(), drone.getY());
        Log.getInstance().addLog("Artifact collected. ");
    }
    @Override
    public boolean requiresMiniGame() {
        return true;
    }
    @Override
    public String getImageName() {
        return "artifact.png";
    }

}
