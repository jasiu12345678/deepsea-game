package pt.isec.pa.deepsea.ui.states;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import pt.isec.pa.deepsea.model.Manager;
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