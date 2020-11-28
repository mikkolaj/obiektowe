package agh.cs.lab1;

import java.util.ArrayList;

public class Animal {
    private MapDirection orientation;
    private Vector2d position;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    final private IWorldMap map;

    public Animal(AbstractWorldMap map) {
        this(map, new Vector2d(0, 0));
    }

    public Animal(AbstractWorldMap map, Vector2d initialPosition) {
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
    }

    public String toString() {
        return this.orientation.toString();
    }

    public void move(MoveDirection direction) {
        Vector2d curMove = this.orientation.toUnitVector();
        switch (direction) {
            case RIGHT:
                this.orientation = this.orientation.next();
                return;
            case LEFT:
                this.orientation = this.orientation.previous();
                return;
            case BACKWARD:
                curMove = curMove.opposite();
                break;
        }
        Vector2d newPos = this.position.add(curMove);
        if(this.map.canMoveTo(newPos)) {
            Vector2d oldPos = this.position;
            this.position = newPos;
            this.positionChanged(oldPos, newPos);
        }
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : this.observers) {
            observer.positionChanged(oldPosition, newPosition);
        }
    }
}
