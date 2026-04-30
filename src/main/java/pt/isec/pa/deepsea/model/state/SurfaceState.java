package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.*;
import pt.isec.pa.deepsea.model.data.GameData;

public class SurfaceState extends Adapter {

    public SurfaceState(Context context, GameData data) {
        super(context, data);
    }

    @Override
    public State getState() {
        return State.SURFACE;
    }

    @Override
    public boolean navigate(Directions dir) {
        boolean moved = data.moveShip(dir);
        if (data.checkLossCondition()) {
            changeState(State.GAME_OVER);
        }
        return moved;
    }

    @Override
    public boolean openWorkshop() {
        changeState(State.WORKSHOP);
        return true;
    }

    @Override
    public boolean startDescent() {
        if (data.prepareDroneForDescent()) {
            changeState(State.TRENCH);
            return true;
        }
        return false;
    }
}