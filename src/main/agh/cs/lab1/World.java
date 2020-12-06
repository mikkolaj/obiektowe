package agh.cs.lab1;


public class World {
    public static void main(String[] args) {
        try {
//            AbstractWorldMap map = new GrassField(10);
            Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(3, 4)};
//            IEngine engine = new SimulationEngine(4, map, positions);
//            engine.run();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
            System.exit(1);
        }
    }
}
