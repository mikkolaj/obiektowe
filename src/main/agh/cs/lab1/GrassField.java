package agh.cs.lab1;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.stream.Collectors;

public class GrassField implements IWorldMap, IPositionChangeObserver {
    private final Random generator = new Random();
    private final ArrayList<IFieldChangeObserver> observers = new ArrayList<>();
    private final Map<Vector2d, Grass> grassMap = new HashMap<>();
    private final Multimap<Vector2d, Animal> animalMap = ArrayListMultimap.create();
    private final OldMapVisualizer oldMapVisualizer = new OldMapVisualizer(this);
    private final Vector2d mapLowerLeft;
    private final Vector2d mapUpperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final Vector2d jungleSize;
    private final Vector2d mapSize;
    public final int moveEnergy;
    private boolean freeSpotJungle = true;
    private boolean freeSpotSavannah = true;

    public GrassField(int width, int height, double jungleRatio, int moveEnergy) {
        Vector2d[] corners = setCorners(width, height);
        Vector2d offset = new Vector2d(width / 2, height / 2);
        this.mapLowerLeft = corners[0].add(offset);
        this.mapUpperRight = corners[1].add(offset);
        corners = setCorners((int) Math.floor(width * jungleRatio), (int) Math.floor(height * jungleRatio));
        this.jungleLowerLeft = corners[0].add(offset);
        this.jungleUpperRight = corners[1].add(offset);
        this.jungleSize = jungleUpperRight.subtract(jungleLowerLeft).add(new Vector2d(1, 1));
        this.mapSize = mapUpperRight.subtract(mapLowerLeft).add(new Vector2d(1, 1));
        this.moveEnergy = moveEnergy;
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
            this.fieldChanged(position);
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
            this.fieldChanged(grassPos);
        }
        if (freeSpotSavannah) {
            Vector2d grassPos;
            do {
                grassPos = new Vector2d(this.mapLowerLeft.x + this.generator.nextInt(this.mapSize.x), this.mapLowerLeft.y + this.generator.nextInt(this.mapSize.y));
            } while (this.grassMap.get(grassPos) != null && this.animalMap.get(grassPos) != null && !this.insideJungle(grassPos));
            Grass newGrass = new Grass(grassPos);
            this.grassMap.put(grassPos, newGrass);
            this.fieldChanged(grassPos);
        }
    }

    private boolean insideJungle(Vector2d position) {
        return position.precedes(this.jungleUpperRight) && position.follows(this.jungleLowerLeft);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.objectAt(position) != null;
    }

    private Object getGrass(Vector2d position) {
        return this.grassMap.get(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        List<Animal> animals = this.animalMap.get(position)
                .stream()
                .sorted(Comparator.comparingInt(Animal::getEnergy))
                .collect(Collectors.toList());
        return !animals.isEmpty() ? animals.get(0) : getGrass(position);
    }

    public boolean GrassPresentAt(Vector2d position) {
        return this.grassMap.containsKey(position);
    }

    public void removeGrassAt(Vector2d position) {
        this.grassMap.remove(position);
        this.fieldChanged(position);
    }

    public void removeAnimalAt(Vector2d position, Animal animal) {
        this.animalMap.remove(position, animal);
        this.fieldChanged(position);
    }

    public int getNumberOfGrass() {
        return grassMap.size();
    }

    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    public String toString() {
        return this.oldMapVisualizer.draw(this.mapLowerLeft, this.mapUpperRight);
    }

    public void findFreeSpots() {
        this.freeSpotJungle = false;
        this.freeSpotSavannah = false;
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

    public Vector2d wrapPositions(Vector2d position) {
        int x = this.wrapPosition(position.x, mapUpperRight.x, mapLowerLeft.x);
        int y = this.wrapPosition(position.y, mapUpperRight.y, mapLowerLeft.y);
        return new Vector2d(x, y);
    }

    private int wrapPosition(int position, int upperBound, int lowerBound) {
        if (position > upperBound) {
            position = lowerBound;
        } else if (position < lowerBound) {
            position = upperBound;
        }
        return position;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        if (!oldPosition.equals(newPosition)) {
            Collection<Animal> listOfAnimals = this.animalMap.get(oldPosition);
            Animal animalToReposition = null;
            for (Animal animal : listOfAnimals) {
                if (animal.getPosition().equals(newPosition)) {
                    animalToReposition = animal;
                }
            }
            if (animalToReposition != null) {
                this.animalMap.remove(oldPosition, animalToReposition);
                this.animalMap.put(newPosition, animalToReposition);
                this.fieldChanged(oldPosition);
                this.fieldChanged(newPosition);
            }
        }
    }


    public void addObserver(IFieldChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IFieldChangeObserver observer) {
        this.observers.remove(observer);
    }

    private void fieldChanged(Vector2d position) {
        for (IFieldChangeObserver observer : this.observers) {
            observer.fieldChanged(position);
        }
    }

    public Vector2d getMapSize() {
        return this.mapSize;
    }
}
