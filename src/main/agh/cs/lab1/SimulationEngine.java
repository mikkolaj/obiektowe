package agh.cs.lab1;

import java.util.ArrayList;

public class SimulationEngine implements IEngine {
    private MoveDirection[] directions;
    private int numOfAnimals;   // czy to pole jest potrzebne, jak możemy użyć animals.size()?
    private ArrayList<Animal> animals = new ArrayList<>();

    public SimulationEngine(MoveDirection[] directions, AbstractWorldMap map, Vector2d[] positions) {
        for (int i = 0; i < positions.length; i++) {
            Animal newAnimal = new Animal(map, positions[i]);
            animals.add(newAnimal); // nie ma żadnej kontroli, czy zwierzę dało się dodać do mapy
        }

        this.directions = directions;
        this.numOfAnimals = positions.length;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.directions.length; i += this.numOfAnimals) {
            for (int j = 0; j < this.numOfAnimals; j++) {
                Animal curAnimal = this.animals.get(j);
                curAnimal.move(directions[i + j]);
            }
        }
    }
}
