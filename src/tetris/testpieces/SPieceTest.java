/*
 * James Gunter Project TCSS 305 Autumn 2010 Assignment Nov 12, 2010
 */

package tetris.testpieces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.util.Arrays;

import org.junit.Test;


import tetris.pieces.Piece;
import tetris.pieces.SPiece;

/**
 * @author jpgunter
 * @version Nov 12, 2010
 */
public class SPieceTest
{
  /**
   * Create an SPiece, test all rotations to the right.
   */
  @Test
  public void testRotateRight()
  {

    Piece actual_piece = new SPiece(0, 0);
    char[][] expected = 
      new char[][] {new char[] {Piece.BG_CHARACTER, SPiece.CHARACTER, SPiece.CHARACTER},
                    new char[] {SPiece.CHARACTER, SPiece.CHARACTER, Piece.BG_CHARACTER}};
    assertTrue("New SPiece before rotation right", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));
    actual_piece = actual_piece.rotateRight();
    expected = new char[][] {new char[] {SPiece.CHARACTER, Piece.BG_CHARACTER},
                             new char[] {SPiece.CHARACTER, SPiece.CHARACTER},
                             new char[] {Piece.BG_CHARACTER, SPiece.CHARACTER}};
    assertTrue("Rotated once to the right", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));

    actual_piece = actual_piece.rotateRight();
    expected = 
      new char[][] {new char[] {Piece.BG_CHARACTER, SPiece.CHARACTER, SPiece.CHARACTER},
                    new char[] {SPiece.CHARACTER, SPiece.CHARACTER, Piece.BG_CHARACTER}};
    assertTrue("Rotated twice to the right, back to original", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));

  }
  /**
   * Create an SPiece, test all rotations to the Left.
   */
  @Test
  public void testRotateLeft()
  {

    Piece actual_piece = new SPiece(0, 0);
    char[][] expected = 
      new char[][] {new char[] {Piece.BG_CHARACTER, SPiece.CHARACTER, SPiece.CHARACTER},
                    new char[] {SPiece.CHARACTER, SPiece.CHARACTER, Piece.BG_CHARACTER}};
    assertTrue("New SPiece before rotation Left", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));
    actual_piece = actual_piece.rotateLeft();
    expected = new char[][] {new char[] {SPiece.CHARACTER, Piece.BG_CHARACTER},
                             new char[] {SPiece.CHARACTER, SPiece.CHARACTER},
                             new char[] {Piece.BG_CHARACTER, SPiece.CHARACTER}};
    assertTrue("Rotated once to the Left", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));

    actual_piece = actual_piece.rotateLeft();
    expected = 
      new char[][] {new char[] {Piece.BG_CHARACTER, SPiece.CHARACTER, SPiece.CHARACTER},
                    new char[] {SPiece.CHARACTER, SPiece.CHARACTER, Piece.BG_CHARACTER}};
    assertTrue("Rotated twice to the Left, back to original", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));

  }
  /**
   * Tests the toString method.
   */
  @Test
  public void testToString()
  {
    final Piece test_piece = new SPiece(0, 0);

    final String actual = test_piece.toString();
    assertEquals("Testing toString.", "3\n2\n.SS\nSS.", actual);
  }

  /**
   * tests the char.
   */
  @Test
  public void testChar()
  {
    final Piece test_piece = new SPiece(0, 0);
    assertEquals("Test the character of the SPiece", 'S', test_piece.getChar());
  }
}
