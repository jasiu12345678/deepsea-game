package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.actors.Drone;
import pt.isec.pa.deepsea.model.data.actors.Ship;
import pt.isec.pa.deepsea.model.data.grids.SurfaceGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private final Ship ship;
    private final SurfaceGrid map;

    public Game() {
        this.ship = new Ship();
        this.map = new SurfaceGrid();
    }

    public Ship getShip() {
        return ship;
    }

    public SurfaceGrid getMap() {
        return map;
    }


    public List<Drone> getDronesSortedByFuel() {
        List<Drone> dronesSortedByFuel = new ArrayList<>(ship.getActiveDrones());
        dronesSortedByFuel.sort(new Drone.SortByFuel());
        return Collections.unmodifiableList(dronesSortedByFuel);
    }

    public List<Drone> getDronesSortedByHullIntegrity() {
        List<Drone> dronesSortedByHuel = new ArrayList<>(ship.getActiveDrones());
        dronesSortedByHuel.sort(new Drone.SortByHuelIntegrity());
        return Collections.unmodifiableList(dronesSortedByHuel);
    }

    public void activateCurrentZone(int x, int y, boolean isSeabed) {
        var cell = map.getCell(x, y);
        if (cell != null) {
            if (isSeabed) {
                cell.getSeabed().activateZone();
            } else {
                cell.getTrench().activateZone();
            }
        }
    }
}