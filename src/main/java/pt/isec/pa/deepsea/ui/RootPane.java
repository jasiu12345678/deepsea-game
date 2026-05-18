package pt.isec.pa.deepsea.ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import pt.isec.pa.deepsea.model.Directions;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.ui.states.*;

public class RootPane extends BorderPane {
    private final Manager manager;

    public RootPane(Manager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
    }

    private void createViews() {
        this.setTop(new MainUI(manager));
        this.setLeft(new SidebarUI(manager));

        StackPane centerArea = new StackPane();

        SurfaceState surfaceState = new SurfaceState(manager);
        TrenchState trenchState = new TrenchState(manager);
        SeabedState seabedState = new SeabedState(manager);
        WorkshopState workshopState = new WorkshopState(manager);
        MiniGameState minigameState = new MiniGameState(manager);
        GameOverState gameOverState = new GameOverState(manager);

        centerArea.getChildren().addAll(
                surfaceState,
                trenchState,
                seabedState,
                workshopState,
                minigameState,
                gameOverState
        );

        this.setCenter(centerArea);
    }

    private void registerHandlers() {
        this.setOnMouseClicked(e -> this.requestFocus());

        this.setOnKeyPressed(event -> {
            boolean handled = false;

            switch (event.getCode()) {
                case UP: case W:
                    manager.navigate(Directions.UP);
                    handled = true;
                    break;
                case DOWN: case S:
                    manager.navigate(Directions.DOWN);
                    handled = true;
                    break;
                case LEFT: case A:
                    manager.navigate(Directions.LEFT);
                    handled = true;
                    break;
                case RIGHT: case D:
                    manager.navigate(Directions.RIGHT);
                    handled = true;
                    break;
                case ENTER:
                    manager.startDescent();
                    handled = true;
                    break;
                case BACK_SPACE:
                    manager.startAscent();
                    handled = true;
                    break;
                case TAB:
                    manager.openWorkshop();
                    handled = true;
                    break;
                case ESCAPE:
                    manager.closeWorkshop();
                    handled = true;
                    break;
                case SPACE:
                    manager.collectItem();
                    handled = true;
                    break;
                case Y:
                    if (manager.isAwaitingConfirmation()) {
                        manager.confirmCollection(true);
                    }
                    handled = true;
                    break;
                case N:
                    if (manager.isAwaitingConfirmation()) {
                        manager.confirmCollection(false);
                    }
                    handled = true;
                    break;
            }

            if (handled) {
                event.consume();
                this.requestFocus();
            }
        });
    }
}