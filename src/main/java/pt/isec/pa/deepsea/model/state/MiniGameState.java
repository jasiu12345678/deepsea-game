package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.*;
import pt.isec.pa.deepsea.model.data.GameData;

public class MiniGameState extends Adapter {
    public MiniGameState(Context context, GameData data) {super(context, data);}

    @Override
    public State getState() {
        return State.MINIGAME;
    }

    @Override
    public boolean resolveMiniGame(boolean won) {
        boolean processed = data.resolveMiniGame(won);
        var drone = data.getShip().getActiveDrone();
        if (processed) {
            if (won) {
                Log.getInstance().addLog("Artifact collected succesfully.");
                changeState(State.SEABED);
            } else {
                Log.getInstance().addLog("MiniGame lost - ascending");
                drone.setPosition(GameConstants.TRENCH_WIDTH/2, GameConstants.TRENCH_HEIGHT-1);
                changeState(State.TRENCH);
            }
            return true;
        }
        return false;
    }
}
