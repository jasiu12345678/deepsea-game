package pt.isec.pa.deepsea.model.data.actors;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.Position;
import pt.isec.pa.deepsea.model.data.elements.Artifact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ship {
    private int currentFuel;
    private int oreQuantity;
    private List<Artifact> collectedArtifacts;
    private Set<Drone> deployedDrones;
    private Position location;

    public Ship() {
        this.currentFuel = GameConstants.SHIP_MAX_FUEL;
        this.oreQuantity = 0;
        this.collectedArtifacts = new ArrayList<>();
        this.deployedDrones = new HashSet<>();
        for (int i=0; i<GameConstants.STARTING_DRONES; i++) {
            deployedDrones.add(new Drone());
        }
        this.location = new Position(GameConstants.SURFACE_WIDTH / 2, 0);
    }

    public Set<Drone> getActiveDrones() {
        return deployedDrones;
    }
    public int getCurrentFuel() {
        return currentFuel;
    }
    public int getOreQuantity() {
        return oreQuantity;
    }
    public List<Artifact> getCollectedArtifacts() {
        return collectedArtifacts;
    }
    public Position getLocation() {
        return location;
    }
}
