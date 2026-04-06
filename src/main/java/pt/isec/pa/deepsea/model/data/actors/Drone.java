package pt.isec.pa.deepsea.model.data.actors;

import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.Position;
import pt.isec.pa.deepsea.model.data.elements.GridElement;

import java.util.ArrayList;
import java.util.List;

public class Drone {
    private int currentFuel;
    private final int maxFuel;
    private int hullIntegrity;

    private Position location;

    private List<GridElement> transportedResources;

    public Drone() {
        this.maxFuel = GameConstants.DRONE_MAX_FUEL;
        this.currentFuel = maxFuel;
        this.hullIntegrity = GameConstants.DRONE_MAX_HULL;
        this.location = null;
        this.transportedResources = new ArrayList<>();
    }

    public int getCurrentFuel() {
        return currentFuel;
    }
    public int getHullIntegrity() {
        return hullIntegrity;
    }
    public Position getLocation() {
        return location;
    }
    public List<GridElement> getTransportedResources() {
        return transportedResources;
    }

}
