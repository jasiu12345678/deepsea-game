package pt.isec.pa.deepsea.ui.states;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.ui.res.SoundManager;
import pt.isec.pa.deepsea.model.State;

/**
 * Base class for views drawn on a {@link javafx.scene.canvas.Canvas}.
 * Handles resizing, focus, visibility and refresh when the model changes.
 */

public abstract class CanvasState extends Canvas {
    protected final Manager manager;

    protected CanvasState(Manager manager) {
        this.manager = manager;
        setFocusTraversable(true);
        setOnMouseClicked(e -> requestFocus());

        widthProperty().addListener(o -> draw());
        heightProperty().addListener(o -> draw());

        manager.addClient(Manager.DATA, evt -> draw());
        manager.addClient(Manager.STATE, evt -> updateVisibility());

        registerHandlers();
        updateVisibility();
    }

    protected abstract State matchingState();

    protected abstract void draw();

    protected void registerHandlers() {}

    /** Plays the sound matching the outcome of the last drone movement. */
    protected void playMoveSound() {
        switch (manager.getLastMoveOutcome()) {
            case DRONE_DESTROYED -> SoundManager.playForever(SoundManager.DRONE_DESTROYED);
            case HIT_CREATURE -> SoundManager.playThen(SoundManager.DRONE_MOVE, SoundManager.DRONE_ANIMAL);
            case HIT_OBSTACLE -> SoundManager.play(SoundManager.DRONE_HIT);
            case MOVED -> SoundManager.play(SoundManager.DRONE_MOVE);
            case NONE -> { }
        }
    }

    /** Fills the whole canvas with the given colour. */
    protected void clear(javafx.scene.paint.Color color) {
        var gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(color);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    protected void updateVisibility() {
        boolean active = manager.getState() == matchingState();
        setVisible(active);
        if (active) {
            Platform.runLater(this::requestFocus);
            draw();
        }
    }

    @Override public boolean isResizable()        { return true; }
    @Override public double minWidth(double h)    { return 0; }
    @Override public double minHeight(double w)   { return 0; }
    @Override public double maxWidth(double h)    { return Double.MAX_VALUE; }
    @Override public double maxHeight(double w)   { return Double.MAX_VALUE; }
    @Override public double prefWidth(double h)   { return getWidth(); }
    @Override public double prefHeight(double w)  { return getHeight(); }
    @Override public void resize(double w, double h) { setWidth(w); setHeight(h); }
}