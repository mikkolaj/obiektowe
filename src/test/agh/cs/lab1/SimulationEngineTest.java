//package agh.cs.lab1;
//
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//public class SimulationEngineTest {
//    @Test
//    public void testRun() {
//        RectangularMap map = new RectangularMap(10, 5);
//        Vector2d position1 = new Vector2d(2, 2);
//        Vector2d position2 = new Vector2d(3, 4);
//        Vector2d[] positions = {position1, position2};
//        String[] directions = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
//        MoveDirection[] moveDirections = OptionsParser.parse(directions);
//        SimulationEngine eng = new SimulationEngine(moveDirections, map, positions);
//        Object animal1 = map.objectAt(position1);
//        Object animal2 = map.objectAt(position2);
//        eng.run();
//        assertTrue(animal1.equals(map.objectAt(new Vector2d(2, 0))));
//        assertTrue(animal2.equals(map.objectAt(new Vector2d(3, 4))));
//
//
//        RectangularMap map2 = new RectangularMap(10, 5);
//        Vector2d position3 = new Vector2d(1, 1);
//        Vector2d position4 = new Vector2d(1, 2);
//        Vector2d[] positions2 = {position3, position4};
//        String[] directions2 = {"l", "r", "f", "f"};
//        MoveDirection[] moveDirections2 = OptionsParser.parse(directions2);
//        SimulationEngine eng2 = new SimulationEngine(moveDirections2, map2, positions2);
//        Object animal3 = map2.objectAt(position3);
//        Object animal4 = map2.objectAt(position4);
//        eng2.run();
//        assertTrue(animal3.equals(map2.objectAt(new Vector2d(0, 1))));
//        assertTrue(animal4.equals(map2.objectAt(new Vector2d(2, 2))));
//
//        GrassField map3 = new GrassField(10);
//        Vector2d position5 = new Vector2d(2, 2);
//        Vector2d position6 = new Vector2d(3, 4);
//        Vector2d[] positions3 = {position5, position6};
//        String[] directions3 = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
//        MoveDirection[] moveDirections3 = OptionsParser.parse(directions3);
//        SimulationEngine eng3 = new SimulationEngine(moveDirections3, map3, positions3);
//        Object animal5 = map3.objectAt(position5);
//        Object animal6 = map3.objectAt(position6);
//        eng3.run();
//        assertTrue(animal5.equals(map3.objectAt(new Vector2d(2, -1))));
//        assertTrue(animal6.equals(map3.objectAt(new Vector2d(3, 7))));
//
//
//        GrassField map4 = new GrassField(10);
//        Vector2d position7 = new Vector2d(1, 1);
//        Vector2d position8 = new Vector2d(1, 2);
//        Vector2d[] positions4 = {position7, position8};
//        String[] directions4 = {"l", "r", "f", "f"};
//        MoveDirection[] moveDirections4 = OptionsParser.parse(directions4);
//        SimulationEngine eng4 = new SimulationEngine(moveDirections4, map4, positions4);
//        Object animal7 = map4.objectAt(position7);
//        Object animal8 = map4.objectAt(position8);
//        eng4.run();
//        assertTrue(animal7.equals(map4.objectAt(new Vector2d(0, 1))));
//        assertTrue(animal8.equals(map4.objectAt(new Vector2d(2, 2))));
//    }
//}
