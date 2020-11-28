package agh.cs.lab1;

import java.util.ArrayList;

public class SimulationEngine implements IEngine {
    private final MoveDirection[] directions;
    private final ArrayList<Animal> animals = new ArrayList<>();
//    private final IWorldMap map;

    public SimulationEngine(MoveDirection[] directions, AbstractWorldMap map, Vector2d[] positions) {
        for (int i = 0; i < positions.length; i++) {
            Animal newAnimal = new Animal(map, positions[i]);
            map.place(newAnimal);
            animals.add(newAnimal);
        }

//        this.map = map;
        this.directions = directions;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.directions.length; i += this.animals.size()) {
            for (int j = 0; j < this.animals.size(); j++) {
//                System.out.println(this.map);
                Animal curAnimal = this.animals.get(j);
                curAnimal.move(this.directions[i + j]);
            }
        }
//        System.out.println(this.map);
    }
}
