package pt.isec.pa.deepsea.ui.states;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.State;
import pt.isec.pa.deepsea.model.data.DroneInfo;

/**
 * Workshop view: lists the available drones and lets the player pick, repair,
 * refuel or upgrade them. Every action is delegated to the {@link Manager}.
 */
public class WorkshopState extends BorderPane {
    private final Manager manager;
    private ListView<DroneInfo> droneListView;
    private Button btnSelect;
    private Button btnRepair;
    private Button btnRefuel;
    private Button btnUpgrade;
    private Button btnExit;

    public WorkshopState(Manager manager) {
        this.manager = manager;
        createViews();
        registerListeners();
        registerHandlers();
        updateVisibility();
    }

    private void registerHandlers() {
        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                manager.closeWorkshop();
                event.consume();
            }
        });
    }

    private void createViews() {
        getStyleClass().add("workshop");
        setPadding(new Insets(36, 40, 36, 40));

        Label title = new Label("Workshop");
        title.getStyleClass().add("workshop-title");
        Label subtitle = new Label("Select a drone, then repair, refuel or upgrade it.");
        subtitle.getStyleClass().add("workshop-subtitle");
        VBox header = new VBox(4, title, subtitle);
        header.setPadding(new Insets(0, 0, 22, 0));
        setTop(header);

        droneListView = new ListView<>();
        droneListView.setPlaceholder(new Label("No drones available"));
        droneListView.setFixedCellSize(-1);
        droneListView.setCellFactory(lv -> new DroneCell());
        setCenter(droneListView);

        btnSelect = button("Use as active drone", "primary");
        btnRepair = button("Repair  (5 ship fuel)", null);
        btnRefuel = button("Refuel  (10 ship fuel)", null);
        btnUpgrade = button("Upgrade hull  (10 ore)", null);
        btnExit = button("Back to surface", "quiet");

        VBox actions = new VBox(12, btnSelect, btnRepair, btnRefuel, btnUpgrade, spacer(), btnExit);
        actions.setPadding(new Insets(0, 0, 0, 28));
        actions.setPrefWidth(260);
        setRight(actions);
    }

    private static Region spacer() {
        Region region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);
        return region;
    }

    private static Button button(String text, String styleClass) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        if (styleClass != null) button.getStyleClass().add(styleClass);
        return button;
    }

    private void registerListeners() {
        manager.addClient(Manager.DATA, evt -> update());
        manager.addClient(Manager.STATE, evt -> updateVisibility());

        btnSelect.setOnAction(e -> withSelected(manager::selectDrone));
        btnRepair.setOnAction(e -> withSelected(manager::repairDrone));
        btnRefuel.setOnAction(e -> withSelected(manager::refuelDrone));
        btnUpgrade.setOnAction(e -> withSelected(manager::upgradeDrone));
        btnExit.setOnAction(e -> manager.closeWorkshop());
    }

    private void withSelected(java.util.function.IntPredicate action) {
        DroneInfo selected = droneListView.getSelectionModel().getSelectedItem();
        if (selected != null) action.test(selected.id());
    }

    private void updateVisibility() {
        boolean isWorkshop = manager.getState() == State.WORKSHOP;
        setVisible(isWorkshop);
        if (isWorkshop) {
            Platform.runLater(this::requestFocus);
            update();
        }
    }

    private void update() {
        if (!isVisible()) return;

        int selectedIndex = droneListView.getSelectionModel().getSelectedIndex();
        droneListView.getItems().setAll(manager.getDronesSortedByFuel());
        if (selectedIndex >= 0 && selectedIndex < droneListView.getItems().size()) {
            droneListView.getSelectionModel().select(selectedIndex);
        }
    }

    private static class DroneCell extends ListCell<DroneInfo> {
        @Override
        protected void updateItem(DroneInfo drone, boolean empty) {
            super.updateItem(drone, empty);
            if (empty || drone == null) {
                setGraphic(null);
                return;
            }
            Label name = new Label("Drone #" + drone.id());
            name.getStyleClass().add("drone-name");
            name.setPrefWidth(80);

            HBox box = new HBox(20, name,
                    meter("Hull", drone.hp(), drone.maxHp(), false),
                    meter("Fuel", drone.fuel(), drone.maxFuel(), true));
            box.setAlignment(Pos.CENTER_LEFT);
            setGraphic(box);
        }

        private static VBox meter(String label, int value, int max, boolean fuel) {
            Label name = new Label(label);
            name.getStyleClass().add("stat-name");
            Label amount = new Label(value + " / " + max);
            amount.getStyleClass().add("stat-value");
            Region gap = new Region();
            HBox.setHgrow(gap, Priority.ALWAYS);

            ProgressBar bar = new ProgressBar(max > 0 ? (double) value / max : 0);
            bar.setMaxWidth(Double.MAX_VALUE);
            bar.setPrefHeight(8);
            if (fuel) bar.getStyleClass().add("fuel");

            VBox box = new VBox(6, new HBox(name, gap, amount), bar);
            box.setPrefWidth(150);
            return box;
        }
    }
}
