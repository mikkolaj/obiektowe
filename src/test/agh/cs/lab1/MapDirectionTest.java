package agh.cs.lab1;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapDirectionTest {
    @Test
    public void testNext () {
        assertTrue(MapDirection.NORTH.next() == MapDirection.EAST &&
           MapDirection.EAST.next() == MapDirection.SOUTH &&
           MapDirection.SOUTH.next() == MapDirection.WEST &&
           MapDirection.WEST.next() == MapDirection.NORTH);
    }
    @Test
    public void testPrevious () {
        assertTrue(MapDirection.NORTH.previous() == MapDirection.WEST &&
                MapDirection.EAST.previous() == MapDirection.NORTH &&
                MapDirection.SOUTH.previous() == MapDirection.EAST &&
                MapDirection.WEST.previous() == MapDirection.SOUTH);
    }
}
