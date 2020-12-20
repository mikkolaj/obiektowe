package agh.cs.lab1;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class MapVisualizer extends StackPane implements IFieldChangeObserver {
    private final int tileSize;
    private final int mapWidth;
    private final int mapHeight;
    private final SimulationStatistics stats;
    private final MapTile[][] grid;
    private final GrassField map;
    private final ControlPanel controlPanel;
    private final SimulationEngine simulationEngine;
    private final int initialEnergy;

    private void createContent() {
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                MapTile tile = new MapTile(x, this.mapHeight - 1 - y, this.tileSize);

                this.grid[y][x] = tile;
                int finalX = x;
                int finalY = y;
                this.grid[y][x].setOnMouseClicked(e -> onClick(finalX, finalY));
                this.getChildren().add(tile);
            }
        }
    }

    public MapVisualizer(GrassField map, SimulationEngine simulationEngine, ControlPanel controlPanel,
                         SimulationStatistics stats, int width, int height, int initialEnergy, int tileSize) {
        this.grid = new MapTile[height][width];
        this.simulationEngine = simulationEngine;
        this.stats = stats;
        this.controlPanel = controlPanel;
        this.mapHeight = height;
        this.mapWidth = width;
        this.map = map;
        this.map.addObserver(this);
        this.initialEnergy = initialEnergy;
        this.tileSize = tileSize;
        this.createContent();
    }

    public void fieldChanged(Vector2d position) {
        grid[position.y][position.x].setCorrectBackground(this.map.objectAt(position), this.initialEnergy);
    }

    private void onClick(int x, int y) {
        if (this.simulationEngine.isSimulationPaused() && this.stats.isObservationFinished()) {
            Object objectAt = this.map.objectAt(new Vector2d(x, y));
            if (objectAt instanceof Animal) {
                this.controlPanel.animalSelected((Animal) objectAt);
            }
        }
    }

    public void setColor(Vector2d position, Color color) {
        this.grid[position.y][position.x].setGivenBackground(color);
    }

}
