package agh.cs.lab1;
import java.util.HashMap;
import java.util.Map;

class RectangularMap extends AbstractWorldMap {
    private Vector2d upperRight;

    public RectangularMap (int width, int height) {
        this.upperRight = new Vector2d(width - 1, height - 1);
        this.visualizedMap = new MapVisualizer(this);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return insideMap(position) && !isOccupied(position);
    }

    public boolean insideMap(Vector2d position) {
        return position.follows(this.lowerLeft) && position.precedes(this.upperRight);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.insideMap(position) && this.objectAt(position) != null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        return this.animalMap.get(position);
    }

    @Override
    public String toString() {
        return this.visualizedMap.draw(this.lowerLeft, this.upperRight);
    }
}
