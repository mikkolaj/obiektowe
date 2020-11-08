package agh.cs.lab1;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapDirectionTest {
    @Test
    public void testNext () {
        assertTrue(MapDirection.NORTH.next() == MapDirection.EAST);
        assertTrue(MapDirection.EAST.next() == MapDirection.SOUTH);
        assertTrue(MapDirection.SOUTH.next() == MapDirection.WEST);
        assertTrue(MapDirection.WEST.next() == MapDirection.NORTH);
    }
    @Test
    public void testPrevious () {
        assertTrue(MapDirection.NORTH.previous() == MapDirection.WEST);
        assertTrue(MapDirection.EAST.previous() == MapDirection.NORTH);
        assertTrue(MapDirection.SOUTH.previous() == MapDirection.EAST);
        assertTrue(MapDirection.WEST.previous() == MapDirection.SOUTH);
    }
}
