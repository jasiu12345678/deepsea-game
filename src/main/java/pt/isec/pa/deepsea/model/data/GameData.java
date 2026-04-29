package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.Log;
import pt.isec.pa.deepsea.model.data.actors.Drone;
import pt.isec.pa.deepsea.model.data.actors.Ship;
import pt.isec.pa.deepsea.model.data.elements.Collectible;
import pt.isec.pa.deepsea.model.data.elements.ElementType;
import pt.isec.pa.deepsea.model.data.elements.GridElement;
import pt.isec.pa.deepsea.model.data.grids.IGrid;
import pt.isec.pa.deepsea.model.data.grids.SeabedGrid;
import pt.isec.pa.deepsea.model.data.grids.SurfaceCell;
import pt.isec.pa.deepsea.model.data.grids.SurfaceGrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameData implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Ship ship;
    private final SurfaceGrid map;
    private Collectible pendingCollectible;

    public GameData() {
        this.ship = new Ship();
        this.map = new SurfaceGrid();
    }

    public Ship getShip() { return ship; }
    public SurfaceGrid getMap() { return map; }
    public Collectible getPendingCollectible() { return pendingCollectible; }
    public void setPendingCollectible(Collectible pending) { this.pendingCollectible = pending; }

    public boolean moveShip(Directions dir) {
        int nX = ship.getX();
        int nY = ship.getY();
        switch (dir) {
            case UP -> nY--;
            case DOWN -> nY++;
            case LEFT -> nX--;
            case RIGHT -> nX++;
        }
        if (map.isOutOfBounds(nX, nY)) return false;
        if (ship.getCurrentFuel() < GameConstants.SHIP_FUEL_CONSUMPTION) return false;

        ship.consumeFuel(GameConstants.SHIP_FUEL_CONSUMPTION);
        ship.setPosition(nX, nY);
        Log.getInstance().addLog("Ship at: " + nX + "," + nY);
        return true;
    }

    public boolean moveDrone(Directions dir, IGrid grid) {
        Drone drone = ship.getActiveDrone();
        if (drone == null) return false;

        int nX = drone.getX();
        int nY = drone.getY();
        switch (dir) {
            case UP -> nY--;
            case DOWN -> nY++;
            case LEFT -> nX--;
            case RIGHT -> nX++;
        }

        if (grid.isOutOfBounds(nX, nY)) return false;

        GridElement element = grid.getElement(nX, nY);
        int cost = drone.getCurrentFuelConsumption();
        boolean blocksMovement = false;

        if (element != null) {
            switch (element.getType()) {
                case CURRENT -> cost *= 2;
                case ROCK -> {
                    blocksMovement = true;
                    drone.takeDamage();
                    Log.getInstance().addLog("Drone hit a rock!");
                }
                case MONSTER, MARINE_ANIMAL -> {
                    drone.takeDamage();
                    Log.getInstance().addLog("Ouch! That's a hostile."); }
                default -> {}
            }
        }

        if (drone.getHP() <= 0 ) {
            Log.getInstance().addLog("Drone lost all HP");
            handleDroneLoss();
            return false;
        }
        if (drone.getFuel() < cost) {
            Log.getInstance().addLog("Drone ran out of fuel!");
            handleDroneLoss();
            return false;
        }

        if (!blocksMovement) {
            drone.consumeFuel(cost);
            drone.setPosition(nX, nY);
            if (grid instanceof SeabedGrid seabed) {
                seabed.revealAt(nX, nY);
            }
            return true;
        }
        return false;
    }

    public void handleDroneLoss() {
        Drone drone = ship.getActiveDrone();
        if (drone != null) {
            Log.getInstance().addLog("Drone is dead!");
            if (drone.getTransportedResources() != null) {
                SurfaceCell cell = map.getCell(ship.getX(), ship.getY());
                cell.getSeabed().dropLoot(drone.getTransportedResources());
            }
            ship.removeActiveDrone();
        }
    }

    public ElementType collectItem() {
        var drone = ship.getActiveDrone();
        if (drone == null) return ElementType.NONE;
        var cell = map.getCell(ship.getX(), ship.getY());
        var seabed = cell.getSeabed();
        var element = seabed.getElement(drone.getX(), drone.getY());

        if (!(element instanceof Collectible collectible)) {
            return ElementType.NONE;
        }

        ElementType result = collectible.onInteract(drone);

        if (result == ElementType.ORE) {
            seabed.removeElementAt(drone.getX(), drone.getY());
        } else if (result == ElementType.ARTIFACT) {
            this.pendingCollectible = collectible;
        }

        return result;
    }

    public boolean setActiveDrone(int id) {
        return ship.setActiveDrone(id);
    }

    public boolean repairDrone(int id) {
        return ship.repairDrone(id);
    }

    public boolean refuelDrone(int id) {
        return ship.refuelDrone(id);
    }

    public boolean upgradeDrone(int id) {
        return ship.upgradeDrone(id);
    }

    public boolean resolveMiniGame(boolean won) {
        if (pendingCollectible == null) return false;

        if (!won) {
            pendingCollectible = null;
            return true;
        }
        var drone = ship.getActiveDrone();
        if (drone == null) return false;
        pendingCollectible.onMiniGameWon(drone);
        var cell = map.getCell(ship.getX(), ship.getY());
        if (cell != null) {
            cell.getSeabed().removeElementAt(drone.getX(), drone.getY());
        }

        pendingCollectible = null;
        return true;
    }

    public List<Drone> getDronesSortedByFuel() {
        List<Drone> dronesSortedByFuel = new ArrayList<>(getShip().getAvailableDrones());
        dronesSortedByFuel.sort(new Drone.SortByFuel());
        return Collections.unmodifiableList(dronesSortedByFuel);
    }

    public List<Drone> getDronesSortedByHullIntegrity() {
        List<Drone> dronesSortedByHull = new ArrayList<>(getShip().getAvailableDrones());
        dronesSortedByHull.sort(new Drone.SortByHullIntegrity());
        return Collections.unmodifiableList(dronesSortedByHull);
    }

    public boolean checkWinCondition() { return ship.getArtifactCount() >= GameConstants.TOTAL_ARTIFACTS; }
    public boolean checkLossCondition() { return ship.getCurrentFuel() <= 0 || ship.getAvailableDrones().isEmpty(); }
}