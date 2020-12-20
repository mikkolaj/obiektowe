package agh.cs.lab1;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ControlPanel extends StackPane {
    private final int height;
    private final SimulationEngine simulationEngine;
    private MapVisualizer mapVisualizer;
    private final Text genotypeListing;
    private final SimulationStatistics stats;
    private final Text grassCountLabel;
    private final Text animalCountLabel;
    private final Text meanEnergy;
    private final Text meanNumberOfChildren;
    private final Text meanDaysLived;
    private Animal selectedAnimal;
    private final Text selectedAnimalLabel;
    private final Text selectedAnimalsGenotype;
    private final Button observe;
    private final Button markAnimals;
    private List<Integer> currentLeadingGenotype;
    private final Text numberOfChildren;
    private final Text numberOfDescendants;
    private final Text isDead;
    private boolean observationInProgress = false;

    public ControlPanel(SimulationEngine simulationEngine, SimulationStatistics stats, int width, int height, int baseX, int baseY) {
        Button pause = new Button("Pause");
        Button start = new Button("Start");
        this.simulationEngine = simulationEngine;
        this.stats = stats;
        this.setPrefSize(width, height);
        this.height = height;

        pause.setTranslateX(160 - width / 2);
        pause.setTranslateY(30 - height / 2);
        pause.setPrefSize(60, 20);
        pause.setOnMouseClicked(e -> this.pauseSimulation());

        start.setTranslateX(240 - width / 2);
        start.setTranslateY(30 - height / 2);
        start.setPrefSize(60, 20);
        start.setOnMouseClicked(e -> this.resumeSimulation());

        Text genotypeLabel = new Text();
        genotypeLabel.setText("The current leading genotype is:");
        genotypeLabel.setTranslateY(60 - height / 2);

        this.genotypeListing = new Text();
        this.genotypeListing.setTranslateY(80 - height / 2);


        this.markAnimals = new Button("Show animals with this genotype");
        this.markAnimals.setTranslateY(110 - height / 2);
        this.markAnimals.setVisible(false);
        this.markAnimals.setOnMouseClicked(e -> this.markAnimals());

        this.animalCountLabel = new Text();
        this.animalCountLabel.setText("Number of animals on the map:");
        this.animalCountLabel.setTranslateY(100 - height / 2);

        this.grassCountLabel = new Text();
        this.grassCountLabel.setText("Number of grass patches on the map:");
        this.grassCountLabel.setTranslateY(120 - height / 2);

        this.meanEnergy = new Text();
        this.meanEnergy.setText("Mean energy of animals:");
        this.meanEnergy.setTranslateY(140 - height / 2);

        this.meanNumberOfChildren = new Text();
        this.meanNumberOfChildren.setText("Mean number of children:");
        this.meanNumberOfChildren.setTranslateY(160 - height / 2);

        this.meanDaysLived = new Text();
        this.meanDaysLived.setText("No dead animals yet.");
        this.meanDaysLived.setTranslateY(180 - height / 2);

        this.selectedAnimalLabel = new Text();
        this.selectedAnimalLabel.setText("Selected animal's genotype is:");
        this.selectedAnimalLabel.setTranslateY(240 - height / 2);
        this.selectedAnimalLabel.setVisible(false);

        this.selectedAnimalsGenotype = new Text();
        this.selectedAnimalsGenotype.setTranslateY(260 - height / 2);
        this.selectedAnimalsGenotype.setVisible(false);

        this.observe = new Button("Observe it");
        this.observe.setVisible(false);
        this.observe.setTranslateY(290 - height / 2);
        this.observe.setOnMouseClicked(e -> this.observeAnimal());

        this.numberOfChildren = new Text();
        this.numberOfChildren.setText("Number of children: 0");
        this.numberOfChildren.setTranslateY(280 - height / 2);
        this.numberOfChildren.setVisible(false);

        this.numberOfDescendants = new Text();
        this.numberOfDescendants.setText("Number of descendants: 0");
        this.numberOfDescendants.setTranslateY(300 - height / 2);
        this.numberOfDescendants.setVisible(false);

        this.isDead = new Text();
        this.isDead.setText("Animal is still alive.");
        this.isDead.setTranslateY(320 - height / 2);
        this.isDead.setVisible(false);

        Button saveStats = new Button("Save simulation statistics");
        saveStats.setTranslateY(height / 2 - 30);
        saveStats.setOnMouseClicked(e -> this.onStatsSave());

        this.getChildren().addAll(pause, start, genotypeLabel,
                this.genotypeListing, markAnimals, this.animalCountLabel, this.grassCountLabel,
                this.meanEnergy, this.meanNumberOfChildren, this.meanDaysLived,
                this.selectedAnimalLabel, this.selectedAnimalsGenotype, this.observe,
                this.numberOfChildren, this.numberOfDescendants, this.isDead, saveStats);
        this.setBackground(new Background(new BackgroundFill(Color.LAVENDER, null, null)));

        this.setTranslateX(baseX);
        this.setTranslateY(baseY);
    }

    private void onStatsSave() {
        this.pauseSimulation();
        String path = ".\\data\\finalStats.txt";
        this.stats.saveStats(path);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Stats saved");
        alert.setHeaderText(null);
        alert.setContentText("Statistics saved to: \n" + System.getProperty("user.dir") + path.substring(1));
        alert.showAndWait();
    }

    private void observeAnimal() {
        TextInputDialog dialog = new TextInputDialog("10");
        dialog.setTitle("Observe an animal");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter number of rounds to observe the animal for:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            try {
                this.stats.startObservation(this.selectedAnimal, Integer.parseInt(s));
                this.observe.setVisible(false);
                this.numberOfChildren.setVisible(true);
                this.numberOfDescendants.setVisible(true);
                this.isDead.setVisible(true);
                this.observationInProgress = true;
            } catch (NumberFormatException ex) {
                inputAlert();
            }
        });
    }

    private void inputAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Number of days to observe the animal for must be an integer!");

        alert.showAndWait();
    }

    private void observationEnded() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Observation has ended");
        alert.setHeaderText(null);
        alert.setContentText(this.numberOfChildren.getText() + "\n" + this.numberOfDescendants.getText() + "\n" +
                this.isDead.getText());
        alert.showAndWait();
    }

    private void markAnimals() {
        this.simulationEngine.getAllAnimals().forEach(animal -> {
            if (animal.getGenes().equals(this.currentLeadingGenotype)) {
                this.mapVisualizer.setColor(animal.getPosition(), Color.BLUEVIOLET);
            }
        });
    }

    private void pauseSimulation() {
        if (this.mapVisualizer != null) {
            this.shiftElements(140);
            this.markAnimals.setVisible(true);
        }
        this.simulationEngine.pauseSimulation();
    }

    private void shiftElements(int base) {
        this.animalCountLabel.setTranslateY(base - this.height / 2);
        this.grassCountLabel.setTranslateY(base + 20 - this.height / 2);
        this.meanEnergy.setTranslateY(base + 40 - this.height / 2);
        this.meanNumberOfChildren.setTranslateY(base + 60 - this.height / 2);
        this.meanDaysLived.setTranslateY(base + 80 - this.height / 2);
        this.selectedAnimalLabel.setTranslateY(base + 100 - this.height / 2);
        this.selectedAnimalsGenotype.setTranslateY(base + 120 - this.height / 2);
        this.numberOfChildren.setTranslateY(base + 140 - this.height / 2);
        this.numberOfDescendants.setTranslateY(base + 160 - this.height / 2);
        this.isDead.setTranslateY(base + 180 - this.height / 2);
    }

    private void resumeSimulation() {
        if (this.mapVisualizer != null) {
            this.markAnimals.setVisible(false);
            this.simulationEngine.getAllAnimals().forEach(animal -> {
                if (animal.getGenes().equals(this.currentLeadingGenotype)) {
                    this.mapVisualizer.fieldChanged(animal.getPosition());
                }
            });
            if (!this.observationInProgress) {
                this.selectedAnimalLabel.setVisible(false);
                this.selectedAnimalsGenotype.setVisible(false);
            }
            this.observe.setVisible(false);
            this.shiftElements(100);
        }
        this.simulationEngine.resumeSimulation();
    }

    public void updateStats() {
        this.updateLeadingGenotype();
        this.updateObjectCounts();
        if (this.observationInProgress)
            this.updateObservation();
        if (this.observationInProgress && this.stats.isObservationFinished()) {
            this.resetObservation();
        }
    }

    private void resetObservation() {
        this.pauseSimulation();
        this.selectedAnimalLabel.setVisible(false);
        this.selectedAnimalsGenotype.setVisible(false);
        this.numberOfChildren.setVisible(false);
        this.numberOfDescendants.setVisible(false);
        this.isDead.setVisible(false);
        this.observationInProgress = false;
        this.selectedAnimal = null;
        this.stats.resetObservation();
        Platform.runLater(this::observationEnded);
    }

    private void updateObservation() {
        this.numberOfChildren.setText("Number of children: " + this.stats.getNumberOfObservedChildren());
        this.numberOfDescendants.setText("Number of descendants: " + this.stats.getNumberOfObservedDescendants());

        int daysLived = this.stats.getDayOfDeath();
        if (daysLived != -1) {
            this.isDead.setText("Animal died after " + daysLived + " days.");
        }
    }

    private void updateLeadingGenotype() {
        this.currentLeadingGenotype = this.stats.getCurrentLeadingGenotype();
        String genotype = "[" + this.currentLeadingGenotype.stream().map(Object::toString)
                .collect(Collectors.joining(" ")) + "]";
        this.genotypeListing.setText(genotype);
    }

    private void updateObjectCounts() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        this.animalCountLabel.setText("Number of animals on the map: " + this.stats.getCurrentNumberOfAnimals());
        this.grassCountLabel.setText("Number of grass patches on the map: " + this.stats.getCurrentNumberOfGrassPatches());
        this.meanEnergy.setText("Mean energy of animals: " + df.format(this.stats.getMeanEnergy()));
        this.meanNumberOfChildren.setText("Mean number of children: " + df.format(this.stats.getMeanNumberOfChildren()));
        double meanDaysLived = this.stats.getMeanDaysLived();
        if (meanDaysLived != 0) {
            this.meanDaysLived.setText("Mean days lived of dead animals: " + df.format(meanDaysLived));
        }
    }

    public void animalSelected(Animal animal) {
        this.selectedAnimal = animal;
        String genotype = "[" + animal.getGenes().stream().map(Object::toString)
                .collect(Collectors.joining(" ")) + "]";
        this.selectedAnimalsGenotype.setText(genotype);
        this.selectedAnimalLabel.setVisible(true);
        this.selectedAnimalsGenotype.setVisible(true);
        this.observe.setVisible(true);
    }

    public void addMapVisualizer(MapVisualizer mapVisualizer) {
        this.mapVisualizer = mapVisualizer;
    }
}
