package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Drone implements Serializable {
    private static final long serialVersionUID = 1L;
    private int currentFuel;
    private final int maxFuel;
    private int currentHull;
    private int maxHull;
    private int fuelMultiplier;
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
        this.currentFuelConsumption = GameConstants.DRONE_FUEL_CONSUMPTION;
        this.x = 0;
        this.y = 0;
        this.hitCount = 0;
        this.fuelMultiplier = 1;
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
    public int getFuel() {return currentFuel;}
    public int getCurrentFuelConsumption() {return currentFuelConsumption;}
    public int getX() {return x;}
    public int getY() {return y;}
    public void resetHitCount() {hitCount = 0;}
    public static void resetIdCounter() {nextId = 1;}

    public List<Collectible> getTransportedResources() {
        return new ArrayList<>(transportedResources);
    }

    public void consumeFuel(int amount) {
        if (amount > 0) {
            currentFuel = Math.max(0, currentFuel - amount);
        }
    }

    public boolean move(Directions dir, IGrid grid) {
        int nX = x;
        int nY = y;
        switch (dir) {
            case UP -> nY--;
            case DOWN -> nY++;
            case LEFT -> nX--;
            case RIGHT -> nX++;
        }

        resetFuelMultiplier();
        boolean blocksMovement = grid.processInteraction(nX, nY, this);
        int cost = currentFuelConsumption * (fuelMultiplier);
        if (currentHull <= 0) {
            Log.getInstance().addLog("Drone lost all HP");
            return false;
        }
        if (currentFuel < cost) {
            Log.getInstance().addLog("Drone ran out of fuel!");
            consumeFuel(currentFuel);
            return false;
        }
        if (!blocksMovement) {
            consumeFuel(cost);
            setPosition(nX, nY);
            return true;
        }

        return false;
    }

    public void fuelMultuplier(int multiplier) {
        this.fuelMultiplier = multiplier;
    }

    public void resetFuelMultiplier() {
        this.fuelMultiplier = 1;
    }

    public void resetFuelConsumption() {
        currentFuelConsumption = GameConstants.DRONE_FUEL_CONSUMPTION;
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

    public boolean isAlive() {
        return (currentFuel > 0 && currentHull > 0);
    }

    public void clearInventory() {
        transportedResources.clear();
    }
}