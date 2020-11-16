package agh.cs.lab1;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected Vector2d lowerLeft = new Vector2d(0, 0);  // czy to na pewno nadaje się na mapę abstrakcyjną?
    Map<Vector2d, Animal> animalMap = new HashMap<>();  // to może być finalne i raczej protected
    protected MapVisualizer visualizedMap;  // to może być finalne + deklaruje Pan, ale nie instancjonuje

    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (this.canMoveTo(position)) {
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

    //tych metod nie dało się zaimplementować tutaj?
    abstract public boolean canMoveTo(Vector2d position);

    abstract public boolean isOccupied(Vector2d position);

    abstract public Object objectAt(Vector2d position);

    abstract public String toString();
}
