package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.DroneInfo;
import pt.isec.pa.deepsea.model.data.GameData;

import java.io.*;
import java.util.List;

public class Manager {
    private GameData data;
    private Context context;

    public Manager() {
        this.data = new GameData();
        this.context = new Context(data);
    }

    public boolean navigate(Directions dir) { return context.navigate(dir); }
    public boolean startDescent() { return context.startDescent(); }
    public boolean startAscent() { return context.startAscent(); }
    public boolean openWorkshop() { return context.openWorkshop(); }
    public boolean closeWorkshop() { return context.closeWorkshop(); }
    public boolean collectItem() { return context.collectItem(); }
    public boolean resolveMiniGame(boolean won) { return context.resolveMiniGame(won); }
    public boolean repairDrone(int id) { return context.repairDrone(id); }
    public boolean refuelDrone(int id) { return context.refuelDrone(id); }
    public boolean upgradeDrone(int id) { return context.upgradeDrone(id); }
    public boolean selectDrone(int id) { return context.selectDrone(id); }

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

    public String getCurrentStateName() {
        return context.getState().toString();
    }

    public List<DroneInfo> getDronesSortedByFuel() {
        return data.getDronesSortedByFuel();
    }

    public List<DroneInfo> getDronesSortedByHull() {
        return data.getDronesSortedByHullIntegrity();
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

}