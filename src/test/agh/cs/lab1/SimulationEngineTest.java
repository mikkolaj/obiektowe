package agh.cs.lab1;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimulationEngineTest { // jedna symulacja wystarcza, żeby uznać klasę za przetestowaną?
    @Test
    public void testRun() {
        RectangularMap map = new RectangularMap(10, 5);
        Vector2d postion1 = new Vector2d(2, 2);
        Vector2d postion2 = new Vector2d(3, 4);
        Vector2d[] positions = {postion1, postion2};
        String[] directions = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        MoveDirection[] moveDirections = OptionsParser.parse(directions);
        SimulationEngine eng = new SimulationEngine(moveDirections, map, positions);
        Object animal1 = map.objectAt(new Vector2d(2, 2));
        Object animal2 = map.objectAt(new Vector2d(3, 4));
        eng.run();
        assertTrue(animal1.equals(map.objectAt(new Vector2d(2, 0))));
        assertTrue(animal2.equals(map.objectAt(new Vector2d(3, 4))));
    }
}
