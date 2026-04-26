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

    public List<Drone> getDronesSortedByFuel() {
        List<Drone> dronesSortedByFuel = new ArrayList<>(ship.getActiveDrones());
        dronesSortedByFuel.sort(new Drone.SortByFuel());
        return Collections.unmodifiableList(dronesSortedByFuel);
    }

    public List<Drone> getDronesSortedByHullIntegrity() {
        List<Drone> dronesSortedByHull = new ArrayList<>(ship.getActiveDrones());
        dronesSortedByHull.sort(new Drone.SortByHullIntegrity());
        return Collections.unmodifiableList(dronesSortedByHull);
    }

}