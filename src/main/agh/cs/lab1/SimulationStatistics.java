package agh.cs.lab1;

import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SimulationStatistics {
    private final SimulationEngine simulationEngine;
    private final GrassField map;
    private int totalDaysLived;
    private int deadAnimals = 0;
    private final List<Integer> meanAnimalCounts = new ArrayList<>();
    private final List<Integer> meanGrassCounts = new ArrayList<>();
    private final List<Double> meanEnergyLevels = new ArrayList<>();
    private final List<Double> meanNumberOfChildren = new ArrayList<>();
    private final Map<List<Integer>, Integer> currentLeadingGenotypes = new LinkedHashMap<>();
    private final Map<List<Integer>, Integer> geneOccurences = new LinkedHashMap<>();
    private AnimalObserver animalObserver = null;

    public SimulationStatistics(SimulationEngine simulationEngine, GrassField map) {
        this.simulationEngine = simulationEngine;
        this.map = map;
    }

    public double getMeanEnergy() {
        Collection<Animal> animals = this.simulationEngine.getAllAnimals();
        if (animals.size() > 0) {
            int sum = animals.stream().mapToInt(Animal::getEnergy).sum();
            return 1.0 * sum / this.simulationEngine.getAllAnimals().size();
        } else {
            return 0;
        }
    }

    public double getMeanNumberOfChildren() {
        Collection<Animal> animals = this.simulationEngine.getAllAnimals();
        if (animals.size() > 0) {
            int sum = this.simulationEngine.getAllAnimals().stream().mapToInt(Animal::getChildren).sum();
            return 1.0 * sum / this.simulationEngine.getAllAnimals().size();
        } else {
            return 0;
        }
    }

    public int getCurrentNumberOfAnimals() {
        return this.simulationEngine.getAllAnimals().size();
    }

    public int getCurrentNumberOfGrassPatches() {
        return this.map.getNumberOfGrassPatches();
    }

    public List<Integer> getCurrentLeadingGenotype() {
        Optional<Map.Entry<List<Integer>, Integer>> currentLeadingGenotype = this.currentLeadingGenotypes
                .entrySet().stream().max(Map.Entry.comparingByValue());
        if (currentLeadingGenotype.isPresent())
            return currentLeadingGenotype.get().getKey();
        else
            return new ArrayList<>(0);
    }

    private List<Integer> getOverallLeadingGenotype() {
        Optional<Map.Entry<List<Integer>, Integer>> overallLeadingGenotype = this.geneOccurences
                .entrySet().stream().max((obj1, obj2) -> obj2.getValue().compareTo(obj1.getValue()));
        if (overallLeadingGenotype.isPresent())
            return overallLeadingGenotype.get().getKey();
        else
            return new ArrayList<>(0);
    }

    public void animalDied(Animal animal) {
        this.totalDaysLived += animal.getDaysLived();
        this.deadAnimals += 1;
        if (this.animalObserver != null) {
            this.animalObserver.animalDied(animal);
        }
        List<Integer> genotype = animal.getGenes();
        Integer currentGenotypeOccurences = this.currentLeadingGenotypes.get(genotype);
        if (currentGenotypeOccurences == 1) {
            this.currentLeadingGenotypes.remove(genotype, currentGenotypeOccurences);
        } else {
            currentGenotypeOccurences -= 1;
            this.currentLeadingGenotypes.replace(genotype, currentGenotypeOccurences);
        }

    }

    public double getMeanDaysLived() {
        if (this.deadAnimals != 0) {
            return 1.0 * this.totalDaysLived / this.deadAnimals;
        } else {
            return 0;
        }
    }

    public void startObservation(Animal animal, int days) {
        this.animalObserver = new AnimalObserver(animal, days);
    }

    public void animalsReproduced(Animal strongerAnimal, Animal weakerAnimal, Animal child) {
        if (this.animalObserver != null) {
            this.animalObserver.animalsReproduced(strongerAnimal, weakerAnimal, child);
        }

        updateGenes(child);
    }

    public void animalPlaced(Animal animal) {
        updateGenes(animal);
    }

    private void updateGenes(Animal animal) {
        List<Integer> genotype = animal.getGenes();
        Integer currentGenotypeOccurences = this.currentLeadingGenotypes.get(genotype);
        if (currentGenotypeOccurences == null) {
            currentGenotypeOccurences = 1;
            this.currentLeadingGenotypes.put(genotype, currentGenotypeOccurences);
        } else {
            currentGenotypeOccurences += 1;
            this.currentLeadingGenotypes.replace(genotype, currentGenotypeOccurences);
        }

        Integer overallGenotypeOccurences = this.geneOccurences.get(genotype);
        if (overallGenotypeOccurences == null) {
            overallGenotypeOccurences = 1;
            this.geneOccurences.put(genotype, overallGenotypeOccurences);
        } else {
            overallGenotypeOccurences += 1;
            this.geneOccurences.replace(genotype, overallGenotypeOccurences);
        }
    }

    public void dayHasPassed() {
        this.meanEnergyLevels.add(this.getMeanEnergy());
        this.meanAnimalCounts.add(this.simulationEngine.getAllAnimals().size());
        this.meanGrassCounts.add(this.map.getNumberOfGrassPatches());
        this.meanEnergyLevels.add(this.getMeanEnergy());
        this.meanNumberOfChildren.add(this.getMeanNumberOfChildren());
        if (this.animalObserver != null) {
            this.animalObserver.dayHasPassed();
        }

    }

    public boolean isObservationFinished() {
        if (this.animalObserver == null)
            return true;
        else
            return this.animalObserver.isFinished();
    }

    public void resetObservation() {
        this.animalObserver = null;
    }

    public int getNumberOfObservedChildren() {
        return this.animalObserver.getNumberOfChildren();
    }

    public int getNumberOfObservedDescendants() {
        return this.animalObserver.getNumberOfAllDescendants();
    }

    public int getDayOfDeath() {
        return this.animalObserver.getDayOfDeath();
    }

    public void saveStats(String path) {
        try (FileWriter file = new FileWriter(path)) {
            int numberOfDays = this.meanAnimalCounts.size();
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            file.write("Stats summarizing the whole simulation.\n\n");
            file.write("Mean number of animals: " + df.format(1.0 * this.meanAnimalCounts.stream()
                    .reduce(0, Integer::sum) / numberOfDays) + "\n");
            file.write("Mean number of grass patches: " + df.format(1.0 * this.meanGrassCounts.stream()
                    .reduce(0, Integer::sum) / numberOfDays) + "\n");
            file.write("Overall leading genotype: " + "[" + this.getOverallLeadingGenotype().stream()
                    .map(Object::toString).collect(Collectors.joining(" ")) + "]" + "\n");
            file.write("Mean energy level: " + df.format(this.meanEnergyLevels.stream()
                    .reduce(0.0, Double::sum) / numberOfDays) + "\n");
            double meanDaysLived = this.getMeanDaysLived();
            if (meanDaysLived == 0) {
                file.write("There were no dead animals\n");
            } else {
                file.write("Mean lifetime of dead animals: " + df.format(meanDaysLived) + "\n");
            }
            file.write("Mean number of children: " + df.format(this.meanNumberOfChildren.stream()
                    .reduce(0.0, Double::sum) / numberOfDays) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
