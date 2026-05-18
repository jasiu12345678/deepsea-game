package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.GameData;

import java.io.Serializable;

public class Context implements Serializable {
    private IState currentState;
    private final GameData data;

    Context(GameData data) {
        this.data = data;
        currentState = State.SURFACE.getInstance(this, data);
        currentState.entry();
        Log.getInstance().addLog("Game initialized: surface");
    }

    public State getState() { return currentState.getState(); }

    GameData getGameData() {
        return data;
    }

    void changeState(IState newState) {
        currentState.exit();
        this.currentState = newState;
        currentState.entry();
        Log.getInstance().addLog("changed state to: " + newState.getState());
    }

    public boolean navigate(Directions dir) { return currentState.navigate(dir); }
    public boolean openWorkshop() { return currentState.openWorkshop(); }
    public boolean closeWorkshop() { return currentState.closeWorkshop(); }
    public boolean startAscent() { return currentState.startAscent(); }
    public boolean startDescent() { return currentState.startDescent(); }
    public boolean collectItem() { return currentState.collectItem(); }
    public boolean repairDrone(int id) { return currentState.repairDrone(id); }
    public boolean refuelDrone(int id) { return currentState.refuelDrone(id); }
    public boolean upgradeDrone(int id) { return currentState.upgradeDrone(id); }
    public boolean selectDrone(int id) { return currentState.selectDrone(id); }
    public boolean resolveMiniGame(boolean won) { return currentState.resolveMiniGame(won); }
    public boolean confirmCollection(boolean confirm) {return currentState.confirmCollection(confirm);}
    public boolean isAwaitingConfirmation() {return data.hasPendingCollectible();}

}