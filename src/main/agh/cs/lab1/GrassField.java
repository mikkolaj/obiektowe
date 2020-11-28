package agh.cs.lab1;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
            this.mapBoundary.addObject(newGrass);
        }
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

    public Map<Vector2d, Grass> getGrassMap() {
        return grassMap;
    }
}
