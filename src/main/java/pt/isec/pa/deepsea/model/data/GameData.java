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

    // GETTERS FOR UI
    public int getShipFuel() { return ship.getCurrentFuel(); }
    public int getShipX() { return ship.getX(); }
    public int getShipY() { return ship.getY(); }
    public int getArtifactCount() { return ship.getArtifactCount(); }
    public int getOreCount() { return ship.getOreQuantity(); }
    public int getActiveDroneHP() {
        var drone = ship.getActiveDrone();
        if (drone == null) {
            return 0;
        }
        return drone.getHP();
    }
    public int getActiveDroneMaxHP() {
        var drone = ship.getActiveDrone();
        if (drone == null) {
            return 0;
        }
        return drone.getMaxHP();
    }
    public int getActiveDroneFuel() {
        var drone = ship.getActiveDrone();
        if (drone == null) {
            return 0;
        }
        return drone.getFuel();
    }
    public int getActiveDroneMaxFuel() {
        var drone = ship.getActiveDrone();
        if (drone == null) {
            return 0;
        }
        return drone.getMaxFuel();
    }
    public String getActiveDroneName() {
        var drone = ship.getActiveDrone();
        if (drone == null) {
            return "null";
        }
        return "Drone#" + drone.getId();
    }

    // methods

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
                case ORE,ARTIFACT -> {}
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

    public boolean moveDroneInTrench(Directions dir) {
        var cell = map.getCell(ship.getX(), ship.getY());
        return moveDrone(dir, cell.getTrench());
    }

    public boolean moveDroneInSeabed(Directions dir) {
        var cell = map.getCell(ship.getX(), ship.getY());
        return moveDrone(dir, cell.getSeabed());
    }

    public void handleDroneLoss() {
        Drone drone = ship.getActiveDrone();
        if (drone != null) {
            Log.getInstance().addLog("Drone is dead!");
            var loot = drone.getTransportedResources();
            if (loot != null && !loot.isEmpty()) {
                var cell = map.getCell(ship.getX(), ship.getY());
                if (cell != null) {
                    cell.getSeabed().dropLoot(loot);
                }
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
        drone.addToInventory(pendingCollectible);
        var cell = map.getCell(ship.getX(), ship.getY());
        if (cell != null) {
            cell.getSeabed().removeElementAt(drone.getX(), drone.getY());
        }
        pendingCollectible = null;
        return true;
    }

    public boolean prepareDroneForDescent() {
        Drone drone = ship.getActiveDrone();
        if (drone !=null && drone.isAlive()) {
            drone.resetHitCount();
            drone.setPosition(GameConstants.TRENCH_WIDTH/2, 0);
            return true;
        }
        return false;
    }
    public boolean prepareDroneForAscent() {
        Drone drone = ship.getActiveDrone();
        if (drone != null && drone.isAlive()) {
            drone.setPosition(GameConstants.TRENCH_WIDTH/2, GameConstants.TRENCH_HEIGHT-1);
            return true;
        }
        return false;
    }
    public boolean prepareDroneForSeabed() {
        Drone drone = ship.getActiveDrone();
        if ((drone != null) && drone.isAlive()) {
            drone.setPosition(GameConstants.SEABED_WIDTH/2, 0);
            return true;
        }
        return false;
    }

    public boolean isDroneAtTrenchBottom() {
        Drone drone = ship.getActiveDrone();
        return drone != null && drone.getY() >= GameConstants.TRENCH_HEIGHT - 1;
    }
    public boolean isDroneAtTrenchTop() {
        Drone drone = ship.getActiveDrone();
        return drone != null && drone.getY() <= 0;
    }
    public boolean hasActiveDrone() {
        return ship.getActiveDrone() != null;
    }
    public boolean handleDroneComeback() {
        var drone = ship.getActiveDrone();
        if (drone != null) {
            ship.transferInventoryFrom(drone);
            return true;
        }
        return false;
    }

    public List<DroneInfo> getDronesSortedByFuel() {
        List<DroneInfo> info = new ArrayList<>();
        for (Drone d : ship.getAvailableDrones()) {
            info.add(new DroneInfo(d.getId(), d.getHP(), d.getMaxHP(), d.getFuel(), d.getMaxFuel()));
        }
        info.sort(new DroneInfo.SortByFuel());
        return Collections.unmodifiableList(info);
    }

    public List<DroneInfo> getDronesSortedByHullIntegrity() {
        List<DroneInfo> info = new ArrayList<>();
        for (Drone d : ship.getAvailableDrones()) {
            info.add(new DroneInfo(d.getId(), d.getHP(), d.getMaxHP(), d.getFuel(), d.getMaxFuel()));
        }
        info.sort(new DroneInfo.SortByHullIntegrity());
        return Collections.unmodifiableList(info);
    }
    public boolean checkWinCondition() { return ship.getArtifactCount() >= GameConstants.TOTAL_ARTIFACTS; }
    public boolean checkLossCondition() { return ship.getCurrentFuel() <= 0 || ship.getAvailableDrones().isEmpty(); }
}