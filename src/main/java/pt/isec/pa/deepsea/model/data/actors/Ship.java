package pt.isec.pa.deepsea.model.data.actors;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.elements.Artifact;

import java.util.*;

public class Ship {
    private int currentFuel;
    private int oreQuantity;
    private List<Artifact> collectedArtifacts;
    private Set<Drone> activeDrones;
    private int x;
    private int y;

    public Ship() {
        this.currentFuel = GameConstants.SHIP_MAX_FUEL;
        this.oreQuantity = 0;
        this.collectedArtifacts = new ArrayList<>();
        this.activeDrones = new HashSet<>();
        for (int i = 0; i<GameConstants.DRONE_MAX_QUANTITY; i++) {
            activeDrones.add(new Drone());
        }
        this.x = GameConstants.SURFACE_WIDTH / 2;
        this.y = 0;
    }

    public Set<Drone> getActiveDrones() {
        return new HashSet<>(activeDrones);
    }
    public int getCurrentFuel() {
        return currentFuel;
    }
    public int getOreQuantity() {
        return oreQuantity;
    }
    public List<Artifact> getCollectedArtifacts() {
        return new ArrayList<>(collectedArtifacts);
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean hasActiveDrones() {
        return !activeDrones.isEmpty();
    }
}
