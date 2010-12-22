package tetris.testpieces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;



import org.junit.Test;

import tetris.pieces.IPiece;
import tetris.pieces.Piece;

/**
 * Tests that the pieces move around as they are supposed to.
 * 
 * @author jpgunter
 * @version November 19, 2010
 */
public class GenericTest
{
  /**
   * Move the shape one to the right.
   */
  @Test
  public void testMoveRight()
  {
    final IPiece test_piece = new IPiece(4, 4);
    final Piece actual_piece = (IPiece) test_piece.moveRight();

    final int actual_x = actual_piece.getPoint().getX();
    final int actual_y = actual_piece.getPoint().getY();
    assertEquals("Moved right one - test x", 5, actual_x);
    assertEquals("Moved right one - test y", 4, actual_y);
  }

  /**
   * Move the shape one to the left.
   */
  @Test
  public void testMoveLeft()
  {
    final IPiece test_piece = new IPiece(4, 4);
    final Piece actual_piece = (IPiece) test_piece.moveLeft();

    final int actual_x = actual_piece.getPoint().getX();
    final int actual_y = actual_piece.getPoint().getY();
    assertEquals("Moved left one - test x", 3, actual_x);
    assertEquals("Moved left one - test y", 4, actual_y);
  }

  /**
   * Move the shape one down.
   */
  @Test
  public void testMoveDown()
  {
    final IPiece test_piece = new IPiece(4, 4);
    final Piece actual_piece = (IPiece) test_piece.moveDown();

    final int actual_x = actual_piece.getPoint().getX();
    final int actual_y = actual_piece.getPoint().getY();
    assertEquals("Moved down one - test x", 4, actual_x);
    assertEquals("Moved down one - test y", 5, actual_y);
  }
  /**
   * Move the shape one up.
   */
  @Test
  public void testMoveUp()
  {
    final IPiece test_piece = new IPiece(4, 4);
    final Piece actual_piece = (IPiece) test_piece.moveUp();

    final int actual_x = actual_piece.getPoint().getX();
    final int actual_y = actual_piece.getPoint().getY();
    assertEquals("Moved up one - test x", 4, actual_x);
    assertEquals("Moved up one - test y", 3, actual_y);
  }
  /**
   * Moves the piece to -1,-1.
   */
  @Test
  public void testMoveIntoNegative()
  {
    final IPiece test_piece = new IPiece(0, 0);
    final Piece actual_piece = (IPiece) test_piece.moveUp().moveLeft();

    final int actual_x = actual_piece.getPoint().getX();
    final int actual_y = actual_piece.getPoint().getY();
    assertEquals("Moved to -1,-1 - test x", -1, actual_x);
    assertEquals("Moved to -1,-1 - test y", -1, actual_y);
  }
  
  /**
   * Test contains.
   */
  @Test
  public void testContains()
  {
    final Piece test_piece = new IPiece(4, 4);
    assertTrue("Point 4,4 contains a block", test_piece.contains(4, 4));
    assertTrue("Point 5,4 contains a block", test_piece.contains(5, 4));
    assertTrue("Point 6,4 contains a block", test_piece.contains(6, 4));
    assertTrue("Point 7,4 contains a block", test_piece.contains(7, 4));
  }
  /**
   * Test does not contain.
   */
  @Test
  public void testDoesntContain()
  {
    final Piece test_piece = new IPiece(4, 4);
    assertFalse("Point 4,5 doesn't contain a block", test_piece.contains(4, 5));
    assertFalse("Point 0,0 doesn't contain a block", test_piece.contains(0, 0));
    assertFalse("Point 3,4 doesn't contain a block", test_piece.contains(3, 4));
  }
  /**
   * test bg char.
   */
  @Test
  public void testBGchar()
  {
    final Piece test_piece = new IPiece(0, 0);
    assertEquals("Testing background char", '.', test_piece.getBGchar());
  }
  /**
   * make sure nothing changes in original after rotate.
   */
  @Test
  public void testNoChange()
  {
    final Piece test_piece = new IPiece(3, 4);
    final String actual = test_piece.toString();
    test_piece.rotateRight();
    final String expected = test_piece.toString();
    assertEquals("make sure piece is same before and after rotate", expected, actual);
  }
}
