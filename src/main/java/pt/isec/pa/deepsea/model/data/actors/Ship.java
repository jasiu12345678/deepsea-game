package pt.isec.pa.deepsea.model.data.actors;

import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.elements.Artifact;
import pt.isec.pa.deepsea.model.data.elements.Collectible;

import java.io.Serializable;
import java.util.*;

public class Ship implements Serializable {
    private static final long serialVersionUID = 1L;
    private int currentFuel;
    private int oreQuantity;
    private final Set<Drone> availableDrones;
    private Drone activeDrone;
    private int x;
    private int y;
    private final List<Collectible> shipInventory;
    private final List<Artifact> collectedArtifacts;

    public Ship() {
        this.currentFuel = GameConstants.SHIP_MAX_FUEL;
        this.oreQuantity = 0;
        this.availableDrones = new HashSet<>();
        this.shipInventory = new ArrayList<>();
        for (int i = 0; i < GameConstants.DRONE_MAX_QUANTITY; i++) {
            availableDrones.add(new Drone());
        }
        this.activeDrone = null;
        this.x = GameConstants.SURFACE_WIDTH / 2;
        this.y = 0;
        this.collectedArtifacts = new ArrayList<>();
    }

    public Set<Drone> getAvailableDrones() {
        return new HashSet<>(availableDrones);
    }

    public int getCurrentFuel() {
        return currentFuel;
    }
    public int getArtifactCount() {return collectedArtifacts.size();}
    public int getOreQuantity() {return oreQuantity;}
    public Drone getActiveDrone() {return activeDrone;}
    public int getX() {return x;}
    public int getY() {return y;}

    public void consumeFuel(int amount) {
        if (amount > 0) {
            currentFuel = Math.max(0, currentFuel - amount);
        }
    }

    public void consumeOres(int quantity) {
        if (quantity > 0 ) {
            oreQuantity = Math.max(0, oreQuantity - quantity);
        }
    }

    public Drone getDrone(int id) {
        for (Drone drone : availableDrones) {
            if (drone.getId() == id) {
                return drone;
            }
        }
        return null;
    }

    public boolean setActiveDrone(int id) {
        for (Drone drone : availableDrones) {
            if (drone.getId() == id) {
                activeDrone = drone;
                return true;
            }
        }
        return false;
    }

    public void setPosition(int newX,int newY) {
        x = newX;
        y = newY;
    }

    public boolean removeActiveDrone() {
        if (activeDrone == null) return false;
        availableDrones.remove(activeDrone);
        activeDrone = null;
        return true;
    }

    public boolean upgradeDrone(int id) {
        Drone drone = getDrone(id);
        if (drone == null || this.oreQuantity < 10) return false;
        consumeOres(10);
        drone.increaseMaxHull(10);
        drone.repair(10);
        return true;
    }
    public boolean removeDrone(int id) {
        var drone = getDrone(id);
        if (drone == null) return false;
        return availableDrones.remove(drone);
    }
    public boolean repairDrone(int id) {
        var drone = getDrone(id);
        if (drone == null || currentFuel < 5 || drone.getHP() >= drone.getMaxHP()) {
            return false;
        }
        consumeFuel(5);
        drone.repair(5);
        return true;
    }
    public boolean refuelDrone(int id) {
        var drone = getDrone(id);
        if (drone == null || currentFuel < 1 || drone.getFuel() >= drone.getMaxFuel()) {
            return false;
        }
        consumeFuel(1);
        drone.refuel(1);
        return true;
    }
    public void transferInventoryFrom(Drone drone) {
        if (drone != null && drone.getTransportedResources() != null) {
            shipInventory.addAll(drone.getTransportedResources());
            drone.resetFuelConsumption();
            drone.clearInventory();
        }
    }
}
