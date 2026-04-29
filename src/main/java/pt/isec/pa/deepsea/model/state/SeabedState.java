package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.*;
import pt.isec.pa.deepsea.model.data.GameData;
import pt.isec.pa.deepsea.model.data.elements.ElementType;

public class SeabedState extends Adapter {

    public SeabedState(Context context, GameData data) {
        super(context, data);
    }

    @Override
    public State getState() {
        return State.SEABED;
    }

    @Override
    public boolean navigate(Directions dir) {
        var cell = data.getMap().getCell(data.getShip().getX(), data.getShip().getY());
        var seabed = cell.getSeabed();

        boolean moved = data.moveDrone(dir, seabed);

        if (!moved && data.getShip().getActiveDrone() == null) {
            Log.getInstance().addLog("Drone died in seabed.. ");
            if (data.checkLossCondition()) {
                changeState(State.GAME_OVER);
            } else {
                changeState(State.SURFACE);
            }
            return false;
        }

        return moved;
    }

    @Override
    public boolean collectItem() {
        ElementType result = data.collectItem();

        if (result == ElementType.ARTIFACT) {
            Log.getInstance().addLog("Artifact found, finish the minigame to collect it!");
            changeState(State.MINIGAME);
            return true;
        }

        if (result == ElementType.ORE) {
            Log.getInstance().addLog("Ore collected.");
            return true;
        }

        return false;
    }

    @Override
    public boolean startAscent() {
        var drone = data.getShip().getActiveDrone();
        if (drone == null) return false;
        drone.setPosition(GameConstants.TRENCH_WIDTH/2, GameConstants.TRENCH_HEIGHT - 1);
        Log.getInstance().addLog("Drone ascend.");
        changeState(State.TRENCH);
        return true;
    }
}