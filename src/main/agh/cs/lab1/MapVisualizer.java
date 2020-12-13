package agh.cs.lab1;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class MapVisualizer implements IFieldChangeObserver {
    private static final int TILE_SIZE = 40;
    private static final int windowWidth = 800;
    private static final int windowHeight = 600;
    private final int mapWidth;
    private final int mapHeight;
    private final MapTile[][] grid;
    private final GrassField map;

    public Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(windowWidth, windowHeight);

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                MapTile tile = new MapTile(x, this.mapHeight - 1 - y, TILE_SIZE);

                grid[y][x] = tile;
                root.getChildren().add(tile);
            }
        }

        return root;
    }

    public MapVisualizer(GrassField map, int width, int height) {
        this.grid = new MapTile[height][width];
        this.mapHeight = height;
        this.mapWidth = width;
        this.map = map;
        this.map.addObserver(this);
    }

    public void fieldChanged(Vector2d position) {
        grid[position.y][position.x].setBckg(this.map.objectAt(position));
    }
}
