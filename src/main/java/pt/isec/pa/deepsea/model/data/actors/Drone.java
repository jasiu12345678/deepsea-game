package pt.isec.pa.deepsea.model.data.actors;

import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.elements.Collectible;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Drone implements Serializable {
    private static final long serialVersionUID = 1L;
    private int currentFuel;
    private final int maxFuel;
    private int currentHull;
    private int maxHull;
    private static int nextId = 1;
    private final int id;
    private int x;
    private int y;
    private int currentFuelConsumption;
    private final List<Collectible> transportedResources;
    private int hitCount;
    private static final int[] DAMAGE_SEQUENCE = {1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29};

    public Drone() {
        this.maxFuel = GameConstants.DRONE_MAX_FUEL;
        this.currentFuel = maxFuel;
        this.maxHull = GameConstants.DRONE_MAX_HULL;
        this.currentHull = maxHull;
        this.transportedResources = new ArrayList<>();
        this.id = nextId++;
        this.currentFuelConsumption = 1;
        this.x = 0;
        this.y = 0;
        this.hitCount = 0;
    }


    public static class SortByFuel implements Comparator<Drone> {
        @Override
        public int compare(Drone a, Drone b) {
            return Integer.compare(b.currentFuel, a.currentFuel);
        }
    }

    public static class SortByHullIntegrity implements Comparator<Drone> {
        @Override
        public int compare(Drone a, Drone b) {
            return Integer.compare(b.currentHull, a.currentHull);
        }
    }

    @Override
    public String toString() {
        return String.format("[id:%d fuel:%d/%d hp:%d pos:(%d,%d) res:%d]",
                id, currentFuel, maxFuel, currentHull, x, y, transportedResources.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Drone drone = (Drone) obj;
        return this.id == drone.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {
        return id;
    }
    public int getCurrentFuel() {
        return currentFuel;
    }
    public int getMaxFuel() {
        return maxFuel;
    }
    public int getHP() {
        return currentHull;
    }
    public int getMaxHP() {return maxHull;}
    public int getCurrentHull() {return currentHull;}
    public int getFuel() {return currentFuel;}
    public int getCurrentFuelConsumption() {return currentFuelConsumption;}
    public int getX() {return x;}
    public int getY() {return y;}
    public void resetHitCount() {hitCount = 0;}

    public List<Collectible> getTransportedResources() {
        return new ArrayList<>(transportedResources);
    }

    public void move(int dx, int dy, int fuelCost) {
        this.x += dx;
        this.y += dy;
        consumeFuel(fuelCost);
    }

    public void consumeFuel(int amount) {
        if (amount > 0) {
            currentFuel = Math.max(0, currentFuel - amount);
        }
    }

    public void takeDamage() {
        int damage = DAMAGE_SEQUENCE[Math.min(hitCount, DAMAGE_SEQUENCE.length-1)];
        currentHull = Math.max(0, currentHull - damage);
        hitCount++;
    }

    public void repair(int amount) {
        if (amount > 0) {
            currentHull = Math.min(GameConstants.DRONE_MAX_HULL, currentHull + amount);
        }
    }

    public void refuel(int amount) {
        if (amount > 0) {
            currentFuel = Math.min(maxFuel, currentFuel + amount);
        }
    }

    public void increaseMaxHull(int amount) {
        if (amount > 0) {
            maxHull += amount;
        }
    }

    public void increaseFuelConsumption(int quantity) {
        if (quantity > 0)
            currentFuelConsumption += quantity;
    }

    public void addToInventory(Collectible item) {
        if (item != null)
            transportedResources.add(item);
    }

    public void setPosition(int nx, int ny) {
        x = nx;
        y = ny;
    }
}