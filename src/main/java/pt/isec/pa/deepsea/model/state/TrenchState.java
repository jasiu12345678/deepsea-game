package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.*;
import pt.isec.pa.deepsea.model.data.GameData;

public class TrenchState extends Adapter {

    public TrenchState(Context context, GameData data) {
        super(context, data);
    }

    @Override
    public State getState() {
        return State.TRENCH;
    }

    @Override
    public boolean navigate(Directions dir) {
        boolean moved = data.moveDroneInTrench(dir);
        if (!data.hasActiveDrone()) {
            if (data.checkLossCondition()) {
                changeState(State.GAME_OVER);
            } else {
                changeState(State.SURFACE);
            }
            return false;
        }

        if (dir == Directions.DOWN && data.isDroneAtTrenchBottom()) {
            if (data.prepareDroneForSeabed()) {
                changeState(State.SEABED);
                return true;
            }
        }
        if (dir == Directions.UP && data.isDroneAtTrenchTop()) {
            data.handleDroneComeback();
            if (data.checkWinCondition()) {
                changeState(State.GAME_OVER);
                Log.getInstance().addLog("All artifacts collected, YOU WIN!");
            } else {
                changeState(State.SURFACE);
                Log.getInstance().addLog("Back on surface, resources transfered to ship.");
            }
            return true;
        }

        return moved;
    }
}