package agh.cs.lab1;

public class Animal {
    private MapDirection orientation;
    private Vector2d position;
    private IWorldMap map;

    public Animal() {
        this.orientation = MapDirection.NORTH;
        this.position = new Vector2d(2, 2);
    }

    public Animal(IWorldMap map) {
        this.orientation = MapDirection.NORTH;
        this.position = new Vector2d(2, 2);
        this.map = map;
    }

    public Animal(IWorldMap map, Vector2d initialPosition) {
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
        if(this.map != null && this.map.canMoveTo(newPos) && !this.map.isOccupied(newPos)) {
            this.position = newPos;
        } else if(this.map == null && newPos.follows(new Vector2d(0, 0)) && newPos.precedes(new Vector2d(4, 4))) {
            this.position = newPos;
        }
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    // 10. W naszym programie moglibyśmy trzymać tablicę 2D odpowiadającą kształtem naszej planszy, gdzie trzymalibyśmy
    //     wartości true/false w zależności, czy dane pole jest już zajęte. Wtedy do metody move należałoby przekazać
    //     tę tablicę i przed ruszeniem się sprawdzać, czy nie wejdziemy na zajęte pole, oraz aktualizować swoją pozycję
    //     w tablicy.
}
