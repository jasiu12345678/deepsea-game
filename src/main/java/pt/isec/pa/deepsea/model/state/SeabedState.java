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
        boolean moved = data.moveDroneInSeabed(dir);
        if (!moved && !data.hasActiveDrone()) {
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
        if (data.prepareDroneForAscent()) {
            changeState(State.TRENCH);
            return true;
        }
        return false;
    }
}