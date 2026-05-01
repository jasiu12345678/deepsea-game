package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Adapter;
import pt.isec.pa.deepsea.model.Context;
import pt.isec.pa.deepsea.model.State;
import pt.isec.pa.deepsea.model.data.GameData;
import pt.isec.pa.deepsea.model.Log;

public class WorkshopState extends Adapter {

    public WorkshopState(Context context, GameData data) {
        super(context, data);
    }

    @Override
    public State getState() {
        return State.WORKSHOP;
    }

    @Override
    public boolean closeWorkshop() {
        changeState(State.SURFACE);
        return true;
    }

    @Override
    public boolean repairDrone(int id) {
        boolean success = data.repairDrone(id);
        if (success) {
            Log.getInstance().addLog("Drone fixed (ID): " + id);
        } else {
            Log.getInstance().addLog("Can't fix drone" + id + " (no fuel or max hull already).");
        }
        return success;
    }

    @Override
    public boolean refuelDrone(int id) {
        boolean success = data.refuelDrone(id);
        if (success) {
            Log.getInstance().addLog("refueled drone with ID: " + id);
        } else {
            Log.getInstance().addLog("can't refuel drone" + id + " (no ship fuel).");
        }
        return success;
    }

    @Override
    public boolean upgradeDrone(int id) {
        boolean success = data.upgradeDrone(id);
        if (success) {
            Log.getInstance().addLog("drone upgraded ID: " + id);
        } else {
            Log.getInstance().addLog("can't fix drone" + id + " (no ores).");
        }
        return success;
    }

    @Override
    public boolean selectDrone(int id) {
        boolean success = data.setActiveDrone(id);
        if (success) {
            Log.getInstance().addLog("selected drone id:" + id);
        } else {
            Log.getInstance().addLog("can't select drone" + id + " (doesn't exist).");
        }
        return success;
    }
}