package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.*;
import pt.isec.pa.deepsea.model.data.GameData;

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
        if (data.hasPendingCollectible()) {
            return false;
        }
        boolean moved = data.moveDroneInSeabed(dir);
        if (!data.hasActiveDrone()) {
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
        return data.collectItem();
    }

    @Override
    public boolean confirmCollection(boolean accept) {
        if (!data.hasPendingCollectible()) {
            return false;
        }
        if (!accept) {
            data.resolvePending(false);
            return true;
        }
        if (data.pendingRequiresMiniGame()) {
            changeState(State.MINIGAME);
        } else {
            data.resolvePending(true);
        }
        return true;
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