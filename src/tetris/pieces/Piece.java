/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Nov 11, 2010
 */
package tetris.pieces;

import tetris.point.Point;

/**
 * 
 * @author jpgunter
 * @version November 16, 2010
 */
public interface Piece extends Cloneable
{
  /**
   * The background char for all pieces.
   */
  char BG_CHARACTER = '.';
  /**
   * Maximum height of a piece.
   */
  char MAX_WIDTH = 4;
  /**
   * Maximum height of a piece.
   */
  char MAX_HEIGHT = 4;
  /**
   * Rotates the piece to the right.
   * @return Piece The piece that would result from rotating this piece right.
   */
  Piece rotateRight();

  /**
   * Rotates the piece to the left.
   * @return Piece The piece that would result from rotating this piece left.
   */
  Piece rotateLeft();

  /**
   * Gets the point where the piece is located.
   * @return The point where the piece is located.
   */
  Point getPoint();

  /**
   * Gets the piece that would result from moving this piece 
   * one to the right(increases x coordinate).
   * @return The new piece moved one to the right.
   */
  Piece moveRight();

  /**
   * Gets the piece that would result from moving this piece 
   * one to the left(decreases x coordinate).
   * @return The new piece moved one to the left.
   */
  Piece moveLeft();

  /**
   * Gets the piece that would result from moving this piece  
   * one down(increases y coordinate).
   * @return The new piece moved one down.
   */
  Piece moveDown();

  /**
   * Gets the piece that would result from moving this piece  
   * one up(decreases y coordinate).
   * @return The new piece moved one up.
   */
  Piece moveUp();

  /**
   * Gets the array of chars that makes up the piece.
   * @return The array of chars that makes up the piece.
   */
  char[][] getBlocks();
  
  /**
   * Gets the background char of the piece.
   * @return The background char of the piece.
   */
  char getBGchar();
  /**
   * Gets a character that indicates the shape of the piece.
   * @return A character that indicates the shape of the piece.
   */
  char getChar();
  /**
   * Checks whether a point in space contains one of this pieces blocks.
   * @param the_x The x coordinate.
   * @param the_y The y coordinate.
   * @return whether the point contains one of this pieces blocks.
   */
  boolean contains(final int the_x, final int the_y);
  /**
   * Gets the width of the piece.
   * @return The width of the piece.
   */
  int getWidth();
  /**
   * Gets the height of the piece.
   * @return The height of the piece.
   */
  int getHeight();
  
  /**
   * Clones a Piece.
   * @return An exact copy of the Piece.
   */
  Piece clone();
}
