package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Game;
import pt.isec.pa.deepsea.model.state.states.SurfaceState;

public class Context {
    private IState currentState;
    private Context context;
    private final Game game;

    public Context(Game game) throws Exception {
        this.game = game;
        currentState = new SurfaceState(context, game);
    }
    public State getState() { return currentState.getState(); }
    void changeState(IState newState) throws Exception {this.currentState = newState;}

    public void diveToTrench() {
        currentState.handleGoTrench();
    }

    public void diveToSeabed() {
        currentState.handleGoSeabed();
    }

    public void surfaceUp() {
        currentState.handleGoSurface();
    }

    public void goToWorkshop() {
        currentState.handleOpenWorkshop();
    }

    public void startMinigame() {
        currentState.handleStartMinigame();
    }

    public void gameOver() {
        currentState.handleGameOver();
    }

}