package agh.cs.lab1;

import org.junit.Test;
import static org.junit.Assert.*;

public class Vector2dTest {
    @Test
    public void testEquals () {
        Vector2d vec1 = new Vector2d(1, 1);
        Vector2d vec2 = new Vector2d(1, 1);
        Vector2d vec3 = new Vector2d(1, 2);
        assertTrue(vec1.equals(vec2) &&
                    vec1.equals(vec1) &&
                    !vec1.equals(vec3) &&
                    !vec1.equals(1));
    }

    @Test
    public void testToString() {
        Vector2d vec1 = new Vector2d(1, 1);
        assertEquals(vec1.toString(), "(1, 1)");
    }

    @Test
    public void testPrecedes() {
        Vector2d vec1 = new Vector2d(1, 1);
        Vector2d vec2 = new Vector2d(1, 1);
        Vector2d vec3 = new Vector2d(1, 2);
        Vector2d vec4 = new Vector2d(2, 1);
        Vector2d vec5 = new Vector2d(2, 2);
        assert(vec1.precedes(vec2) && vec1.precedes(vec3) &&
               vec1.precedes(vec4) && vec1.precedes(vec5) &&
               !vec5.precedes(vec4) && !vec5.precedes(vec3) &&
               !vec5.precedes(vec1));
    }

    @Test
    public void testFollows() {
        Vector2d vec1 = new Vector2d(1, 1);
        Vector2d vec2 = new Vector2d(1, 1);
        Vector2d vec3 = new Vector2d(1, 2);
        Vector2d vec4 = new Vector2d(2, 1);
        Vector2d vec5 = new Vector2d(2, 2);
        assert(vec1.follows(vec2) && !vec1.follows(vec3) &&
                !vec1.follows(vec4) && !vec1.follows(vec5) &&
                vec5.follows(vec4) && vec5.follows(vec3) &&
                vec5.follows(vec1));
    }

    // in following tests I use .equals() method as it is tested before
    // and those tests prove it returns appropriate results
    @Test
    public void testUpperRight() {
        Vector2d vec1 = new Vector2d(1, 1);
        Vector2d vec3 = new Vector2d(1, 2);
        Vector2d vec4 = new Vector2d(2, 1);
        Vector2d vec5 = new Vector2d(2, 2);
        assert(vec1.upperRight(vec1).equals(vec1) && vec1.upperRight(vec3).equals(vec3) &&
                vec3.upperRight(vec1).equals(vec3) && vec1.upperRight(vec4).equals(vec4) &&
                vec4.upperRight(vec1).equals(vec4) && vec1.upperRight(vec5).equals(vec5) &&
                vec5.upperRight(vec1).equals(vec5));
    }

    @Test
    public void testLowerLeft() {
        Vector2d vec1 = new Vector2d(1, 1);
        Vector2d vec3 = new Vector2d(1, 2);
        Vector2d vec4 = new Vector2d(2, 1);
        Vector2d vec5 = new Vector2d(2, 2);
        assert(vec1.lowerLeft(vec1).equals(vec1) && vec1.lowerLeft(vec3).equals(vec1) &&
                vec3.lowerLeft(vec1).equals(vec1) && vec1.lowerLeft(vec4).equals(vec1) &&
                vec4.lowerLeft(vec1).equals(vec1) && vec1.lowerLeft(vec5).equals(vec1) &&
                vec5.lowerLeft(vec1).equals(vec1));
    }

    @Test
    public void testAdd() {
        Vector2d vec1 = new Vector2d(1, -1);
        Vector2d vec2 = new Vector2d(-1, 1);
        Vector2d vec3 = new Vector2d(0, 0);
        assertTrue(vec1.add(vec2).equals(vec3));
    }

    @Test
    public void testSubtract() {
        Vector2d vec1 = new Vector2d(1, -1);
        Vector2d vec2 = new Vector2d(-1, 1);
        Vector2d vec3 = new Vector2d(2, -2);
        assertTrue(vec1.subtract(vec2).equals(vec3));
    }

    @Test
    public void testOpposite() {
        Vector2d vec1 = new Vector2d(1, -1);
        Vector2d vec2 = new Vector2d(-1, 1);
        assertTrue(vec1.opposite().equals(vec2) && vec2.opposite().equals(vec1));
    }
}
