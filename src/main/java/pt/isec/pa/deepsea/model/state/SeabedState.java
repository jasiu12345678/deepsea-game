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
        if (data.collectItem()) {
            if (data.hasPendingCollectible()) {
                changeState(State.MINIGAME);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean confirmCollection(boolean confirm) {
        if (!data.hasPendingCollectible()) return false;
        if (!confirm) {
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