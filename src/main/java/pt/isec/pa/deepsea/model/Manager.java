package pt.isec.pa.deepsea.model;

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

    public boolean navigate(Directions dir)   { State b = context.getState(); return handler(b, context.navigate(dir)); }
    public boolean startDescent()             { State b = context.getState(); return handler(b, context.startDescent()); }
    public boolean startAscent()              { State b = context.getState(); return handler(b, context.startAscent()); }
    public boolean openWorkshop()             { State b = context.getState(); return handler(b, context.openWorkshop()); }
    public boolean closeWorkshop()            { State b = context.getState(); return handler(b, context.closeWorkshop()); }
    public boolean collectItem()              { State b = context.getState(); return handler(b, context.collectItem()); }
    public boolean resolveMiniGame(boolean w) { State b = context.getState(); return handler(b, context.resolveMiniGame(w)); }
    public boolean confirmCollection(boolean c){ State b = context.getState(); return handler(b, context.confirmCollection(c)); }
    public boolean repairDrone(int id)        { State b = context.getState(); return handler(b, context.repairDrone(id)); }
    public boolean refuelDrone(int id)        { State b = context.getState(); return handler(b, context.refuelDrone(id)); }
    public boolean upgradeDrone(int id)       { State b = context.getState(); return handler(b, context.upgradeDrone(id)); }
    public boolean selectDrone(int id)        { State b = context.getState(); return handler(b, context.selectDrone(id)); }

    public boolean hasActiveDrone() { return data.hasActiveDrone(); }
    public int getDroneX() { return data.getDroneX(); }
    public int getDroneY() { return data.getDroneY(); }

    public String getSeabedElementImage(int x, int y) {
        return data.getSeabedElementImage(x, y);
    }

    public String getTrenchElementImage(int x, int y) {
        return data.getTrenchElementImage(x, y);
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

    public boolean isAwaitingConfirmation() {
        return context.isAwaitingConfirmation();
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