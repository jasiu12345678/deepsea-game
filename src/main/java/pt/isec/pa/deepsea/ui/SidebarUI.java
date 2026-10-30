package pt.isec.pa.deepsea.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pt.isec.pa.deepsea.model.Manager;
import pt.isec.pa.deepsea.model.State;

/**
 * Read-only side panel with ship and drone information and the controls that
 * apply to the current game state. It only displays what the {@link Manager} reports.
 */
public class SidebarUI extends VBox {
    private final Manager manager;
    private final Label shipFuel = value();
    private final Label ore = value();
    private final Label artifacts = value();
    private final Label droneName = new Label();
    private final Label droneHull = value();
    private final Label droneFuel = value();
    private final ProgressBar hullBar = bar(false);
    private final ProgressBar fuelBar = bar(true);
    private final VBox controls = new VBox(8);

    public SidebarUI(Manager manager) {
        this.manager = manager;
        getStyleClass().add("sidebar");
        setPadding(new Insets(22, 20, 20, 20));
        setSpacing(10);
        setPrefWidth(230);
        setMinWidth(230);
        createViews();
        registerListeners();
        update();
    }

    private static Label value() {
        Label label = new Label();
        label.getStyleClass().add("stat-value");
        return label;
    }

    private static ProgressBar bar(boolean fuel) {
        ProgressBar bar = new ProgressBar(0);
        bar.setMaxWidth(Double.MAX_VALUE);
        bar.setPrefHeight(8);
        if (fuel) bar.getStyleClass().add("fuel");
        return bar;
    }

    private static Label section(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-title");
        return label;
    }

    private static HBox row(String name, Label value) {
        Label label = new Label(name);
        label.getStyleClass().add("stat-name");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox box = new HBox(label, spacer, value);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private void createViews() {
        Label title = new Label("DEEP SEA MINING");
        title.getStyleClass().add("app-title");
        droneName.getStyleClass().add("drone-name");

        getChildren().addAll(
                title,
                new Separator(),
                section("SHIP"),
                row("Fuel", shipFuel),
                row("Ore", ore),
                row("Artifacts", artifacts),
                section("ACTIVE DRONE"),
                droneName,
                row("Hull", droneHull), hullBar,
                row("Fuel", droneFuel), fuelBar,
                section("CONTROLS"),
                controls
        );
    }

    private void registerListeners() {
        manager.addClient(Manager.DATA, evt -> update());
        manager.addClient(Manager.STATE, evt -> update());
    }

    private void update() {
        shipFuel.setText(String.valueOf(manager.getShipFuel()));
        ore.setText(String.valueOf(manager.getOreCount()));
        artifacts.setText(manager.getArtifactCount() + " / " + manager.getTotalArtifacts());

        droneName.setText(manager.hasActiveDrone() ? manager.getActiveDroneName() : "No drone deployed");
        droneHull.setText(manager.getActiveDroneHP() + " / " + manager.getActiveDroneMaxHP());
        droneFuel.setText(manager.getActiveDroneFuel() + " / " + manager.getActiveDroneMaxFuel());
        hullBar.setProgress(ratio(manager.getActiveDroneHP(), manager.getActiveDroneMaxHP()));
        fuelBar.setProgress(ratio(manager.getActiveDroneFuel(), manager.getActiveDroneMaxFuel()));

        updateControls();
    }

    private static double ratio(int value, int max) {
        return max > 0 ? (double) value / max : 0;
    }

    private void updateControls() {
        controls.getChildren().clear();
        State state = manager.getState();
        if (state == null) return;

        switch (state) {
            case SURFACE -> add("W A S D", "Move ship", "Enter", "Dive", "Tab", "Workshop");
            case TRENCH -> add("W A S D", "Move drone");
            case SEABED -> add("W A S D", "Move drone", "Space", "Collect", "Backspace", "Ascend");
            case WORKSHOP -> add("Esc", "Leave workshop");
            case MINIGAME -> add("W A S D", "Slide tiles");
            case GAME_OVER -> add("Game > New", "Play again");
        }
    }

    private void add(String... pairs) {
        for (int i = 0; i < pairs.length; i += 2) {
            Label chip = new Label(pairs[i]);
            chip.getStyleClass().add("key-chip");
            Label desc = new Label(pairs[i + 1]);
            desc.getStyleClass().add("key-desc");
            HBox line = new HBox(10, chip, desc);
            line.setAlignment(Pos.CENTER_LEFT);
            controls.getChildren().add(line);
        }
    }
}
