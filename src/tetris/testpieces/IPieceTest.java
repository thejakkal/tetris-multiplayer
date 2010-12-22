/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Nov 12, 2010
 */
package tetris.testpieces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.util.Arrays;

import org.junit.Test;

import tetris.pieces.IPiece;
import tetris.pieces.Piece;

/**
 * @author jpgunter
 * @version Nov 12, 2010
 */
public class IPieceTest
{

  /**
   * Create an IPiece, test all rotations to the right.
   */
  @Test
  public void testRotateRight()
  {

    Piece actual_piece = new IPiece(0, 0);
    char[][] expected = new char[][]{new char[]{IPiece.CHARACTER,
                                                IPiece.CHARACTER,
                                                IPiece.CHARACTER,
                                                IPiece.CHARACTER}};
    assertTrue("New IPiece before rotation right", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));
    actual_piece = actual_piece.rotateRight();
    expected = new char[][]{new char[]{IPiece.CHARACTER},
                            new char[]{IPiece.CHARACTER},
                            new char[]{IPiece.CHARACTER},
                            new char[]{IPiece.CHARACTER}};
    assertTrue("Rotated once to the right", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));

    actual_piece = actual_piece.rotateRight();
    expected = new char[][]{new char[]{IPiece.CHARACTER,
                                       IPiece.CHARACTER,
                                       IPiece.CHARACTER,
                                       IPiece.CHARACTER}};
    assertTrue("Rotated twice to the right, back to original", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));

  }
  
  /**
   *  Create an IPiece, test all rotations to the left.
   */
  @Test
  public void testRotateLeft()
  {

    Piece actual_piece = new IPiece(0, 0);
    char[][] expected = new char[][]{new char[]{IPiece.CHARACTER,
                                                IPiece.CHARACTER,
                                                IPiece.CHARACTER,
                                                IPiece.CHARACTER}};
    assertTrue("New IPiece before rotation left", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));
    
    
    actual_piece = actual_piece.rotateLeft();
    expected = new char[][]{new char[]{IPiece.CHARACTER},
                            new char[]{IPiece.CHARACTER},
                            new char[]{IPiece.CHARACTER},
                            new char[]{IPiece.CHARACTER}};
    assertTrue("Rotated once to the left", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));
  

    actual_piece = actual_piece.rotateLeft();
    expected = new char[][]{new char[]{IPiece.CHARACTER,
                                       IPiece.CHARACTER,
                                       IPiece.CHARACTER,
                                       IPiece.CHARACTER}};
    assertTrue("Rotated twice to the left, back to original", 
               Arrays.deepEquals(expected, actual_piece.getBlocks()));

  
  }
  /**
   * Tests the toString method.
   */
  @Test
  public void testToString()
  {
    final Piece test_piece = new IPiece(0, 0);

    final String actual = test_piece.toString();
    assertEquals("Testing toString.", "4\n1\nIIII", actual);
  }
  
  /**
   * tests the char.
   */
  @Test
  public void testChar()
  {
    final Piece test_piece = new IPiece(0, 0);
    assertEquals("Test the character of the IPiece", 'I', test_piece.getChar());
  }
}
