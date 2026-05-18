package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.DroneInfo;
import pt.isec.pa.deepsea.model.data.GameData;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.List;

public class Manager {
    private GameData data;
    private Context context;
    PropertyChangeSupport pcs;

    public Manager() {
        this.data = new GameData();
        this.context = new Context(data);
        pcs = new PropertyChangeSupport(this);
    }

    public boolean navigate(Directions dir) {
        boolean result = context.navigate(dir);
        pcs.firePropertyChange("data", null, null);
        pcs.firePropertyChange("state", null, context.getState());
        return result;
    }

    public boolean startDescent() {
        boolean result = context.startDescent();
        pcs.firePropertyChange("data", null, null);
        pcs.firePropertyChange("state", null, context.getState());
        return result;
    }

    public boolean startAscent() {
        boolean result = context.startAscent();
        pcs.firePropertyChange("data", null, null);
        pcs.firePropertyChange("state", null, context.getState());
        return result;
    }

    public boolean openWorkshop() {
        boolean result = context.openWorkshop();
        pcs.firePropertyChange("data", null, null);
        pcs.firePropertyChange("state", null, context.getState());
        return result;
    }

    public boolean closeWorkshop() {
        boolean result = context.closeWorkshop();
        pcs.firePropertyChange("data", null, null);
        pcs.firePropertyChange("state", null, context.getState());
        return result;
    }

    public boolean collectItem() {
        boolean result = context.collectItem();
        pcs.firePropertyChange("data", null, null);
        pcs.firePropertyChange("state", null, context.getState());
        return result;
    }

    public boolean resolveMiniGame(boolean won) {
        boolean result = context.resolveMiniGame(won);
        pcs.firePropertyChange("data", null, null);
        pcs.firePropertyChange("state", null, context.getState());
        return result;
    }

    public boolean repairDrone(int id) {
        boolean result = context.repairDrone(id);
        pcs.firePropertyChange("data", null, null);
        return result;
    }

    public boolean refuelDrone(int id) {
        boolean result = context.refuelDrone(id);
        pcs.firePropertyChange("data", null, null);
        return result;
    }

    public boolean upgradeDrone(int id) {
        boolean result = context.upgradeDrone(id);
        pcs.firePropertyChange("data", null, null);
        return result;
    }

    public boolean selectDrone(int id) {
        boolean result = context.selectDrone(id);
        pcs.firePropertyChange("data", null, null);
        return result;
    }

    public boolean confirmCollection(boolean confirm) {
        boolean result = context.confirmCollection(confirm);
        pcs.firePropertyChange("data", null, null);
        pcs.firePropertyChange("state", null, context.getState());
        return result;
    }

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
            this.context = (Context) in.readObject();
            this.data = context.getGameData();
            return true;
        } catch (Exception e) { return false; }
    }

    public void clearLogs() {
        Log.getInstance().clearLogs();
        pcs.firePropertyChange("log_clear", false, true);
    }
}