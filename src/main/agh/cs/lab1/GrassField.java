package agh.cs.lab1;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GrassField extends AbstractWorldMap {
    private Random generator = new Random();    // to może być finalne
    private final Map<Vector2d,Grass> grassMap = new HashMap<>();
    private Vector2d upperRight = new Vector2d(0, 0);

    public GrassField(int grassNum) {
        for(int i=0; i<grassNum; i++) {
            int upperBound = (int)Math.floor(Math.sqrt(grassNum*10));
            Vector2d grassPos;
            do {
                grassPos = new Vector2d(this.generator.nextInt(upperBound), this.generator.nextInt(upperBound));
            } while (grassMap.get(grassPos) != null);
            Grass newGrass = new Grass(grassPos);
            grassMap.put(grassPos, newGrass);
            this.visualizedMap = new MapVisualizer(this);   // czemu w pętli?
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
         return this.animalAt(position) == null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.objectAt(position) != null;
    }

    public Animal animalAt(Vector2d position) {
        return this.animalMap.get(position);
    }

    @Override
    public Object objectAt(Vector2d position) {
        Animal animal = this.animalAt(position);
        return animal != null ? animal : this.grassMap.get(position);
    }

    @Override
    public String toString() {
        updateCorners(grassMap.keySet());
        updateCorners(animalMap.keySet());
        return this.visualizedMap.draw(this.lowerLeft, this.upperRight);
    }

    public Map<Vector2d,Grass> getGrassMap() {
        return grassMap;
    }

    private void updateCorners(Set<Vector2d> vector2ds) {
        for (Vector2d position : vector2ds) {
            if(position.x > this.upperRight.x || position.y > this.upperRight.y) {  // lepiej operować na całych wektorach, niż pojedynczych współrzędnych
                this.upperRight = this.upperRight.upperRight(position);
            }
            if(position.x < this.lowerLeft.x || position.y < this.lowerLeft.y) {
                this.lowerLeft = this.lowerLeft.lowerLeft(position);
            }
        }   // mapa się rozszerza, ale nie kurczy
    }
}
