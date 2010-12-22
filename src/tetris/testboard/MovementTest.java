/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Nov 12, 2010
 */
package tetris.testboard;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tetris.board.Board;
import tetris.pieces.GenericPiece;
import tetris.pieces.Piece;
import tetris.point.Point;
/**
 * Tests moving a piece on the board.
 * @author jpgunter
 * @version November 24, 2010
 */
public class MovementTest
{
  /**
   * Test left bound.
   */
  @Test
  public void testLeftBound()
  {
    final List<Piece> piece_list = new ArrayList<Piece>();
    piece_list.add(GenericPiece.charToPiece('I', 0, 0));
    final Board test_board = new Board(20, 10, piece_list);
    for (int i = 0; i < 12; i++)
    {
      test_board.moveLeft();
    }
    final Point expected = new Point(0, 0);
    final Point actual = test_board.getCurrentPiece().getPoint();
    assertEquals("Moved too far left", expected, actual);
  }
  /**
   * Test right bound.
   */
  @Test
  public void testRightBound()
  {
    final List<Piece> piece_list = new ArrayList<Piece>();
    piece_list.add(GenericPiece.charToPiece('I', 0, 0));
    final Board test_board = new Board(20, 10, piece_list);
    for (int i = 0; i < 12; i++)
    {
      test_board.moveRight();
    }
    final Point expected = new Point(10 - test_board.getCurrentPiece().getWidth(), 0);
    final Point actual = test_board.getCurrentPiece().getPoint();
    assertEquals("Moved too far right", expected, actual);
  }
  
  /**
   * Test bottom bound.
   */
  @Test
  public void testBottomBound()
  {
    final List<Piece> piece_list = new ArrayList<Piece>();
    piece_list.add(GenericPiece.charToPiece('I', 0, 0));
    final Board test_board = new Board(20, 10, piece_list);
    for (int i = 0; i < 10; i++)
    {
      test_board.moveLeft();
    }
    for (int i = 0; i < 28; i++)
    {
      test_board.moveDown();
    }
    final Point expected = new Point(0, 24 - test_board.getCurrentPiece().getHeight());
    final Point actual = test_board.getCurrentPiece().getPoint();
    assertEquals("Moved too far down", expected, actual);
  }
}
