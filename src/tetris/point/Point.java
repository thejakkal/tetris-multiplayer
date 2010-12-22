/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Nov 22, 2010
 */
package tetris.point;

/**
 * Represents a point on a Cartesian plane.
 * @author jpgunter
 * @version November 22, 2010
 */
public class Point
{
  /**
   * The x coordinate.
   */
  private final transient int my_x;
  /**
   * The y coordinate.
   */
  private final transient int my_y;
  
  /**
   * Constructs a point at the given Cartesian coordinates.
   * @param the_x The x coordinate.
   * @param the_y The y coordinate.
   */
  public Point(final int the_x, final int the_y)
  {
    my_x = the_x;
    my_y = the_y;
  }
  
  /**
   * Gets the x coordinate.
   * @return The x coordinate.
   */
  public int getX()
  {
    return my_x;
  }
  /**
   * Gets the y coordinate.
   * @return The y coordinate.
   */
  public int getY()
  {
    return my_y;
  }
  
  /**
   * Returns a String with format "(x,y)".
   * @return A String representation of a point.
   */
  @Override
  public String toString()
  {
    final StringBuilder point_string = new StringBuilder();
    
    point_string.append("(");
    point_string.append(my_x);
    point_string.append(",");
    point_string.append(my_y);
    point_string.append(")");
    
    return point_string.toString();
  }
  
  /**
   * Tests whether the_other is equal to this.
   * @param the_other The other object to compare this to.
   * @return Whether the objects are equal.
   */
  @Override
  public boolean equals(final Object the_other)
  {
    boolean result = false;
    if (the_other != null && getClass().equals(the_other.getClass()))
    {
      final Point other_point = (Point) the_other;
      if (my_x == other_point.my_x && my_y == other_point.my_y)
      {
        result = true;
      }
    }
    return result;
  }
  
  /**
   * Returns a hashcode of the point.
   * @return The hashcode of this point.
   */
  @Override
  public int hashCode()
  {
    return toString().hashCode();
  }
}
