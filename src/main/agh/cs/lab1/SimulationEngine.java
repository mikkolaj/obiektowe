package agh.cs.lab1;

import java.util.ArrayList;

public class SimulationEngine implements IEngine {
    private final MoveDirection[] directions;
    private final ArrayList<Animal> animals = new ArrayList<>();

    public SimulationEngine(MoveDirection[] directions, AbstractWorldMap map, Vector2d[] positions) {
        for (int i = 0; i < positions.length; i++) {
            Animal newAnimal = new Animal(map, positions[i]);
            if(map.place(newAnimal)) {
                animals.add(newAnimal);
            } else {
                // W przyszłości rzucimy tu wyjątek
            }
        }

        this.directions = directions;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.directions.length; i += this.animals.size()) {
            for (int j = 0; j < this.animals.size(); j++) {
                Animal curAnimal = this.animals.get(j);
                curAnimal.move(this.directions[i + j]);
            }
        }
    }
}
