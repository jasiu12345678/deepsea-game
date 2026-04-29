package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.*;
import pt.isec.pa.deepsea.model.data.GameData;
import pt.isec.pa.deepsea.model.data.actors.Drone;

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
        Drone drone = data.getShip().getActiveDrone();
        if (drone == null) return false;
        drone.resetHitCount();
        drone.setPosition(GameConstants.TRENCH_WIDTH / 2, 0);
        changeState(State.TRENCH);
        return true;
    }
}