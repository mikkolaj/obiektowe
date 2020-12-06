package agh.cs.lab1;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.stream.Collectors;

public class GrassField implements IWorldMap, IPositionChangeObserver {
    private final Random generator = new Random();
    private final Map<Vector2d, Grass> grassMap = new HashMap<>();
    private final Multimap<Vector2d, Animal> animalMap = ArrayListMultimap.create();
    private final MapVisualizer mapVisualizer = new MapVisualizer(this);
    private final Vector2d mapLowerLeft;
    private final Vector2d mapUpperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final Vector2d jungleSize;
    private final Vector2d mapSize;
    private final int plantEnergy;
    private final int startEnergy;
    private boolean freeSpotJungle = true;
    private boolean freeSpotSavannah = true;

    public GrassField(int width, int height, float jungleRatio, int plantEnergy, int startEnergy) {
        Vector2d[] corners = setCorners(width, height);
        this.mapLowerLeft = corners[0];
        this.mapUpperRight = corners[1];
        corners = setCorners((int) Math.floor(width * jungleRatio), (int) Math.floor(height * jungleRatio));
        this.jungleLowerLeft = corners[0];
        this.jungleUpperRight = corners[1];
        this.jungleSize = jungleUpperRight.subtract(jungleLowerLeft).add(new Vector2d(1, 1));
        this.mapSize = mapUpperRight.subtract(mapLowerLeft).add(new Vector2d(1, 1));
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;
    }

