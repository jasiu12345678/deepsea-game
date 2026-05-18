package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.Log;

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

    public boolean move(Directions dir, SurfaceGrid map) {
        int nX = x;
        int nY = y;
        switch (dir) {
            case UP -> nY--;
            case DOWN -> nY++;
            case LEFT -> nX--;
            case RIGHT -> nX++;
        }

        if (map.isOutOfBounds(nX, nY)) return false;
        if (currentFuel < GameConstants.SHIP_FUEL_CONSUMPTION) return false;

        consumeFuel(GameConstants.SHIP_FUEL_CONSUMPTION);
        setPosition(nX, nY);
        Log.getInstance().addLog("Ship at: " + nX + "," + nY);
        return true;
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

    public void addArtifact(Artifact artifact) {
        collectedArtifacts.add(artifact);
        Log.getInstance().addLog("Artifact added to ship!");
    }

    public void addOre(int quantity) {
        oreQuantity += quantity;
        Log.getInstance().addLog("Ore quantity increased by: " + quantity);
    }

    public void transferInventoryFrom(Drone drone) {
        if (drone != null && drone.getTransportedResources() != null) {
            for (Collectible item : drone.getTransportedResources()) {
                item.addToShip(this);
            }
            drone.resetFuelConsumption();
            drone.clearInventory();
        }
    }
}
