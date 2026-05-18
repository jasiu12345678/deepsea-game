package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.GameConstants;
import pt.isec.pa.deepsea.model.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameData implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Ship ship;
    private final SurfaceGrid map;
    private Collectible pendingCollectible;
    private MiniGameData minigame;

    public GameData() {
        this.ship = new Ship();
        this.map = new SurfaceGrid();
    }

    // GETTERS FOR UI
    public int getDroneX() {
        var drone = ship.getActiveDrone();
        return drone != null ? drone.getX() : -1;
    }

    public int getDroneY() {
        var drone = ship.getActiveDrone();
        return drone != null ? drone.getY() : -1;
    }

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

    public boolean isSeabedRevealed(int x, int y) {
        var cell = map.getCell(ship.getX(), ship.getY());
        if (cell == null || cell.getSeabed() == null) return false;
        return cell.getSeabed().isRevealed(x, y);
    }

    public String getSeabedElementImage(int x, int y) {
        var cell = map.getCell(ship.getX(), ship.getY());
        if (cell == null || cell.getSeabed() == null) return "water.png";
        return cell.getSeabed().getElementImage(x, y);
    }

    public String getTrenchElementImage(int x, int y) {
        var cell = map.getCell(ship.getX(), ship.getY());
        if (cell == null || cell.getTrench() == null) return "water.png";
        return cell.getTrench().getElementImage(x, y);
    }

    // methods
    public boolean setActiveDrone(int id) { return ship.setActiveDrone(id); }
    public boolean moveShip(Directions dir) {
        return ship.move(dir, map);
    }

    public boolean moveDrone(Directions dir, IGrid grid) {
        Drone drone = ship.getActiveDrone();
        if (drone == null) return false;
        boolean moved = drone.move(dir, grid);
        if (moved && grid != null) {
            grid.processInteraction(drone.getX(), drone.getY(), drone);
        }
        if (!drone.isAlive()) {
            handleDroneLoss();
        }
        return moved;
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

    public boolean collectItem() {
        var drone = ship.getActiveDrone();
        if (drone == null) return false;
        var cell = map.getCell(ship.getX(), ship.getY());
        if (cell == null || cell.getSeabed() == null) return false;

        Collectible collected = cell.getSeabed().processCollection(drone.getX(), drone.getY(), drone);
        if (collected != null) {
            this.pendingCollectible = collected;
            return true;
        }
        return false;
    }
    public void startMiniGame() {
        this.minigame = new MiniGameData();
    }

    public boolean movePuzzle(Directions dir) {
        if (minigame != null) {
            return minigame.move(dir);
        }
        return false;
    }

    public boolean isPuzzleSolved() {
        return minigame != null && minigame.checkWinCondition();
    }

    public int[][] getPuzzleGrid() {
        return minigame != null ? minigame.getGrid() : null;
    }

    public int getPuzzleMovesCount() {
        return minigame != null ? minigame.getMovesCount() : 0;
    }

    public boolean hasPendingCollectible() {
        return pendingCollectible !=null;
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

    public boolean resolvePending(boolean confirmed) {
        if (pendingCollectible == null) return false;
        if (!confirmed) {
            pendingCollectible = null;
            return true;
        }
        var drone = ship.getActiveDrone();
        if (drone == null) return false;
        var cell = map.getCell(ship.getX(), ship.getY());
        if (cell == null) return false;

        pendingCollectible.completeCollection(drone, cell.getSeabed());
        pendingCollectible = null;
        return true;
    }

    public boolean pendingRequiresMiniGame() {
        return pendingCollectible != null && pendingCollectible.requiresMiniGame();
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
        if (drone != null && drone.isAlive()) {
            drone.setPosition(GameConstants.SEABED_WIDTH / 2, GameConstants.SEABED_HEIGHT / 2);
            var cell = map.getCell(ship.getX(), ship.getY());
            if (cell != null && cell.getSeabed() != null) {
                cell.getSeabed().removeElement(drone.getX(), drone.getY());
                cell.getSeabed().processInteraction(drone.getX(), drone.getY(), drone);
            }
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