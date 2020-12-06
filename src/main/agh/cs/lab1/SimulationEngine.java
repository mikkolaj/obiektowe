package agh.cs.lab1;

import java.util.ArrayList;

public class SimulationEngine implements IEngine {
    private final int roundCount;
    private final ArrayList<Animal> animals = new ArrayList<>();
    private final IWorldMap map;

    public SimulationEngine(int roundCount, IWorldMap map, Vector2d[] positions) {
        for (int i = 0; i < positions.length; i++) {
            Animal newAnimal = new Animal(map, positions[i]);
            map.place(newAnimal);
            animals.add(newAnimal);
        }

        this.map = map;
        this.roundCount = roundCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < roundCount; i += this.animals.size()) {
            for (int j = 0; j < this.animals.size(); j++) {
                System.out.println(this.map);
                Animal curAnimal = this.animals.get(j);
                curAnimal.move();
            }
        }
        System.out.println(this.map);
    }
}
