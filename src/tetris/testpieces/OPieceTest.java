/*
 * James Gunter Project TCSS 305 Autumn 2010 Assignment Nov 12, 2010
 */

package tetris.testpieces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.util.Arrays;

import org.junit.Test;

import tetris.pieces.OPiece;
import tetris.pieces.Piece;

/**
 * @author jpgunter
 * @version Nov 12, 2010
 */
public class OPieceTest
{

  /**
   * Create an OPiece, test all rotations to the right.
   */
  @Test
  public void testRotateRight()
  {

    Piece actual_piece = new OPiece(0, 0);
    char[][] expected = new char[][] {new char[] {OPiece.CHARACTER, OPiece.CHARACTER},
                                      new char[] {OPiece.CHARACTER, OPiece.CHARACTER}};
    assertTrue("New OPiece before rotation right", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));
    actual_piece = actual_piece.rotateRight();
    expected = new char[][] {new char[] {OPiece.CHARACTER, OPiece.CHARACTER},
                             new char[] {OPiece.CHARACTER, OPiece.CHARACTER}};
    assertTrue("Rotated once to the right, should be back to original", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));

  } 
  
  /**
   * Create an OPiece, test all rotations to the Left.
   */
  @Test
  public void testRotateLeft()
  {

    Piece actual_piece = new OPiece(0, 0);
    char[][] expected = new char[][] {new char[] {OPiece.CHARACTER, OPiece.CHARACTER},
                                      new char[] {OPiece.CHARACTER, OPiece.CHARACTER}};
    assertTrue("New OPiece before rotation Left", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));
    actual_piece = actual_piece.rotateLeft();
    expected = new char[][] {new char[] {OPiece.CHARACTER, OPiece.CHARACTER},
                             new char[] {OPiece.CHARACTER, OPiece.CHARACTER}};
    assertTrue("Rotated once to the Left, should be back to original", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));

  } 
  /**
   * Tests the toString method.
   */
  @Test
  public void testToString()
  {
    final Piece test_piece = new OPiece(0, 0);

    final String actual = test_piece.toString();
    assertEquals("Testing toString.", "2\n2\nOO\nOO", actual);
  }

  /**
   * tests the char.
   */
  @Test
  public void testChar()
  {
    final Piece test_piece = new OPiece(0, 0);
    assertEquals("Test the character of the OPiece", 'O', test_piece.getChar());
  }
}
