package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.Game;
import pt.isec.pa.deepsea.model.state.Context;
import pt.isec.pa.deepsea.model.state.Adapter;

public class GameOverState extends Adapter {
    public GameOverState(Context context, Game game) { super(context, game); }
}
