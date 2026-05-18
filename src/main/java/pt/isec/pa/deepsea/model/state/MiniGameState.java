package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.*;
import pt.isec.pa.deepsea.model.data.GameData;

public class MiniGameState extends Adapter {

    public MiniGameState(Context context, GameData data) {
        super(context, data);
        data.startMiniGame();
    }

    @Override
    public State getState() {
        return State.MINIGAME;
    }

    @Override
    public boolean navigate(Directions dir) {
        boolean moved = data.movePuzzle(dir);
        if (moved) {
            if (data.isPuzzleSolved()) {
                winMinigame();
            } else if (data.getPuzzleMovesCount() >= GameConstants.MAX_PUZZLE_MOVES) {
                loseMinigame();
            }
        }
        return moved;
    }

    private void winMinigame() {
        data.resolvePending(true);
        changeState(State.SEABED);
    }

    private void loseMinigame() {
        data.resolvePending(false);
        if (data.prepareDroneForAscent()) {
            changeState(State.TRENCH);
        } else {
            changeState(State.SURFACE);
        }
    }
}