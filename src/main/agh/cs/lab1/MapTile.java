package agh.cs.lab1;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;


public class MapTile extends StackPane {
    private Rectangle border;
    private Text textBox = new Text();

    public MapTile(int x, int y, int tile_size) {
        this.border = new Rectangle(tile_size - 2, tile_size - 2);

        this.border.setStroke(Color.LIGHTGRAY);
        this.border.setFill(Color.LAVENDER);
        this.textBox.setFill(Color.LAVENDER);
        this.getChildren().addAll(border, textBox);
        this.setBackground(new Background(new BackgroundFill(Color.LAVENDER, null, null)));

        setTranslateX(x * tile_size);
        setTranslateY(y * tile_size);
    }

    public void setText(String text) {
        this.textBox.setText(text);
    }

    public void setBckg(Object mapObject) {
        if (mapObject == null) {
            this.border.setFill(Color.LAVENDER);
            return;
        }
        String urlString;
        if (mapObject instanceof Animal) {
            urlString = "./icons/" + ((Animal) mapObject).getOrientation().toFilename() + ".png";
        } else {
            urlString = "./icons/grass.png";
        }
        try {
//            System.out.println(urlString);
            URL url = getClass().getResource(urlString);
            String currentDirectory = System.getProperty("user.dir");
      System.out.println("The current working directory is " + currentDirectory);
            this.border.setFill(new ImagePattern(new Image(new FileInputStream(url.getPath()))));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }

    }
}

