package pt.isec.pa.deepsea.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import pt.isec.pa.deepsea.model.Manager;

public class SidebarUI extends VBox {
    private final Manager manager;
    private final Label shipFuelLabel = new Label();
    private final Label oreLabel = new Label();
    private final Label artifactLabel = new Label();
    private final Label droneHpLabel = new Label();
    private final ProgressBar droneHpBar = new ProgressBar();
    private final Label droneFuelLabel = new Label();
    private final ProgressBar droneFuelBar = new ProgressBar();

    private final Label controlsLabel = new Label();

    public SidebarUI(Manager manager) {
        this.manager = manager;
        this.setPadding(new Insets(15));
        this.setSpacing(15);
        this.setPrefWidth(220);
        this.setStyle("-fx-background-color: #1e1e24;");
        createViews();
        registerListeners();
        update();
    }

    private void createViews() {
        shipFuelLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        oreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        artifactLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        droneHpLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        droneFuelLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        controlsLabel.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 20 0 0 0;");

        droneHpBar.setPrefWidth(200);
        droneFuelBar.setPrefWidth(200);

        this.getChildren().addAll(
                shipFuelLabel, oreLabel, artifactLabel,
                droneHpLabel, droneHpBar,
                droneFuelLabel, droneFuelBar,
                controlsLabel
        );
    }

    private void registerListeners() {
        manager.addClient("data", evt -> update());
        manager.addClient("state", evt -> update());
    }

    private void update() {
        shipFuelLabel.setText("Ship Fuel: " + manager.getShipFuel());
        oreLabel.setText("Ores Collected: " + manager.getOreCount());
        artifactLabel.setText("Artifacts: " + manager.getArtifactCount() + " / 5");
        droneHpLabel.setText(manager.getActiveDroneName() + " HP:");

        double maxHp = manager.getActiveDroneMaxHP();
        if (maxHp > 0) {
            droneHpBar.setProgress(manager.getActiveDroneHP() / maxHp);
        } else {
            droneHpBar.setProgress(0);
        }

        droneFuelLabel.setText("Drone Fuel:");
        double maxFuel = manager.getActiveDroneMaxFuel();
        if (maxFuel > 0) {
            droneFuelBar.setProgress(manager.getActiveDroneFuel() / maxFuel);
        } else {
            droneFuelBar.setProgress(0);
        }

        if (manager.getState() != null) {
            switch (manager.getState()) {
                case SURFACE:
                    controlsLabel.setText("CONTROLS:\n[WSAD] Move Ship\n[ENTER] Dive\n[TAB] Workshop");
                    break;
                case SEABED:
                    controlsLabel.setText("CONTROLS:\n[WSAD] Move Drone\n[SPACE] Collect Item\n[BACKSPACE] Ascend");
                    break;
                case WORKSHOP:
                    controlsLabel.setText("CONTROLS:\n[ESC] Close Workshop\nUse Mouse to repair");
                    break;
                case MINIGAME:
                    controlsLabel.setText("CONTROLS:\n[Y] Accept\n[N] Decline");
                    break;
                case TRENCH:
                    controlsLabel.setText("CONTROLS:\n[WSAD] Move Drone\n");
                    break;
                case GAME_OVER:
                    controlsLabel.setText("Game Over.");
                    break;
            }
        }
    }
}