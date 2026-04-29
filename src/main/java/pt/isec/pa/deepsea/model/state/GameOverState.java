package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Adapter;
import pt.isec.pa.deepsea.model.Context;
import pt.isec.pa.deepsea.model.State;
import pt.isec.pa.deepsea.model.data.GameData;


public class GameOverState extends Adapter {
    public GameOverState(Context context, GameData data) { super(context, data); }

    @Override
    public State getState() {return State.GAME_OVER;}
}
