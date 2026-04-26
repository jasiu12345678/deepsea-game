package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.Game;

public class Manager {
    private final Game game;
    private final Context context;
    public Manager(Context context, Game game) throws Exception {
        this.game = new Game();
        this.context = new Context(game);
    }

    public void goToTrench() {}
    public void goToSurface() {}
    public void goToSeabed() {}
    public void startMiniGame() {}
    public void openWorkshop() {}
    public void gameOver() {}
}
