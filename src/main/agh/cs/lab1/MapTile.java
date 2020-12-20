package agh.cs.lab1;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MapTile extends StackPane {
    private final Rectangle border;

    public MapTile(int x, int y, int tileSize) {
        this.border = new Rectangle(tileSize, tileSize);

        this.border.setStroke(Color.LIGHTGRAY);
        this.border.setFill(Color.LAVENDER);
        this.getChildren().add(border);
        this.setBackground(new Background(new BackgroundFill(Color.LAVENDER, null, null)));

        setTranslateX(x * tileSize);
        setTranslateY(y * tileSize);
    }

    public void setCorrectBackground(Object mapObject, int initialEnergy) {
        if (mapObject == null) {
            this.border.setFill(Color.LAVENDER);
            return;
        }

        // set background color relative to the energy of an animal, the darker it is
        // the closer the animal is to death
        if (mapObject instanceof Animal) {
            double relativeEnergy = 1.0 * ((Animal) mapObject).getEnergy() / initialEnergy;
            relativeEnergy = Math.min(relativeEnergy, 1.0);
            this.border.setFill(Color.color(relativeEnergy, 166.0 * relativeEnergy / 255.0, 0));
        } else {
            // grass is green
            this.border.setFill(Color.GREEN);
        }

    }

    public void setGivenBackground(Color color) {
        this.border.setFill(color);
    }


}

