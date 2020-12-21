package agh.cs.lab1;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class SimulationEngine implements IEngine {
    private final static int ROUND_DELAY = 40;
    private final Multimap<Vector2d, Animal> animalMap = ArrayListMultimap.create();
    private final GrassField map;
    private final int plantEnergy;
    private final int startEnergy;
    private final Random generator = new Random();
    private final SimulationStatistics stats;
    private volatile boolean isPaused = false;
    private final SimulationVisualizer simulationVisualizer;
    private final int moveDelay;
    private final int moveEnergy;


    public SimulationEngine(Stage primaryStage, GrassField map, int numberOfAnimals, int startEnergy, int plantEnergy,
                            int moveDelay, int moveEnergy, int simNumber) {
        Vector2d mapSize = map.getMapSize();

        this.stats = new SimulationStatistics(this, map);
        this.simulationVisualizer = new SimulationVisualizer(primaryStage, this,
                this.stats, map, mapSize.x, mapSize.y, startEnergy, simNumber);

        List<Vector2d> positions = this.drawInitialPositions(numberOfAnimals, mapSize);

        for (Vector2d position : positions) {
            Animal newAnimal = new Animal(map, position, startEnergy);
            map.place(newAnimal);
            this.animalMap.put(position, newAnimal);
            this.stats.animalPlaced(newAnimal);
        }

        this.map = map;
        this.moveDelay = moveDelay;
        this.startEnergy = startEnergy;
        this.plantEnergy = plantEnergy;
        this.moveEnergy = moveEnergy;
    }

    @Override
    public void run() {
        // new Thread not to block refreshing of the GUI
        new Thread(() -> {
            while (true) {
                this.simulationVisualizer.updateVisualizer();
                // wait for user input to resume the simulation
                while (isPaused) {
                    Thread.onSpinWait();
                }
                // for each animal, move it and update its position on the map
                List<Animal> values = new ArrayList<>(this.animalMap.values());
                for (Animal animal : values) {
                    Vector2d oldPos = animal.getPosition();
                    animal.setEnergy(animal.getEnergy()-this.moveEnergy);
                    animal.move();
                    Vector2d newPos = animal.getPosition();
                    this.animalMap.remove(oldPos, animal);
                    this.animalMap.put(newPos, animal);
                    try {
                        Thread.sleep(moveDelay);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                // normal cycle of the day
                this.removeDeadAnimals();
                this.eatGrass();
                this.reproduce();
                this.map.findFreeSpots();
                this.map.placeGrass();

                // inform our Statistics class about the end of the day
                this.stats.dayHasPassed();
                try {
                    Thread.sleep(ROUND_DELAY);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private List<Vector2d> drawInitialPositions(int number, Vector2d mapSize) {
        Random generator = new Random();
        List<Vector2d> positions = new ArrayList<>();
        do {
            int x = generator.nextInt(mapSize.x);
            int y = generator.nextInt(mapSize.y);
            Vector2d position = new Vector2d(x, y);
            if (!positions.contains(position)) {
                positions.add(position);
            }
        } while (positions.size() < number);
        return positions;
    }

    private void eatGrass() {
        ArrayList<Vector2d> grassToRemove = new ArrayList<>();

        // check each position on the map taken by animals, and feed strongest of them (dividing the energy
        // if there is a need to)
        this.animalMap.asMap().forEach((position, listOfAnimals) -> {
            if (this.map.grassPresentAt(position)) {
                Animal strongestAnimal = listOfAnimals.stream().max(Comparator.comparingInt(Animal::getEnergy)).orElse(null);
                if (strongestAnimal != null) {
                    grassToRemove.add(position);
                    List<Animal> strongestAnimals = listOfAnimals.stream().filter(animal1 -> animal1.getEnergy() == strongestAnimal.getEnergy()).collect(Collectors.toList());
                    int energyPerAnimal = this.plantEnergy / strongestAnimals.size();
                    for (Animal animal : strongestAnimals) {
                        animal.setEnergy(animal.getEnergy() + energyPerAnimal);
                    }
                }
            }
        });
        for (Vector2d position : grassToRemove) {
            this.map.removeGrassAt(position);
        }
    }

    private void removeDeadAnimals() {
        ArrayList<Vector2d> deadAnimalsKeys = new ArrayList<>();
        ArrayList<Animal> deadAnimals = new ArrayList<>();

        // If an animal has energy level of 0 it's dead
        this.animalMap.asMap().forEach((key, listOfAnimals) -> listOfAnimals.forEach(animal -> {
                    if (animal.getEnergy() <= 0) {
                        deadAnimalsKeys.add(key);
                        deadAnimals.add(animal);
                    }
                }
        ));

        for (int i = 0; i < deadAnimals.size(); i++) {
            Vector2d animalsPosition = deadAnimalsKeys.get(i);
            Animal animalToRemove = deadAnimals.get(i);
            this.animalMap.remove(animalsPosition, animalToRemove);
            this.map.removeAnimalAt(animalsPosition, animalToRemove);
            this.stats.animalDied(animalToRemove);
        }
    }

    public void pauseSimulation() {
        this.isPaused = true;
    }

    public void resumeSimulation() {
        this.isPaused = false;
    }

    public boolean isSimulationPaused() {
        return this.isPaused;
    }

    public Collection<Animal> getAllAnimals() {
        return this.animalMap.values();
    }

    private void reproduce() {
        ArrayList<Animal> animalsToPlace = new ArrayList<>();

        this.animalMap.asMap().forEach((position, listOfAnimals) -> {
            // animal is able to reproduce if its current energy level is greater than half of the start energy for
            // first animals

            List<Animal> reproducingAnimals = listOfAnimals.stream().filter(animal -> animal.getEnergy() >= 0.5 * this.startEnergy).sorted(Comparator.comparingInt(Animal::getEnergy)).collect(Collectors.toList());

            // at least 2 animals are needed
            if (reproducingAnimals.size() > 1) {
                int highestEnergy = reproducingAnimals.get(0).getEnergy();
                int secondHighestEnergy = reproducingAnimals.get(1).getEnergy();
                List<Animal> drawableAnimals;
                Animal firstAnimal;
                Animal secondAnimal;

                // if 2 or more animals on the same position have the highest number of energy, we draw 2 of them
                if (highestEnergy == secondHighestEnergy) {
                    int firstAnimalNum;
                    int secondAnimalNum;
                    drawableAnimals = listOfAnimals.stream()
                            .filter(animal -> animal.getEnergy() == highestEnergy)
                            .collect(Collectors.toList());
                    firstAnimalNum = this.generator.nextInt(drawableAnimals.size());
                    do {
                        secondAnimalNum = this.generator.nextInt(drawableAnimals.size());
                    } while (secondAnimalNum == firstAnimalNum);
                    firstAnimal = reproducingAnimals.get(firstAnimalNum);
                    secondAnimal = reproducingAnimals.get(secondAnimalNum);
                } else {
                    // otherwise we need to draw one animal from those with second highest energy
                    firstAnimal = reproducingAnimals.get(0);
                    drawableAnimals = listOfAnimals.stream()
                            .filter(animal -> animal.getEnergy() == secondHighestEnergy)
                            .collect(Collectors.toList());
                    secondAnimal = reproducingAnimals.get(this.generator.nextInt(drawableAnimals.size()));
                }

                List<Vector2d> freePositions = new ArrayList<>();

                // search for a free position adjacent to the current one
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i != 0 || j != 0) {
                            Vector2d adjacentPos = this.map.wrapPositions(new Vector2d(position.x + i, position.y + j));
                            if (this.map.objectAt(adjacentPos) == null) {
                                freePositions.add(adjacentPos);
                            }
                        }
                    }
                }

                Vector2d childPosition;

                // if no positions are free, choose random adjacent one
                if (freePositions.size() == 0) {
                    int x = this.generator.nextInt(2) - 1;
                    int y;
                    do {
                        y = this.generator.nextInt(2) - 1;
                    } while (x == 0 && y == 0);
                    childPosition = this.map.wrapPositions(new Vector2d(x, y));
                } else {
                    //otherwise pick one of free positions
                    childPosition = freePositions.get(this.generator.nextInt(freePositions.size()));
                }
                Animal child = new Animal(this.map, childPosition, firstAnimal, secondAnimal);
                this.stats.animalsReproduced(firstAnimal, secondAnimal, child);
                animalsToPlace.add(child);
            }
        });

        for (Animal animal : animalsToPlace) {
            this.map.place(animal);
            this.animalMap.put(animal.getPosition(), animal);
        }
    }
}