    private Vector2d[] setCorners(int width, int height) {
        Vector2d lowerLeft = new Vector2d(-width / 2, -height / 2);
        int upperX = width / 2;
        int upperY = height / 2;
        if (width % 2 == 0) {
            upperX -= 1;
            upperY -= 1;
        }
        Vector2d upperRight = new Vector2d(upperX, upperY);
        return new Vector2d[]{lowerLeft, upperRight};
    }

    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (this.canMoveTo(position)) {
            this.animalMap.put(position, animal);
            animal.addObserver(this);
            return true;
        } else {
            throw new IllegalArgumentException("Animal can't be placed on this field: " + position);
        }
    }

    public void placeGrass() {
        if (freeSpotJungle) {
            Vector2d grassPos;
            do {
                grassPos = new Vector2d(this.jungleLowerLeft.x + this.generator.nextInt(this.jungleSize.x), this.jungleLowerLeft.y + this.generator.nextInt(this.jungleSize.y));
            } while (this.grassMap.get(grassPos) != null && this.animalMap.get(grassPos) != null);
            Grass newGrass = new Grass(grassPos);
            this.grassMap.put(grassPos, newGrass);
        }
        if (freeSpotSavannah) {
            Vector2d grassPos;
            do {
                grassPos = new Vector2d(this.mapLowerLeft.x + this.generator.nextInt(this.mapSize.x), this.mapLowerLeft.y + this.generator.nextInt(this.mapSize.y));
            } while (this.grassMap.get(grassPos) != null && this.animalMap.get(grassPos) != null && !this.insideJungle(grassPos));
            Grass newGrass = new Grass(grassPos);
            this.grassMap.put(grassPos, newGrass);
        }
    }

    private boolean insideJungle(Vector2d position) {
        return position.precedes(this.jungleUpperRight) && position.follows(this.jungleLowerLeft);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return !this.canMoveTo(position) || this.getGrass(position) != null;
    }

    private Object getGrass(Vector2d position) {
        return this.grassMap.get(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        Object animal = this.animalMap.get(position);
        return animal != null ? animal : getGrass(position);
    }

    public Map<Vector2d, Grass> getGrassMap() {
        return grassMap;
    }

    public void findFreeSpots() {
        for (int i = this.mapLowerLeft.x; i < this.mapUpperRight.x; i++) {
            for (int j = this.mapLowerLeft.y; j < this.mapUpperRight.y; j++) {
                Vector2d position = new Vector2d(i, j);
                if (this.animalMap.get(position).size() == 0 && this.grassMap.get(position) == null) {
                    if (this.insideJungle(position)) {
                        this.freeSpotJungle = true;
                    } else {
                        this.freeSpotSavannah = true;
                    }
                }
            }
        }
    }

    public void removeDeadAnimals() {
        this.animalMap.asMap().forEach((key, listOfAnimals) -> listOfAnimals.forEach(animal -> {
                    if (animal.getEnergy() <= 0) {
                        // TODO może się krzaczy?
                        this.animalMap.remove(key, animal);
                    }
                }
        ));
    }

    public void eatGrass() {
        this.grassMap.forEach((position, grass) -> {
            Collection<Animal> listOfAnimals = this.animalMap.get(position);
            Animal strongestAnimal = listOfAnimals.stream().max(Comparator.comparingInt(Animal::getEnergy)).orElse(null);
            if (strongestAnimal != null) {
                List<Animal> strongestAnimals = listOfAnimals.stream().filter(animal1 -> animal1.getEnergy() == strongestAnimal.getEnergy()).collect(Collectors.toList());
                int energyPerAnimal = this.plantEnergy / strongestAnimals.size();
                for (Animal animal : strongestAnimals) {
                    animal.boostEnergy(energyPerAnimal);
                }
            }
        });
    }

    public void reproduce() {
        this.grassMap.forEach((position, grass) -> {
            Collection<Animal> listOfAnimals = this.animalMap.get(position);
            List<Animal> reproducingAnimals = listOfAnimals.stream().filter(animal -> animal.getEnergy() > 0.5 * this.startEnergy).sorted(Comparator.comparingInt(Animal::getEnergy)).collect(Collectors.toList());
            int highestEnergy = reproducingAnimals.get(0).getEnergy();
            int secondHighestEnergy = reproducingAnimals.get(1).getEnergy();
            List<Animal> drawableAnimals;
            Animal firstAnimal;
            Animal secondAnimal;
            if(highestEnergy == secondHighestEnergy) {
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
            for(int i=-1; i<=1; i++) {
                for(int j=-1; j<=1; j++) {
                    if(i != 0 || j != 0) {
                        Vector2d adjacentPos = this.wrapPositions(new Vector2d(position.x + i, position.y + j));
                        if(this.objectAt(adjacentPos) == null) {
                            freePositions.add(adjacentPos);
                        }
                    }
                }
            }
            Vector2d childPosition;
            if(freePositions.size() == 0) {
                int x = this.generator.nextInt(2) - 1;
                int y;
                do {
                    y = this.generator.nextInt(2) - 1;
                } while (x == 0 && y == 0);
                childPosition = this.wrapPositions(new Vector2d(x, y));
            } else {
                childPosition = freePositions.get(this.generator.nextInt(freePositions.size()));
            }
            Animal child = new Animal(this, childPosition, firstAnimal, secondAnimal);
            this.place(child);
        });
    }

    public Vector2d wrapPositions(Vector2d position) {
        int x = position.x;
        int y = position.y;
        if(position.x > this.mapUpperRight.x) {
            x = this.mapLowerLeft.x;
        } else if(position.x < this.mapLowerLeft.x) {
            x = this.mapUpperRight.x;
        }
        if(position.y > this.mapUpperRight.y) {
            y = this.mapLowerLeft.y;
        } else if(position.y < this.mapLowerLeft.y) {
            y = this.mapUpperRight.y;
        }
        return new Vector2d(x, y);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        if (!oldPosition.equals(newPosition)) {
            Collection<Animal> listOfAnimals = this.animalMap.get(oldPosition);
            listOfAnimals.forEach(animal -> {
                if (animal.getPosition().equals(newPosition)) {
                    this.animalMap.remove(oldPosition, animal);
                    this.animalMap.put(newPosition, animal);
                }
            });
        }
    }

    public boolean canMoveTo(Vector2d position) {
        return !(this.objectAt(position) instanceof Animal);
    }



    public String toString() {
        return this.mapVisualizer.draw(this.mapLowerLeft, this.mapUpperRight);
    }
}
