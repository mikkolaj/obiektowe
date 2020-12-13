package agh.cs.lab1;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class SimulationEngine implements IEngine {
    private final int roundCount;
    private final Multimap<Vector2d, Animal> animalMap = ArrayListMultimap.create();
    private final GrassField map;
    private final int plantEnergy = 10;
    private final int startEnergy = 100;
    private final Random generator = new Random();
    //    private final SortedSet
    private int deadAnimals;


    public SimulationEngine(Stage primaryStage, int roundCount, GrassField map, Vector2d[] positions, int startEnergy) {
        Vector2d mapSize = map.getMapSize();
        MapVisualizer mapVisualizer = new MapVisualizer(map, mapSize.x, mapSize.y);
        primaryStage.setTitle("Simulation");
        primaryStage.setScene(new Scene(mapVisualizer.createContent(), 800, 600));
        primaryStage.show();

        for (int i = 0; i < positions.length; i++) {
            Animal newAnimal = new Animal(map, positions[i], startEnergy);
            map.place(newAnimal);
            animalMap.put(positions[i], newAnimal);
        }


        this.map = map;
        this.roundCount = roundCount;
    }

    @Override
    public void run() {
        new Thread(() -> {
            for (int i = 0; i < roundCount; i += 1) {
                List<Animal> values = new ArrayList<>(this.animalMap.values());
                for (Animal animal : values) {
                    System.out.println(this.map);
                    Vector2d oldPos = animal.getPosition();
                    animal.move();
                    System.out.println(this.map);
                    Vector2d newPos = animal.getPosition();
                    this.animalMap.remove(oldPos, animal);
                    this.animalMap.put(newPos, animal);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                this.removeDeadAnimals();
                this.eatGrass();
                this.reproduce();
                map.findFreeSpots();
                map.placeGrass();
            }
            System.out.println(this.map);
        }).start();
    }

    private void eatGrass() {
        ArrayList<Vector2d> grassToRemove = new ArrayList<>();
        this.animalMap.asMap().forEach((position, listOfAnimals) -> {
            if (this.map.GrassPresentAt(position)) {
                Animal strongestAnimal = listOfAnimals.stream().max(Comparator.comparingInt(Animal::getEnergy)).orElse(null);
                if (strongestAnimal != null) {
                    grassToRemove.add(position);
                    List<Animal> strongestAnimals = listOfAnimals.stream().filter(animal1 -> animal1.getEnergy() == strongestAnimal.getEnergy()).collect(Collectors.toList());
                    int energyPerAnimal = this.plantEnergy / strongestAnimals.size();
                    for (Animal animal : strongestAnimals) {
                        System.out.print(animal.getEnergy() + " -> ");
                        animal.boostEnergy(energyPerAnimal);
                        System.out.print(animal.getEnergy() + "\n");
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
        }
    }

    private void printStats() {
        System.out.println("Liczba zwierząt " + this.animalMap.values().size());
        System.out.println("Liczba traw: " + this.map.getNumberOfGrass());

    }

    private void genes() {
        final List<int[]> genotypes = new ArrayList<>();
        this.animalMap.values().forEach(animal -> genotypes.add(animal.getGenes()));
        Set<int[]> uniqGenotypes = new TreeSet<>((obj1, obj2) -> {
            if(Arrays.equals(obj1, obj2)) {
                return 0;
            } else {
                for (int i=0; i<obj1.length; i++) {
                    if (obj1[i] < obj2[i]) {
                        return -1;
                    } else if (obj1[i] > obj2[i]) {
                        return 1;
                    }
                }
                return 1;
            }
        });
        uniqGenotypes.addAll(genotypes);
        List<int[]> uniqGenotypesList = new ArrayList<>(uniqGenotypes);
        List<Integer> genOccurences = new ArrayList<>();
        for(int i=0; i<uniqGenotypesList.size(); i++) {
            int finalI = i;
            genOccurences.add(Math.toIntExact(genotypes.stream().filter(genotype -> Arrays.equals(genotype, uniqGenotypesList.get(finalI))).count()));
        }

    }

    private void reproduce() {
        ArrayList<Animal> animalsToPlace = new ArrayList<>();
        this.animalMap.asMap().forEach((position, listOfAnimals) -> {
            List<Animal> reproducingAnimals = listOfAnimals.stream().filter(animal -> animal.getEnergy() > 0.5 * this.startEnergy).sorted(Comparator.comparingInt(Animal::getEnergy)).collect(Collectors.toList());
            if (reproducingAnimals.size() > 1) {
                int highestEnergy = reproducingAnimals.get(0).getEnergy();
                int secondHighestEnergy = reproducingAnimals.get(1).getEnergy();
                List<Animal> drawableAnimals;
                Animal firstAnimal;
                Animal secondAnimal;
                if (highestEnergy == secondHighestEnergy) {
                    int firstAnimalNum;
                    int secondAnimalNum;
                    drawableAnimals = listOfAnimals.stream().filter(animal -> animal.getEnergy() == highestEnergy).collect(Collectors.toList());
                    firstAnimalNum = this.generator.nextInt(drawableAnimals.size());
                    do {
                        secondAnimalNum = this.generator.nextInt(drawableAnimals.size());
                    } while (secondAnimalNum == firstAnimalNum);
                    firstAnimal = reproducingAnimals.get(firstAnimalNum);
                    secondAnimal = reproducingAnimals.get(secondAnimalNum);
                } else {
                    firstAnimal = reproducingAnimals.get(0);
                    drawableAnimals = listOfAnimals.stream().filter(animal -> animal.getEnergy() == secondHighestEnergy).collect(Collectors.toList());
                    secondAnimal = reproducingAnimals.get(this.generator.nextInt(drawableAnimals.size()));
                }

                List<Vector2d> freePositions = new ArrayList<>();

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

                if (freePositions.size() == 0) {
                    int x = this.generator.nextInt(2) - 1;
                    int y;
                    do {
                        y = this.generator.nextInt(2) - 1;
                    } while (x == 0 && y == 0);
                    childPosition = this.map.wrapPositions(new Vector2d(x, y));
                } else {
                    childPosition = freePositions.get(this.generator.nextInt(freePositions.size()));
                }
                Animal child = new Animal(this.map, childPosition, firstAnimal, secondAnimal);
                animalsToPlace.add(child);
            }
        });

        for (Animal animal : animalsToPlace) {
            System.out.println("New Animal at: " + animal.getPosition());
            this.map.place(animal);
            this.animalMap.put(animal.getPosition(), animal);
        }
    }
}
