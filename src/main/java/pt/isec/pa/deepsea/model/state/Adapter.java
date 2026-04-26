package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Game;

public abstract class Adapter implements IState {
    protected Context context;
    protected Game game;

    public Adapter(Context context, Game game) {
        this.context = context;
        this.game = game;
    }

    protected void changeState(State newState) throws Exception {
            context.changeState(newState.getInstance(context,game));
    }

    @Override
    public void handleGoTrench() {

    }

    @Override
    public void handleGoSeabed() {

    }

    @Override
    public void handleGoSurface() {

    }

    @Override
    public void handleStartMinigame() {

    }

    @Override
    public void handleGameOver() {

    }

    @Override
    public void handleOpenWorkshop() {

    }

    @Override
    public void entry() {

    }

    @Override
    public void exit() {

    }
}
