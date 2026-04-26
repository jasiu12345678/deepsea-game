package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Game;
import pt.isec.pa.deepsea.model.state.states.*;

public enum State {
    SURFACE,
    TRENCH,
    SEABED,
    WORKSHOP,
    MINIGAME,
    GAME_OVER;

    public IState getInstance(Context context, Game game) throws Exception {
        return switch (this) {
            case SURFACE -> new SurfaceState(context, game);
            case TRENCH -> new TrenchState(context, game);
            case SEABED -> new SeabedState(context, game);
            case WORKSHOP -> new WorkshopState(context, game);
            case MINIGAME -> new MiniGameState(context, game);
            case GAME_OVER -> new GameOverState(context, game);
        };
    }
}
