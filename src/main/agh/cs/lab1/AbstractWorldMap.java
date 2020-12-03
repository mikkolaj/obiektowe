package agh.cs.lab1;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected final Map<Vector2d,Animal> animalMap = new HashMap<>();
    private final MapVisualizer mapVisualizer = new MapVisualizer(this);
    protected final MapBoundary mapBoundary = new MapBoundary();    // RectangularMap nie powinno używać MapBoundary

    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if(this.canMoveTo(position)) {
            this.animalMap.put(position, animal);
            this.mapBoundary.addObject(animal);
            animal.addObserver(this);
            return true;
        } else {
            throw new IllegalArgumentException("Animal can't be placed on this field: " + position);
        }
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        if (!oldPosition.equals(newPosition)) {
            Animal animal = this.animalMap.get(oldPosition);
            this.animalMap.remove(oldPosition);
            this.animalMap.put(newPosition, animal);
        }
    }

    public boolean canMoveTo(Vector2d position) {
        return !(this.objectAt(position) instanceof Animal);
    }

    public boolean isOccupied(Vector2d position) {
        return !this.canMoveTo(position);
    }

    public Object objectAt(Vector2d position) {
        return this.animalMap.get(position);
    }

    public String toString() {
        return this.mapVisualizer.draw(this.mapBoundary.getLowerLeft(), this.mapBoundary.getUpperRight());
    }
}
