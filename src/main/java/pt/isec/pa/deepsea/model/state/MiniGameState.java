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
        boolean processed = data.resolvePending(won);
        if (processed) {
            if (won) {
                Log.getInstance().addLog("Minigame won!");
                changeState(State.SEABED);
            } else {
                Log.getInstance().addLog("MiniGame lost - ascending");
                data.prepareDroneForAscent();
                changeState(State.TRENCH);
            }
            return true;
        }
        return false;
    }
}
