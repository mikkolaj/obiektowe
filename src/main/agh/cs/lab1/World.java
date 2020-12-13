package agh.cs.lab1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class World extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            GrassField map = new GrassField(10, 10, 0.5,  1);
            Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(3, 4), new Vector2d(3, 3), new Vector2d(2, 4)};
            IEngine engine = new SimulationEngine(primaryStage, 100, map, positions, 100);
            engine.run();
//            Random generator = new Random();
//            generator.nextInt(0);

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            System.out.println(ex);
            System.exit(1);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
