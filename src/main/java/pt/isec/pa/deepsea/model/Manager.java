package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.GameData;

import java.io.*;

public class Manager {
    private GameData data;
    private Context context;

    public Manager() {
        this.data = new GameData();
        this.context = new Context(data);
    }

    public void navigate(Directions dir) { context.navigate(dir); }
    public void startDescent() { context.startDescent(); }
    public void startAscent() {context.startAscent();}
    public void openWorkshop() { context.openWorkshop(); }
    public void closeWorkshop() { context.closeWorkshop(); }
    public void collectItem() { context.collectItem(); }
    public void resolveMiniGame(boolean won) { context.resolveMiniGame(won); }
    public void repairDrone(int id) { context.repairDrone(id); }
    public void refuelDrone(int id) { context.refuelDrone(id); }
    public void upgradeDrone(int id) { context.upgradeDrone(id); }
    public void selectDrone(int id) { context.selectDrone(id); }

    public State getCurrentState() { return context.getState(); }
    public int getShipFuel() { return data.getShip().getCurrentFuel(); }
    public int getShipX() { return data.getShip().getX(); }
    public int getShipY() { return data.getShip().getY(); }
    public int getArtifactCount() { return data.getShip().getArtifactCount(); }
    public int getOreCount() { return data.getShip().getOreQuantity(); }
    public boolean hasActiveDrone() { return data.getShip().getActiveDrone() != null; }

    public int getActiveDroneHP() {
        var drone = data.getShip().getActiveDrone();
        if (drone == null) {
            return 0;
        }
        return drone.getHP();
    }
    public int getActiveDroneMaxHP() {
        var drone = data.getShip().getActiveDrone();
        if (drone == null) {
            return 0;
        }
        return drone.getMaxHP();
    }
    public int getActiveDroneFuel() {
        var drone = data.getShip().getActiveDrone();
        if (drone == null) {
            return 0;
        }
        return drone.getFuel();
    }
    public int getActiveDroneMaxFuel() {
        var drone = data.getShip().getActiveDrone();
        if (drone == null) {
            return 0;
        }
        return drone.getMaxFuel();
    }
    public String getActiveDroneName() {
        var drone = data.getShip().getActiveDrone();
        if (drone == null) {
            return "null";
        }
        return "Drone#" + drone.getId();
    }
    public String getCurrentStateName() {
        return context.getState().toString();
    }

    public boolean save(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
            return true;
        } catch (IOException e) { return false; }
    }

    public boolean load(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            this.data = (GameData) ois.readObject();
            this.context = new Context(data);
            return true;
        } catch (Exception e) { return false; }
    }

}