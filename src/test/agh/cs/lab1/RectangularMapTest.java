package agh.cs.lab1;

import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularMapTest {

    @Test
    public void testIsOccupied() {
        RectangularMap map = new RectangularMap(5, 5);
        Vector2d position =  new Vector2d(2, 2);
        new Animal(map, position);
        assertTrue(map.isOccupied(position));
        assertFalse(map.isOccupied(new Vector2d(-1, -1)));
        assertFalse(map.isOccupied(new Vector2d(0, 0)));
    }

    @Test
    public void testCanMoveTo() {
        RectangularMap map = new RectangularMap(5, 5);
        Vector2d vec1 = new Vector2d(0, 0);
        Vector2d vec2 = new Vector2d(4, 4);
        Vector2d vec3 = new Vector2d(0, 4);
        Vector2d vec4 = new Vector2d(4, 0);
        Vector2d vec5 = new Vector2d(-1, 0);
        Vector2d vec6 = new Vector2d(0, -1);
        Vector2d vec7 = new Vector2d(0, 5);
        Vector2d vec8 = new Vector2d(5, 0);
        assertTrue(map.canMoveTo(vec1));
        assertTrue(map.canMoveTo(vec2));
        assertTrue(map.canMoveTo(vec3));
        assertTrue(map.canMoveTo(vec4));
        assertFalse(map.canMoveTo(vec5));
        assertFalse(map.canMoveTo(vec6));
        assertFalse(map.canMoveTo(vec7));
        assertFalse(map.canMoveTo(vec8));
    }

    @Test
    public void testObjectAt() {
        RectangularMap map = new RectangularMap(5, 5);
        Vector2d position =  new Vector2d(2, 2);
        Animal animal1 = new Animal(map, position);
        assertTrue(map.objectAt(position).equals(animal1));
        assertNull(map.objectAt(new Vector2d(-1, -1)));
        assertNull(map.objectAt(new Vector2d(0, 0)));
    }
}