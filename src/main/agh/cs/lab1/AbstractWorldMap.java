package agh.cs.lab1;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected Vector2d lowerLeft;
    protected Vector2d upperRight;
    protected final Map<Vector2d,Animal> animalMap = new HashMap<>();
    protected final MapVisualizer visualizedMap = new MapVisualizer(this);  // to może być prywatne; nazwa trochę myląca

    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if(this.canMoveTo(position)) {
            this.animalMap.put(position, animal);
            return true;
        }
        return false;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        if (!oldPosition.equals(newPosition)) {
            Animal animal = this.animalMap.get(oldPosition);
            this.animalMap.remove(oldPosition);
            this.animalMap.put(newPosition, animal);
        }
    }

    public boolean canMoveTo(Vector2d position) {
        return this.objectAt(position) == null;
    }

    public boolean isOccupied(Vector2d position) {
        return !this.canMoveTo(position);
    }

    public Object objectAt(Vector2d position) {
        return this.animalMap.get(position);
    }

    public String toString(Vector2d lowerLeft, Vector2d upperRight) {   // nagłówek toString tak nie wygląda
        return this.visualizedMap.draw(lowerLeft, upperRight);
    }
}
