package agh.cs.lab1;

import java.util.Arrays;

public class SimulationEngine implements IEngine {
    private IWorldMap map;
    private MoveDirection[] directions;
    private int numOfAnimals;

    public SimulationEngine(MoveDirection[] directions, IWorldMap map, Vector2d[] positions) {
        for(int i=0; i<positions.length; i++) {
            Animal newAnimal = new Animal(map, positions[i]);
            map.place(newAnimal);
        }

        this.map = map;
        this.directions = directions;
        this.numOfAnimals = positions.length;
    }

    @Override
    public void run() {
//        System.out.println(this.map);
        for(int i=0; i<this.directions.length; i+=this.numOfAnimals) {
            int end = i + this.numOfAnimals;
            this.map.run(Arrays.copyOfRange(this.directions, i, Math.min(end, this.directions.length)));
//            System.out.println(this.map);
        }
    }
}
