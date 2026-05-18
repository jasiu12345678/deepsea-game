package pt.isec.pa.deepsea.ui.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.data.DroneInfo;

public class WorkshopState extends BorderPane {
    private final Manager manager;
    private ListView<DroneInfo> droneListView;
    private Button btnSelect, btnRepair, btnRefuel, btnUpgrade, btnExit;

    public WorkshopState(Manager manager) {
        this.manager = manager;
        createViews();
        registerListeners();
        updateVisibility();
    }

    private void createViews() {
        this.setStyle("-fx-background-color: #1a252c;");
        this.setPadding(new Insets(30));

        Label title = new Label("W O R K S H O P");
        title.setStyle("-fx-text-fill: #f39c12; -fx-font-size: 32px; -fx-font-weight: bold;");
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(0, 0, 20, 0));
        this.setTop(title);

        droneListView = new ListView<>();
        droneListView.setStyle("-fx-control-inner-background: #2c3e50; -fx-text-fill: white; -fx-font-size: 16px;");
        this.setCenter(droneListView);

        VBox actionBox = new VBox(15);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setPadding(new Insets(0, 0, 0, 30));

        btnSelect = createStyledButton("Select as Active", "#2980b9");
        btnRepair = createStyledButton("Repair Drone", "#27ae60");
        btnRefuel = createStyledButton("Refuel Drone", "#8e44ad");
        btnUpgrade = createStyledButton("Upgrade Hull", "#d35400");

        btnExit = createStyledButton("Exit Workshop", "#c0392b");
        VBox.setMargin(btnExit, new Insets(50, 0, 0, 0));

        actionBox.getChildren().addAll(btnSelect, btnRepair, btnRefuel, btnUpgrade, btnExit);
        this.setRight(actionBox);
    }

    private Button createStyledButton(String text, String colorHex) {
        Button btn = new Button(text);
        btn.setPrefWidth(200);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
        return btn;
    }

    private void registerListeners() {
        manager.addClient("data", evt -> update());
        manager.addClient("state", evt -> updateVisibility());

        btnSelect.setOnAction(e -> {
            DroneInfo selected = droneListView.getSelectionModel().getSelectedItem();
            if (selected != null) manager.selectDrone(selected.id());
        });

        btnRepair.setOnAction(e -> {
            DroneInfo selected = droneListView.getSelectionModel().getSelectedItem();
            if (selected != null) manager.repairDrone(selected.id());
        });

        btnRefuel.setOnAction(e -> {
            DroneInfo selected = droneListView.getSelectionModel().getSelectedItem();
            if (selected != null) manager.refuelDrone(selected.id());
        });

        btnUpgrade.setOnAction(e -> {
            DroneInfo selected = droneListView.getSelectionModel().getSelectedItem();
            if (selected != null) manager.upgradeDrone(selected.id());
        });

        btnExit.setOnAction(e -> manager.closeWorkshop());
    }

    private void updateVisibility() {
        boolean isWorkshop = (manager.getState() != null && manager.getState().name().equals("WORKSHOP"));
        this.setVisible(isWorkshop);

        if (isWorkshop) {
            update();
        }
    }

    private void update() {
        if (!this.isVisible()) return;

        int selectedIndex = droneListView.getSelectionModel().getSelectedIndex();

        droneListView.getItems().clear();

        var drones = manager.getDronesSortedByFuel();
        if (drones != null) {
            droneListView.getItems().addAll(drones);
        }

        if (selectedIndex >= 0 && selectedIndex < droneListView.getItems().size()) {
            droneListView.getSelectionModel().select(selectedIndex);
        }
    }
}