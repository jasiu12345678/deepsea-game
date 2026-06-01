package pt.isec.pa.deepsea.ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.ui.states.*;

public class RootPane extends BorderPane {
    private final Manager manager;

    public RootPane(Manager manager) {
        this.manager = manager;
        createViews();
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
}