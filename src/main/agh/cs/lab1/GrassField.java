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
    private final Vector2d mapLowerLeft;
    private final Vector2d mapUpperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final Vector2d jungleSize;
    private final Vector2d mapSize;
    private boolean freeSpotJungle = true;
    private boolean freeSpotSavannah = true;

    public GrassField(int width, int height, double jungleRatio) {
        this.mapLowerLeft = new Vector2d(0, 0);
        this.mapUpperRight = new Vector2d(width - 1, height - 1);
        int jWidth = (int) Math.floor(width * jungleRatio);
        int jHeight = (int) Math.floor(height * jungleRatio);
        Vector2d[] offsets = findOffsets(jWidth, jHeight);
        this.jungleLowerLeft = (new Vector2d(width/2, height/2)).subtract(offsets[0]);
        this.jungleUpperRight = (new Vector2d(width/2 , height/2)).add(offsets[1]);
        this.jungleSize = new Vector2d(jWidth, jHeight);
        this.mapSize = new Vector2d(width, height);
    }

    public Vector2d[] findOffsets(int jWidth, int jHeight) {
        Vector2d offsetLower = new Vector2d(jWidth/2, jHeight/2);
        int x = jWidth/2;
        int y = jHeight/2;
        if (jWidth % 2 == 0) {
            x -= 1;
        }
        if (jHeight % 2 == 0) {
            y -= 1;
        }
        Vector2d offsetUpper = new Vector2d(x, y);
        return new Vector2d[]{offsetLower, offsetUpper};
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
                .sorted(Comparator.comparingInt(Animal::getEnergy).reversed())
                .collect(Collectors.toList());
        return !animals.isEmpty() ? animals.get(0) : getGrass(position);
    }

    public boolean grassPresentAt(Vector2d position) {
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

    public int getNumberOfGrassPatches() {
        return grassMap.size();
    }

    // map is a torus, we can move everywhere
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    // determine if it is possible to place grass in either of biomes
    public void findFreeSpots() {
        this.freeSpotJungle = false;
        this.freeSpotSavannah = false;
        for (int i = this.mapLowerLeft.x; i <= this.mapUpperRight.x; i++) {
            for (int j = this.mapLowerLeft.y; j <= this.mapUpperRight.y; j++) {
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

    // wrap around positions of our map, animals can move one tile at a time, so the
    // implementation is simple
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

    // we need to inform our map about animals changing their positions
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

    // inform observers of the map about a change on each field
    public void fieldChanged(Vector2d position) {
        for (IFieldChangeObserver observer : this.observers) {
            observer.fieldChanged(position);
        }
    }

    public Vector2d getMapSize() {
        return this.mapSize;
    }
}
