package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.CellType;
import pt.isec.pa.deepsea.model.data.DroneInfo;
import pt.isec.pa.deepsea.model.data.GameData;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.List;

/**
 * Facade of the data model and the only entry point the user interface talks to.
 * <p>
 * Every game operation is delegated to the {@link Context} (the state machine)
 * and to {@link GameData}. After each operation the manager notifies the UI
 * through {@link PropertyChangeSupport} using three channels:
 * <ul>
 *   <li>{@link #STATE} - the active game moment changed;</li>
 *   <li>{@link #DATA} - the game data may have changed and views should redraw;</li>
 *   <li>{@link #LOG} - a log entry was added or the log was cleared.</li>
 * </ul>
 * The UI never accesses the model classes directly; it reads everything through
 * this facade and reacts to the notifications above.
 */

public class Manager {
    private GameData data;
    private Context context;
    PropertyChangeSupport pcs;
    private MoveOutcome lastMoveOutcome = MoveOutcome.NONE;
    public final static String DATA = "data";
    public final static String STATE = "state";
    public final static String LOG = "log";

    public Manager() {
        this.data = new GameData();
        this.context = new Context(data);
        pcs = new PropertyChangeSupport(this);
        Log.getInstance().addPropertyChangeListener(evt -> pcs.firePropertyChange(LOG, null, evt.getNewValue()));
    }

    private boolean handler(State before, boolean result) {
        if (context.getState() != before) {
            pcs.firePropertyChange(STATE, before, context.getState());
        }
        pcs.firePropertyChange(DATA, null, context);
        return result;
    }

    public boolean navigate(Directions dir) {
        State before = context.getState();
        boolean hadDrone = data.hasActiveDrone();
        int hpBefore = data.getActiveDroneHP();
        CellType target = targetCell(before, dir);

        boolean result = context.navigate(dir);

        if (before != State.TRENCH && before != State.SEABED) {
            lastMoveOutcome = MoveOutcome.NONE;
        } else if (hadDrone && !data.hasActiveDrone()) {
            lastMoveOutcome = MoveOutcome.DRONE_DESTROYED;
        } else if (data.hasActiveDrone() && data.getActiveDroneHP() < hpBefore) {
            boolean creature = target == CellType.MARINE_ANIMAL || target == CellType.MONSTER;
            lastMoveOutcome = creature ? MoveOutcome.HIT_CREATURE : MoveOutcome.HIT_OBSTACLE;
        } else {
            lastMoveOutcome = result ? MoveOutcome.MOVED : MoveOutcome.NONE;
        }
        return handler(before, result);
    }

    private CellType targetCell(State state, Directions dir) {
        if (!data.hasActiveDrone()) return CellType.WATER;
        int x = data.getDroneX(), y = data.getDroneY();
        switch (dir) {
            case UP -> y--;
            case DOWN -> y++;
            case LEFT -> x--;
            case RIGHT -> x++;
        }
        return switch (state) {
            case TRENCH -> data.getTrenchElementType(x, y);
            case SEABED -> data.getSeabedElementType(x, y);
            default -> CellType.WATER;
        };
    }

    public MoveOutcome getLastMoveOutcome() { return lastMoveOutcome; }
    public boolean startDescent()             { State b = context.getState(); return handler(b, context.startDescent()); }
    public boolean startAscent()              { State b = context.getState(); return handler(b, context.startAscent()); }
    public boolean openWorkshop()             { State b = context.getState(); return handler(b, context.openWorkshop()); }
    public boolean closeWorkshop()            { State b = context.getState(); return handler(b, context.closeWorkshop()); }
    public boolean collectItem()              { State b = context.getState(); return handler(b, context.collectItem()); }
    public boolean resolveMiniGame(boolean w) { State b = context.getState(); return handler(b, context.resolveMiniGame(w)); }
    public boolean repairDrone(int id)        { State b = context.getState(); return handler(b, context.repairDrone(id)); }
    public boolean refuelDrone(int id)        { State b = context.getState(); return handler(b, context.refuelDrone(id)); }
    public boolean upgradeDrone(int id)       { State b = context.getState(); return handler(b, context.upgradeDrone(id)); }
    public boolean selectDrone(int id)        { State b = context.getState(); return handler(b, context.selectDrone(id)); }

