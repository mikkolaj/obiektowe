package agh.cs.lab1;

import javafx.application.Application;
import javafx.stage.Stage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class World extends Application {

    @Override
    public void start(Stage primaryStage) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader("./data/parameters.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            runSimulation(primaryStage, jsonObject);

            for (int i = 0; i < Math.toIntExact((long) jsonObject.get("numberOfSimulations")) - 1; i++) {
                Stage newStage = new Stage();
                runSimulation(newStage, jsonObject);
            }
        } catch (IllegalArgumentException | IOException | ParseException ex) {
            ex.printStackTrace();
            System.out.println(ex);
            System.exit(1);
        }
    }

    private void runSimulation(Stage primaryStage, JSONObject jsonObject) {
        GrassField map = new GrassField(Math.toIntExact((long) jsonObject.get("width")), Math.toIntExact((long) jsonObject.get("height")),
                (double) jsonObject.get("jungleRatio"), Math.toIntExact((long) jsonObject.get("moveEnergy")));

        IEngine engine = new SimulationEngine(primaryStage, map, Math.toIntExact((long) jsonObject.get("numberOfAnimals")),
                Math.toIntExact((long) jsonObject.get("startEnergy")), Math.toIntExact((long) jsonObject.get("plantEnergy")),
                Math.toIntExact((long) jsonObject.get("moveDelay")));
        engine.run();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
