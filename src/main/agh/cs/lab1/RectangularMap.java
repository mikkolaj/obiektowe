package agh.cs.lab1;
import java.util.ArrayList;
import java.util.Arrays;

class RectangularMap implements IWorldMap {
    private int width;
    private int height;
    private Animal[][] map;
    private ArrayList<Animal> animals;
    private MapVisualizer visualizedMap;
    private Vector2d lowerLeft;
    private Vector2d upperRight;

    public RectangularMap (int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new Animal[height][width];
        this.visualizedMap = new MapVisualizer(this);
        this.animals = new ArrayList<>();
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width - 1, height - 1);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(this.lowerLeft) && position.precedes(this.upperRight);
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if(this.canMoveTo(position) && this.map[position.y][position.x] == null) {
            this.map[position.y][position.x] = animal;
            this.animals.add(animal);
            return true;
        }
        return false;
    }

    @Override
    public void run(MoveDirection[] directions) {
        for(int i=0; i < directions.length; i++) {
            Animal curAnimal = this.animals.get(i);
            Vector2d curPos = curAnimal.getPosition();
            curAnimal.move(directions[i]);
            Vector2d newPos = curAnimal.getPosition();
            if(!newPos.equals(curPos)) {
                map[curPos.y][curPos.x] = null;
                map[newPos.y][newPos.x] = curAnimal;
            }
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return canMoveTo(position) && this.map[position.y][position.x] != null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if(!this.canMoveTo(position))
            return null;
        return map[position.y][position.x];
    }

    public String toString() {
        return this.visualizedMap.draw(this.lowerLeft, this.upperRight);
    }
}
