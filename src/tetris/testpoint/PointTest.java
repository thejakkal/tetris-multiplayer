/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Nov 22, 2010
 */
package tetris.testpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import tetris.point.Point;

/**
 * Tests the Point class.
 * @author jpgunter
 * @version November 24, 2010
 */
public class PointTest
{
  /**
   * Tests whether a new shape has the correct x and y.
   */
  @Test
  public void testXY()
  {
    final Point test_point = new Point(4, 2);
    assertEquals("test whether x is in the right place", 4, test_point.getX());
    assertEquals("test whether y is in the right place", 2, test_point.getY());
  }
  /**
   * Tests the equals method.
   */
  @Test
  public void testEquals()
  {
    final Point point1 = new Point(4, 2);
    final Point point2 = new Point(4, 2);
    final Point point3 = new Point(5, 2);
    assertEquals("test point1 is equal to point1", point1, point1);
    assertEquals("test point1 is equal to point2", point1, point2);
    assertEquals("test point2 is equal to point3", point2, point1);
    assertFalse("test point1 is not equal to point3", point1.equals(point3));
  }
  /**
   * Tests toString().
   */
  @Test
  public void testToString()
  {
    final Point test_point = new Point(42, 9000);
    
    assertEquals("Test toString", "(42,9000)", test_point.toString());
  }
}
