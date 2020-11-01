package agh.cs.lab1;

public class Animal {
    private MapDirection orientation;
    private Vector2d position;

    public Animal() {
        this.orientation = MapDirection.NORTH;
        this.position = new Vector2d(2, 2);
    }

    public String toString() {
        return "Orientation: " + this.orientation.toString() + " position: " + this.position.toString();
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
        if(newPos.follows(new Vector2d(0, 0)) && newPos.precedes(new Vector2d(4, 4))) {
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