    public boolean hasActiveDrone() { return data.hasActiveDrone(); }
    public int getDroneX() { return data.getDroneX(); }
    public int getDroneY() { return data.getDroneY(); }

    public CellType getSeabedElementType(int x, int y) {
        return data.getSeabedElementType(x, y);
    }

    public CellType getTrenchElementType(int x, int y) {
        return data.getTrenchElementType(x, y);
    }

    public void addClient(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property,listener);
    }

    public void removeClient(String property,PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
    }

    public int getShipFuel() { return data.getShipFuel(); }
    public int getShipX() { return data.getShipX(); }
    public int getShipY() { return data.getShipY(); }
    public int getArtifactCount() { return data.getArtifactCount(); }
    public int getOreCount() { return data.getOreCount(); }

    public int getActiveDroneHP() {
        return data.getActiveDroneHP();
    }

    public int getActiveDroneMaxHP() {
        return data.getActiveDroneMaxHP();
    }

    public int getActiveDroneFuel() {
        return data.getActiveDroneFuel();
    }

    public int getActiveDroneMaxFuel() {
        return data.getActiveDroneMaxFuel();
    }

    public String getActiveDroneName() {
        return data.getActiveDroneName();
    }

    public State getState() {return context.getState();}

    public List<DroneInfo> getDronesSortedByFuel() {
        return data.getDronesSortedByFuel();
    }

    public List<DroneInfo> getDronesSortedByHull() {
        return data.getDronesSortedByHullIntegrity();
    }

    public boolean isSeabedRevealed(int x, int y) {
        return data.isSeabedRevealed(x, y);
    }

    public boolean isCollectibleUnderDrone() {
        if (!data.hasActiveDrone()) return false;
        CellType type = data.getSeabedElementType(data.getDroneX(), data.getDroneY());
        return type == CellType.ORE || type == CellType.ARTIFACT;
    }

    public boolean isVictory() { return data.checkWinCondition(); }

    public int getTotalArtifacts() { return GameConstants.TOTAL_ARTIFACTS; }

    public boolean isPuzzleTileMovable(int x, int y) { return data.isPuzzleTileMovable(x, y); }

    public int getPuzzleMovesLeft() {
        return Math.max(0, GameConstants.MAX_PUZZLE_MOVES - data.getPuzzleMovesCount());
    }

    public List<String> getRecentGames() { return RecentGames.load(); }

    public boolean saveLogs(String filename) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            for (String line : Log.getInstance().getLogs()) writer.write(line + "\n");
            return true;
        } catch (IOException e) { return false; }
    }

    public int[][] getPuzzleGrid() {
        return data.getPuzzleGrid();
    }

    public int getPuzzleMovesCount() {
        return data.getPuzzleMovesCount();
    }

    public boolean save(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(context);
            RecentGames.add(filename);
            return true;
        } catch (IOException e) { return false; }
    }

    public boolean load(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            State before = context.getState();
            this.context = (Context) in.readObject();
            this.data = context.getGameData();
            pcs.firePropertyChange(DATA, null, context);
            pcs.firePropertyChange(STATE, before, context.getState());
            Log.getInstance().addLog("Game loaded: " + filename);
            RecentGames.add(filename);
            return true;
        } catch (Exception e) { return false; }
    }

    public void newGame() {
        State before = context.getState();
        this.data = new GameData();
        this.context = new Context(data);
        pcs.firePropertyChange(DATA, null, context);
        pcs.firePropertyChange(STATE, before, context.getState());
    }

    public void clearLogs() {
        Log.getInstance().clearLogs();
        pcs.firePropertyChange(LOG, false, true);
    }

    public List<String> getLogs() {
        return Log.getInstance().getLogs();
    }
}