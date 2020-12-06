//package agh.cs.lab1;
//
//import org.junit.Test;
//
//import java.util.Arrays;
//import java.util.Map;
//
//import static org.junit.Assert.*;
//
//public class AnimalTest {
//    @Test
//    public void testOrientation() {
//        Animal pet = new Animal(new RectangularMap(5, 5), new Vector2d(2, 2));
//        assertTrue(pet.getOrientation() == MapDirection.NORTH);
//        pet.move(MoveDirection.FORWARD);
//        assertTrue(pet.getOrientation() == MapDirection.NORTH);
//        pet.move(MoveDirection.BACKWARD);
//        assertTrue(pet.getOrientation() == MapDirection.NORTH);
//        pet.move(MoveDirection.LEFT);
//        assertTrue(pet.getOrientation() == MapDirection.WEST);
//        pet.move(MoveDirection.RIGHT);
//        assertTrue(pet.getOrientation() == MapDirection.NORTH);
//    }
//
//    @Test
//    public void testPosistions() {
//        Animal pet = new Animal(new RectangularMap(5, 5), new Vector2d(2, 2));
//        assertTrue(pet.getPosition().equals(new Vector2d(2, 2)));
//        pet.move(MoveDirection.FORWARD);
//        assertTrue(pet.getPosition().equals(new Vector2d(2, 3)));
//        pet.move(MoveDirection.BACKWARD);
//        assertTrue(pet.getPosition().equals(new Vector2d(2, 2)));
//        pet.move(MoveDirection.LEFT);
//        assertTrue(pet.getPosition().equals(new Vector2d(2, 2)));
//        pet.move(MoveDirection.RIGHT);
//        assertTrue(pet.getPosition().equals(new Vector2d(2, 2)));
//    }
//
//    @Test
//    public void testBorders() {
//        Animal pet = new Animal(new RectangularMap(5, 5), new Vector2d(2, 2));
//        pet.move(MoveDirection.FORWARD);
//        pet.move(MoveDirection.FORWARD);
//        assertTrue(pet.getPosition().equals(new Vector2d(2, 4)) && pet.getOrientation() == MapDirection.NORTH);
//
//        // Try to break northern border
//        pet.move(MoveDirection.FORWARD);
//        assertTrue(pet.getPosition().equals(new Vector2d(2, 4)) && pet.getOrientation() == MapDirection.NORTH);
//
//        pet.move(MoveDirection.RIGHT);
//        pet.move(MoveDirection.FORWARD);
//        pet.move(MoveDirection.FORWARD);
//        assertTrue(pet.getPosition().equals(new Vector2d(4, 4)) && pet.getOrientation() == MapDirection.EAST);
//
//        // Try to break eastern border
//        pet.move(MoveDirection.FORWARD);
//        assertTrue(pet.getPosition().equals(new Vector2d(4, 4)) && pet.getOrientation() == MapDirection.EAST);
//
//        pet = new Animal(new RectangularMap(5, 5), new Vector2d(2, 2));
//        pet.move(MoveDirection.BACKWARD);
//        pet.move(MoveDirection.BACKWARD);
//        assertTrue(pet.getPosition().equals(new Vector2d(2, 0)) && pet.getOrientation() == MapDirection.NORTH);
//
//        // Try to break southern border
//        pet.move(MoveDirection.BACKWARD);
//        assertTrue(pet.getPosition().equals(new Vector2d(2, 0)) && pet.getOrientation() == MapDirection.NORTH);
//
//        pet.move(MoveDirection.RIGHT);
//        pet.move(MoveDirection.BACKWARD);
//        pet.move(MoveDirection.BACKWARD);
//        assertTrue(pet.getPosition().equals(new Vector2d(0, 0)) && pet.getOrientation() == MapDirection.EAST);
//
//        // Try to break western border
//        pet.move(MoveDirection.BACKWARD);
//        assertTrue(pet.getPosition().equals(new Vector2d(0, 0)) && pet.getOrientation() == MapDirection.EAST);
//    }
//
//    @Test
//    public void testParser() {
//        String[] args = {"forward", "foo", "f", "bla", "backward", "", "b", "_+", "left", ";'", "l", "123",
//                "right", " ", "r"};
//        String[] args2 = {"forward", "f", "backward", "b", "left", "l", "right", "r"};
//        MoveDirection[] result = {MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.BACKWARD,
//                MoveDirection.BACKWARD, MoveDirection.LEFT, MoveDirection.LEFT, MoveDirection.RIGHT,
//                MoveDirection.RIGHT};
//        assertThrows(IllegalArgumentException.class, () -> OptionsParser.parse(args));
//        MoveDirection[] parsed = OptionsParser.parse(args2);
//        assertTrue(Arrays.equals(result, parsed));
//    }
//
//}
