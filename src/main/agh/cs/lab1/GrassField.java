package agh.cs.lab1;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GrassField extends AbstractWorldMap {
    private final Random generator = new Random();
    private final Map<Vector2d, Grass> grassMap = new HashMap<>();

    public GrassField(int grassNum) {
        for (int i = 0; i < grassNum; i++) {
            int upperBound = (int) Math.floor(Math.sqrt(grassNum * 10));
            Vector2d grassPos;
            do {
                grassPos = new Vector2d(this.generator.nextInt(upperBound), this.generator.nextInt(upperBound));
            } while (grassMap.get(grassPos) != null);
            Grass newGrass = new Grass(grassPos);
            grassMap.put(grassPos, newGrass);
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.objectAt(position) == null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || this.getGrass(position) != null;
    }

    private Object getGrass(Vector2d position) {
        return this.grassMap.get(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        Object animal = super.objectAt(position);
        return animal != null ? animal : getGrass(position);
    }

    @Override
    public String toString() {
        updateCorners(grassMap.keySet());
        updateCorners(animalMap.keySet());
        return super.toString(this.lowerLeft, this.upperRight);
    }

    public Map<Vector2d, Grass> getGrassMap() {
        return grassMap;
    }

    private void updateCorners(Set<Vector2d> vector2ds) {
        this.upperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        this.lowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (Vector2d position : vector2ds) {
            this.upperRight = this.upperRight.upperRight(position);
            this.lowerLeft = this.lowerLeft.lowerLeft(position);
        }
    }
}
