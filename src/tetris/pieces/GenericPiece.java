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
public class GenericPiece implements Piece
{  
  /**
   * The point where the piece is on the board.
   */
  private transient Point my_point;

  /**
   * The array of chars that make up this piece.
   */
  private transient char[][] my_blocks;
  /**
   * The background char of the shape.
   */
  private final transient char my_bg_char;
  /**
   * Character representation of the piece. (I plan to use this later when i make my program
   * multi-player so that i can the gameboard as text instead of serializing it.)
   */
  private final transient char my_char;

  /**
   * Creates a new piece with the specified block array and initial coordinates. The 0,0 is
   * considered the upper-left hand corner of the board to conforming with regular Java
   * GUI coordinate systems.
   * @param the_blocks The array of blocks that make up the piece.
   * @param the_point The point where this piece is on the board.
   * @param the_char A character representation of the piece.
   */
  public GenericPiece(final char[][] the_blocks, final Point the_point,
                      final char the_char)
  {
    //just to be safe...Can't have someone changin' me blocks!
    my_blocks = the_blocks.clone();
    for (int i = 0; i < the_blocks.length; i++)
    {
      my_blocks[i] = the_blocks[i].clone();
    }
    
    my_point = the_point;
    
    my_bg_char = BG_CHARACTER;
    my_char = the_char;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Piece rotateRight()
  {
    final GenericPiece new_piece = (GenericPiece) this.clone();
    
    
    //create a block array to hold the new rotated blocks
    final char[][] rotated_piece = new char[my_blocks[0].length][my_blocks.length];
    
    //rotate the blocks
    for (int row = 0; row < rotated_piece.length; row++)
    {
      for (int col = 0; col < rotated_piece[row].length; col++)
      {
        rotated_piece[row][col] = my_blocks[(rotated_piece[row].length - 1) - col][row];
      }
    }
    //assign the new blocks to the cloned piece.
    new_piece.my_blocks = rotated_piece;

    return new_piece;
  }
  
  /**
   * {@inheritDoc}
   * @return 
   */
  @Override
  public Piece rotateLeft()
  {
    return rotateRight().rotateRight().rotateRight();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Point getPoint()
  {
    return my_point;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Piece moveRight()
  {
    final GenericPiece new_piece = (GenericPiece) this.clone();
    final Point old_point = new_piece.getPoint();
    new_piece.my_point = new Point(old_point.getX() + 1, old_point.getY());
    return new_piece;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Piece moveLeft()
  {
    final GenericPiece new_piece = (GenericPiece) this.clone();
    final Point old_point = new_piece.getPoint();
    new_piece.my_point = new Point(old_point.getX() - 1, old_point.getY());
    return new_piece;
  }
  /**
   * {@inheritDoc}
   */
  @Override
  public Piece moveDown()
  {
    final GenericPiece new_piece = (GenericPiece) this.clone();
    final Point old_point = new_piece.getPoint();
    new_piece.my_point = new Point(old_point.getX(), old_point.getY() + 1);
    return new_piece;
  }
  /**
   * {@inheritDoc}
   */
  @Override
  public Piece moveUp()
  {
    final GenericPiece new_piece = (GenericPiece) this.clone();
    final Point old_point = new_piece.getPoint();
    new_piece.my_point = new Point(old_point.getX(), old_point.getY() - 1);
    return new_piece;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public char[][] getBlocks()
  {
    final char[][] return_blocks = my_blocks.clone();
    for (int i = 0; i < my_blocks.length; i++)
    {
      return_blocks[i] = my_blocks[i].clone();
    }
    return return_blocks;
  }
  
  /**
   * {@inheritDoc}
   */
  public String toString()
  {
    final StringBuilder piece_string = new StringBuilder();
    
    piece_string.append(my_blocks[0].length);
    piece_string.append('\n');
    piece_string.append(my_blocks.length);
    piece_string.append('\n');
    for (int x = 0; x < my_blocks.length; x++)
    {
      for (int y = 0; y < my_blocks[x].length; y++)
      {
        
        if (my_blocks[x][y] == my_bg_char)
        {
          piece_string.append(".");
        }
        else
        {
          piece_string.append(getChar());
        }
      }
      if (x != my_blocks.length - 1)
      {
        piece_string.append("\n");
      }
    }
    
    return piece_string.toString();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public GenericPiece clone()
  {
    GenericPiece cloned_piece = null;
    try
    {
      cloned_piece = (GenericPiece) super.clone();
    }
    catch (final CloneNotSupportedException exception) 
    {
      throw new RuntimeException(exception);
    }
    cloned_piece.my_blocks = my_blocks.clone();
    for (int i = 0; i < my_blocks.length; i++)
    {
      cloned_piece.my_blocks[i] = my_blocks[i].clone();
    }
    return cloned_piece;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public char getBGchar()
  {
    return my_bg_char;
  }
  /**
   * {@inheritDoc}
   */
  @Override
  public char getChar()
  {
    return my_char;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean contains(final int the_x, final int the_y)
  {
    //first check that the x and y are within the bounds of the piece, then if the point
    //lies on an fg_char
    return the_x >= my_point.getX() && the_y >= my_point.getY() && 
           the_x <= (my_point.getX() + (my_blocks[0].length - 1)) && 
           the_y <= (my_point.getY() + (my_blocks.length - 1)) &&
           my_blocks[the_y - my_point.getY()][the_x - my_point.getX()] == my_char;

  }
  /**
   * Gets a new piece from a character. If it doesn't know the letter, 
   * then you get an O piece.
   * @param the_char The character representation of a piece.
   * @param the_x The initial x-coordinate.
   * @param the_y The initial y-coordinate.
   * @return A piece from its character representation.
   */
  public static Piece charToPiece(final char the_char, final int the_x, final int the_y)
  {
    Piece new_piece = null;
    switch (Character.toUpperCase(the_char))
    {
      case 'I':
        new_piece = new IPiece(the_x, the_y);
        break;
      case 'J':
        new_piece = new JPiece(the_x, the_y);
        break;
      case 'L':
        new_piece = new LPiece(the_x, the_y);
        break;
      case 'S':
        new_piece = new SPiece(the_x, the_y);
        break;
      case 'T':
        new_piece = new TPiece(the_x, the_y);
        break;
      case 'Z':
        new_piece = new ZPiece(the_x, the_y);
        break;
      default:
        new_piece = new OPiece(the_x, the_y);
        break;
    }
    return new_piece;
  }
  /**
   * {@inheritDoc}
   */
  public int getWidth()
  {
    return my_blocks[0].length;
  }
  /**
   * {@inheritDoc}
   */
  public int getHeight()
  {
    return my_blocks.length;
  }
}


