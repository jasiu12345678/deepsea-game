package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.GameData;

/**
 * Base class for model states. Provides default {@code false} for actions that are not allowed.
 */

public abstract class Adapter implements IState {
    protected Context context;
    protected GameData data;

    public Adapter(Context context, GameData data) {
        this.context = context;
        this.data = data;
    }

    protected void changeState(State newState) {
        context.changeState(newState.getInstance(context, data));
    }

    public abstract State getState();

    @Override
    public boolean navigate(Directions dir) {
        return false;
    }

    @Override
    public boolean openWorkshop() {
        return false;
    }

    @Override
    public boolean closeWorkshop() {
        return false;
    }

    @Override
    public boolean repairDrone(int droneId) {
        return false;
    }

    @Override
    public boolean refuelDrone(int droneId) {
        return false;
    }

    @Override
    public boolean upgradeDrone(int droneId) {
        return false;
    }

    @Override
    public boolean selectDrone(int droneId) {
        return false;
    }

    @Override
    public boolean startDescent() {
        return false;
    }

    @Override
    public boolean startAscent() {
        return false;
    }

    @Override
    public boolean collectItem() {
        return false;
    }

    @Override
    public boolean resolveMiniGame(boolean won) {
        return false;
    }
    @Override
    public boolean confirmCollection(boolean confirm) {
        return false;
    }

    @Override
    public void entry() {
    }

    @Override
    public void exit() {
    }
}
