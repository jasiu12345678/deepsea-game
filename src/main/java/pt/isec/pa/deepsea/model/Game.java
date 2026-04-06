package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.actors.Drone;
import pt.isec.pa.deepsea.model.data.actors.Ship;
import pt.isec.pa.deepsea.model.data.grids.SurfaceGrid;
import pt.isec.pa.deepsea.model.data.minigame.MiniGameData;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private final Ship ship;
    private final SurfaceGrid map;
    private final MiniGameData miniGame;

    public Game() {
        this.ship = new Ship();
        this.map = new SurfaceGrid();
        this.miniGame = new MiniGameData();
    }

    public Ship getShip() {
        return ship;
    }

    public SurfaceGrid getMap() {
        return map;
    }

    public MiniGameData getMiniGame() {
        return miniGame;
    }

    public List<Drone> getDronesSortedByFuel() {
        return ship.getActiveDrones().stream()
                .sorted(Comparator.comparingInt(Drone::getCurrentFuel).reversed())
                .collect(Collectors.toList());
    }

    public List<Drone> getDronesSortedByHullIntegrity() {
        return ship.getActiveDrones().stream()
                .sorted(Comparator.comparingInt(Drone::getHullIntegrity).reversed())
                .collect(Collectors.toList());
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