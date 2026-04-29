package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.*;
import pt.isec.pa.deepsea.model.data.GameData;
import pt.isec.pa.deepsea.model.Log;

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
        var cell = data.getMap().getCell(data.getShip().getX(), data.getShip().getY());
        var trench = cell.getTrench();
        boolean moved = data.moveDrone(dir, trench);
        if (!moved && data.getShip().getActiveDrone() == null) {
            if (data.checkLossCondition()) {
                changeState(State.GAME_OVER);
            } else {
                changeState(State.SURFACE);
            }
            return false;
        }

        var drone = data.getShip().getActiveDrone();
        if (drone == null) return false;

        if (dir == Directions.DOWN && drone.getY() >= GameConstants.TRENCH_HEIGHT - 1) {
            drone.setPosition(GameConstants.SEABED_WIDTH / 2, 0);
            changeState(State.SEABED);
            return true;
        }

        if (dir == Directions.UP && drone.getY() <= 0) {
            data.getShip().transferInventoryFrom(drone);
            if (data.checkWinCondition()) {
                changeState(State.GAME_WON);
                Log.getInstance().addLog("All artifacts collected, YOU WIN!");
            } else {
                changeState(State.SURFACE);
                Log.getInstance().addLog("Back on surface.");
            }
            return true;
        }

        return moved;
    }
}