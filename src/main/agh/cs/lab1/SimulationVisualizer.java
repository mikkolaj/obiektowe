package agh.cs.lab1;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SimulationVisualizer {
    private final ControlPanel controlPanel;
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    private static final int STATS_WIDTH = 400;

    public SimulationVisualizer(Stage primaryStage, SimulationEngine simulationEngine, SimulationStatistics stats,
                                GrassField map, int width, int height, int initialEnergy) {
        Pane root = new Pane();
        root.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        int tileSize = this.calculateTileSize(width, height);

        if (tileSize >= 3) {
            this.controlPanel = new ControlPanel(simulationEngine, stats, STATS_WIDTH,
                    Math.max(height * tileSize + 1, 370), width * tileSize + 1, 0);
            MapVisualizer mapVisualizer = new MapVisualizer(map, simulationEngine, controlPanel,
                    stats, width, height, initialEnergy, tileSize);
            this.controlPanel.addMapVisualizer(mapVisualizer);
            root.getChildren().addAll(mapVisualizer, this.controlPanel);
            primaryStage.setScene(new Scene(root, width * tileSize + STATS_WIDTH, Math.max(height * tileSize, 370)));
        } else {
            // map is illegible, only stats and a message will be displayed
            this.controlPanel = new ControlPanel(simulationEngine, stats, STATS_WIDTH,
                    WINDOW_HEIGHT, WINDOW_WIDTH - STATS_WIDTH, 0);
            StackPane pane = new StackPane();
            Text text = new Text();
            text.setText("The map is too big to be displayed");
            pane.setPrefSize(WINDOW_WIDTH - STATS_WIDTH, WINDOW_HEIGHT);
            pane.getChildren().add(text);
            root.getChildren().addAll(pane, this.controlPanel);
            primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        }

        primaryStage.setTitle("Simulation");
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }

    private int calculateTileSize(int width, int height) {
        return Math.min((WINDOW_WIDTH - STATS_WIDTH) / width, (WINDOW_HEIGHT) / height);
    }

    public void updateVisualizer() {
        this.controlPanel.updateStats();
    }
}
