package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.GameData;
import pt.isec.pa.deepsea.model.state.*;


/**
 * Enumeration of game moments (surface, trench, seabed, workshop, etc.).
 * Each value creates the matching {@link IState} implementation.
 */

public enum State {
    SURFACE,
    TRENCH,
    SEABED,
    WORKSHOP,
    MINIGAME,
    GAME_OVER;

    public IState getInstance(Context context, GameData data) {
        return switch (this) {
            case SURFACE -> new SurfaceState(context, data);
            case TRENCH -> new TrenchState(context, data);
            case SEABED -> new SeabedState(context, data);
            case WORKSHOP -> new WorkshopState(context, data);
            case MINIGAME -> new MiniGameState(context, data);
            case GAME_OVER -> new GameOverState(context, data);
        };
    }
}
