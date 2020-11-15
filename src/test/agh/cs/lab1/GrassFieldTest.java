package agh.cs.lab1;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class GrassFieldTest {
    @Test
    public void testIsOccupiedAndCanMoveTo() {
        GrassField field = new GrassField(1);
        Map<Vector2d, Grass> grassMap = field.getGrassMap();
        Vector2d grassPos = grassMap.keySet().stream().findFirst().get();
        assertTrue(field.isOccupied(grassPos));
        assertTrue(field.canMoveTo(grassPos));

        Vector2d newPos = grassPos.add(new Vector2d(1, 1));
        assertFalse(field.isOccupied(newPos));
        assertTrue(field.canMoveTo(newPos));

        new Animal(field, newPos);
        assertTrue(field.isOccupied(newPos));
        assertFalse(field.canMoveTo(newPos));

        new Animal(field, grassPos);
        assertTrue(field.isOccupied(grassPos));
        assertFalse(field.canMoveTo(grassPos));

        Vector2d outOfMap = new Vector2d(-1, -1);
        assertFalse(field.isOccupied(outOfMap));
        assertTrue(field.canMoveTo(outOfMap));
    }

    @Test
    public void testObjectAt() {
        GrassField field = new GrassField(1);
        Map<Vector2d, Grass> grassMap = field.getGrassMap();
        Vector2d grassPos = grassMap.keySet().stream().findFirst().get();
        Grass grass = grassMap.get(grassPos);
        assertTrue(field.objectAt(grassPos).equals(grass));

        Animal animal1 = new Animal(field, grassPos);
        assertTrue(field.objectAt(grassPos).equals(animal1));

        Vector2d newPos = grassPos.add(new Vector2d(1, 1));
        assertTrue(field.objectAt(newPos) == null);
        Animal animal2 = new Animal(field, newPos);
        assertTrue(field.objectAt(newPos).equals(animal2));

    }
}
