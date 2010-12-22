/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Nov 11, 2010
 */
package tetris.pieces;

import tetris.point.Point;



/**
 * @author jpgunter
 * @version Nov 11, 2010
 */
public class SPiece extends GenericPiece
{
  /**
   * The character that represents this piece. 
   */
  public static final char CHARACTER = 'S';
  
  /**
   * Creates an S piece and the given initial point. The 0,0 is
   * considered the upper-left hand corner of the board to conforming with regular Java
   * GUI coordinate systems.
   * 
   * Default Position:
   *    [#][#]
   * [#][#]
   * 
   * @param the_x The initial x coordinate on the game board.
   * @param the_y The initial y coordinate on the game board.
   */
  public SPiece(final int the_x, final int the_y)
  {
    super(new char[][]{new char[] {Piece.BG_CHARACTER, CHARACTER, CHARACTER},
                       new char[] {CHARACTER, CHARACTER, Piece.BG_CHARACTER}},
                       new Point(the_x, the_y), CHARACTER);
    
  }
}
