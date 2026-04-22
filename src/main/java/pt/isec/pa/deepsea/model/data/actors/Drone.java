package pt.isec.pa.deepsea.model.data.actors;

import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.data.elements.GridElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Drone {
    private int currentFuel;
    private final int maxFuel;
    private int hullIntegrity;
    private static int nextId =1;
    private final int id;
    private int x;
    private int y;

    private List<GridElement> transportedResources;

    public Drone() {
        this.maxFuel = GameConstants.DRONE_MAX_FUEL;
        this.currentFuel = maxFuel;
        this.hullIntegrity = GameConstants.DRONE_MAX_HULL;
        this.transportedResources = new ArrayList<>();
        this.id = nextId++;
    }

    public static class SortByFuel implements Comparator {
        public int compare(Object obj1, Object obj2) {
            Drone a = (Drone) obj1;
            Drone b = (Drone) obj2;

            if (a.currentFuel < b.currentFuel) return -1;
            if (a.currentFuel > b.currentFuel) return 1;
            return 0;
        }
    }

    public static class SortByHuelIntegrity implements Comparator {
        public int compare(Object obj1, Object obj2) {
            Drone a = (Drone) obj1;
            Drone b = (Drone) obj2;

            if (a.hullIntegrity < b.hullIntegrity) return -1;
            if (a.hullIntegrity > b.hullIntegrity) return 1;
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("[id:%d fuel:%d/%d hp:%d pos:(%d,%d) res:%d]",
                id, currentFuel, maxFuel, hullIntegrity, x,y, transportedResources.size());
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

    public int getCurrentFuel() {
        return currentFuel;
    }
    public int getHullIntegrity() {
        return hullIntegrity;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public List<GridElement> getTransportedResources() {
        List<GridElement> unmodifiableTransportedResources = Collections.unmodifiableList(transportedResources);
        return unmodifiableTransportedResources;
    }

}
