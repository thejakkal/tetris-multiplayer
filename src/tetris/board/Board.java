/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Nov 20, 2010
 */
package tetris.board;

import java.util.List;
import java.util.Observable;
import java.util.Random;

import tetris.pieces.GenericPiece;
import tetris.pieces.Piece;
import tetris.point.Point;

/**
 * Represents a tetris game board.
 * @author jpgunter
 * @version November 20, 2010
 */
public class Board extends Observable
{
  /**
   * The number of points someone gets per line.
   */
  private static final int POINTS_PER_LINE = 100;
  /**
   * The number of points someone gets per line.
   */
  private static final int ROWS_ABOVE_BOARD = Piece.MAX_HEIGHT;
  /**
   * When pushing a line from the bottom this is the column of the block that will be
   * left out.
   */
  private static final int BLOCK_TO_LEAVE_OUT = 4;

  /**
   * The squares of the game board.
   */
  private final char[][] my_squares;
  /**
   * Current piece being dropped.
   */
  private Piece my_current_piece;
  /**
   * The next piece to be dropped.
   */
  private Piece my_next_piece;
  /**
   * Randomizer for getting random pieces.
   */
  private final Random my_random = new Random();
  /**
   * The player's score.
   */
  private int my_score;
  /**
   * List of pieces to use.
   */
  private final List<Piece> my_piece_list;
  /**
   * Number to keep track of which piece I am on.
   */
  private int my_piece_number;
  /**
   * Whether the game has ended or not.
   */
  private boolean my_is_game_over;
  /**
   * Number of lines to push on the player the next time a piece finishes dropping.
   */
  private int my_lines_queue;
  
  /**
   * Creates a board that will use random pieces.
   * @param the_height The height of the board.
   * @param the_width The width of the board.
   */
  public Board(final int the_height, final int the_width)
  {
    this(the_height, the_width, null);
  }
  
  /**
   * Creates a new board that will use the given list of pieces.
   * @param the_height The height of the board.
   * @param the_width The width of the board.
   * @param the_pieces The pieces to use on the board.
   */
  public Board(final int the_height, final int the_width, final List<Piece> the_pieces)
  {
    super();
    my_squares = new char[the_height + ROWS_ABOVE_BOARD][the_width];
    my_piece_list = the_pieces;
    my_piece_number = 0;
    my_current_piece = getNewNextPiece();
    my_next_piece = getNewNextPiece();
    initialFill();
    my_score = 0;
    my_is_game_over = false;
  }
  
  /**
   * Fills all cells of the board with the background character.
   */
  private void initialFill()
  {
    for (int row = 0; row < my_squares.length; row++)
    {
      for (int col = 0; col < my_squares[row].length; col++)
      {
        my_squares[row][col] = Piece.BG_CHARACTER;
      }
    }
    for (int row = 0; row < my_squares.length; row++)
    {
      for (int col = 0; col < my_squares[row].length; col++)
      {
        if (my_current_piece.contains(col, row))
        {
          my_squares[row][col] = my_current_piece.getChar();
        }
      }
    }
  }
  
  /**
   * Either gets the next piece in the piece_list or a random piece.
   * @return The next piece to use.
   */
  private Piece getNewNextPiece()
  {
    Piece next_piece = null;
    if (my_piece_list == null)
    {
      next_piece = getRandomPiece();
    }
    else
    {
      //return the current piece, then increment the piece counter.
      next_piece = my_piece_list.get(my_piece_number++ % my_piece_list.size());
    }
    return next_piece;
  }
  
  /**
   * Gets a random piece that is at position (width/2 - 2, 0).
   * @return A random piece.
   */
  private Piece getRandomPiece()
  {
    final char[] piece_chars = new char[] {'I', 'J', 'L', 'O', 'S', 'T', 'Z'};
    final int rand_int = Math.abs(my_random.nextInt() % piece_chars.length);
    final char selected_char = piece_chars[rand_int];
    Piece rand_piece = 
      GenericPiece.charToPiece(selected_char, my_squares[0].length / 2 - 2, 0);
    for (int i = 0; i < ROWS_ABOVE_BOARD - rand_piece.getHeight(); i++)
    {
      rand_piece = rand_piece.moveDown();
    }
    return rand_piece;
  }
  
  /**
   * Takes the current piece off the board.
   */
  private void removePiece()
  {
    final Point piece_point = my_current_piece.getPoint();
    final int start_row = piece_point.getY();
    final int end_row = start_row + my_current_piece.getHeight();
    final int start_col = piece_point.getX();
    final int end_col = start_col + my_current_piece.getWidth();
    
    for (int row = start_row; row <  end_row && row < my_squares.length; row++)
    {
      for (int col = start_col; col < end_col && col < my_squares[row].length; col++)
      {
        if (my_current_piece.contains(col, row))
        {
          my_squares[row][col] = Piece.BG_CHARACTER;
        }
      }
    }
  }
  
  /**
   * Adds the current piece to the board.
   */
  private void putPiece()
  {
    final Point piece_point = my_current_piece.getPoint();
    final int start_row = piece_point.getY();
    final int end_row = start_row + my_current_piece.getHeight();
    final int start_col = piece_point.getX();
    final int end_col = start_col + my_current_piece.getWidth();
    
    for (int row = start_row; row <  end_row && row < my_squares.length; row++)
    {
      for (int col = start_col; col < end_col && col < my_squares[row].length; col++)
      {
        if (my_current_piece.contains(col, row))
        {
          my_squares[row][col] = my_current_piece.getChar();
        }
      }
    }
  }
  
  /**
   * Checks to make sure the piece does not fall outside the bounds of the board.
   * @param the_piece The piece to check.
   * @return Whether or not the piece is within the bounds of the board.
   */
  private boolean boundsCheck(final Piece the_piece)
  {
    boolean result = false;
    
    if (the_piece.getPoint().getX() >= 0 && the_piece.getPoint().getY() >= 0 &&
       (the_piece.getPoint().getX() + the_piece.getWidth()) <= my_squares[0].length &&
       (the_piece.getPoint().getY() + the_piece.getHeight()) <= my_squares.length)
    {
      result = true;
    }
    return result;
  }
  
  /**
   * Checks to make sure this piece does not hit any other blocks.
   * @param the_piece The piece to check.
   * @return Whether or not the piece collides with other blocks on the board.
   */
  private boolean blocksCheck(final Piece the_piece)
  {
    boolean result = true;
    for (int row = 0; row < my_squares.length; row++)
    {
      for (int col = 0; col < my_squares[row].length; col++)
      {
        if (the_piece.contains(col, row) && my_squares[row][col] !=  '.')
        {
          result = false;
        }
      }
    }
    return result;
  }
  
  /**
   * Checks to see if there are any lines to clear and clears them.
   * @return How many lines were cleared.
   */
  private int checkLines()
  {
    int lines_cleared = 0;
    for (int row = 0; row < my_squares.length; row++)
    {
      for (int col = 0; col < my_squares[row].length; col++)
      {
        if (my_squares[row][col] == '.')
        {
          break;
        }
        else if (col == my_squares[0].length - 1)
        {
          clearRow(row);
          lines_cleared++;
        }
      }
    }
    return lines_cleared;
  }
  /**
   * Clears the current row and drops all others down one.
   * @param the_line The line number to clear (0 indexed).
   */
  private void clearRow(final int the_line)
  {
    for (int i = the_line; i > 0; i--)
    {
      for (int col = 0; col < my_squares[0].length; col++)
      {
        my_squares[i][col] = my_squares[i - 1][col];
      }
    }
    for (int col = 0; col < my_squares[0].length; col++)
    {
      my_squares[0][col] = '.';
    }
  }
  
  /**
   * Returns the currently dropping piece.
   * @return The currently dropping piece.
   */
  public Piece getCurrentPiece()
  {
    return my_current_piece.clone();
  }
  /**
   * Returns the currently dropping piece.
   * @return The currently dropping piece.
   */
  public Piece getNextPiece()
  {
    return my_next_piece.clone();
  }
  
  /**
   * Gets the board's score.
   * @return The board's score.
   */
  public int getScore()
  {
    return my_score;
  }
  
  /**
   * Returns whether the game is over.
   * @return If the game is over.
   */
  public boolean isGameOver()
  {
    return my_is_game_over;
  }
  /**
   * Gets a copy of the game board.
   * @return A copy of the game board.
   */
  public char[][] getBoard()
  {
    final char[][] cloned = my_squares.clone();
    for (int row = 0; row < my_squares.length; row++)
    {
      cloned[row] = my_squares[row].clone();
    }
    return cloned;
  }
  
  /**
   * Tries to move the piece down one, returns whether the move succeeded.
   * @return Whether the move succeeded.
   */
  public boolean moveDown()
  {
    boolean result = false;
    removePiece();  
    final Piece test_piece = my_current_piece.moveDown();
    if (boundsCheck(test_piece) && blocksCheck(test_piece))
    {
      my_current_piece = test_piece;
      result = true;
    }
    putPiece();
    setChanged();
    notifyObservers();
    return result;
  }
  /**
   * Tries to move the piece right one, returns whether the move succeeded.
   * @return Whether the move succeeded.
   */
  public boolean moveRight()
  {
    boolean result = false;
    removePiece();  
    final Piece test_piece = my_current_piece.moveRight();
    
    if (boundsCheck(test_piece) && blocksCheck(test_piece))
    {
      my_current_piece = test_piece;
      result = true;
    }
    putPiece();
    setChanged();
    notifyObservers();
    return result;
  }
  /**
   * Tries to move the piece left one, returns whether the move succeeded.
   * @return Whether the move succeeded.
   */
  public boolean moveLeft()
  {
    boolean result = false;
    removePiece();  
    final Piece test_piece = my_current_piece.moveLeft();
    if (boundsCheck(test_piece) && blocksCheck(test_piece))
    {
      my_current_piece = test_piece;
      result = true;
    }
    putPiece();
    setChanged();
    notifyObservers();
    return result;
  }
  /**
   * Tries to rotate the piece right one, returns whether the move succeeded.
   * @return Whether the move succeeded.
   */
  public boolean rotateRight()
  {
    boolean result = false;
    removePiece();  
    final Piece test_piece = my_current_piece.rotateRight();
    if (boundsCheck(test_piece) && blocksCheck(test_piece))
    {
      
      my_current_piece = test_piece;
      result = true;
    }
    putPiece();
    setChanged();
    notifyObservers();
    return result;
  }  
  /**
   * Tries to rotate the piece right one, returns whether the move succeeded.
   * @return Whether the move succeeded.
   */
  public boolean rotateLeft()
  {
    boolean result = false;
    removePiece();  
    final Piece test_piece = my_current_piece.rotateLeft();
    if (boundsCheck(test_piece) && blocksCheck(test_piece))
    {
      my_current_piece = test_piece;
      result = true;
    }
    putPiece();
    setChanged();
    notifyObservers();
    return result;
  }
  
  /**
   * Drops the current piece to the bottom and calls update to clear any lines made.
   */
  public void drop()
  {
    boolean move_result = moveDown();
    while (move_result)
    {
      move_result = moveDown();
    }
    update();
  }
  
  
  
  /**
   * Tries to move the piece down one, if it can't it checks for cleared lines and
   * increments the score.
   */
  public void update()
  {
    if (!moveDown() && !my_is_game_over)
    {
      final int lines_cleared = checkLines();
      if (lines_cleared > 0)
      {
        my_score += POINTS_PER_LINE * Math.pow(2, lines_cleared - 1);
        setChanged();
        notifyObservers(Integer.valueOf(lines_cleared));
      }

      final int lines_to_add = my_lines_queue;
      for (int i = 0; i < lines_to_add / 2; i++)
      {
        addLine();
      }
      my_lines_queue = 0;
      
      if (isAboveTop())
      {
        my_is_game_over = true;
      }
      my_current_piece = my_next_piece;
      my_next_piece = getNewNextPiece();
      putPiece();
      setChanged();
      notifyObservers(TetrisStatusChange.BOARD_CHANGED);
    } 
  }
  
  
  
  /**
   * 
   */
  private void addLine()
  {
    for (int i = 0; i < my_squares.length - 1; i++)
    {
      System.arraycopy(my_squares[i + 1], 0, my_squares[i], 0, my_squares[i + 1].length);
    }
    my_squares[my_squares.length - 1] = new char[my_squares[0].length];
    for (int i = 0; i < my_squares[0].length; i++)
    {
      my_squares[my_squares.length - 1][i] = 'X';
    }
    my_squares[my_squares.length - 1][BLOCK_TO_LEAVE_OUT] = '.';
  }

  /**
   * Checks whether the player's pieces have gone above the top of the board.
   * @return  Whether the player's pieces have gone above the top of the board.
   */
  private boolean isAboveTop()
  {
    boolean result = false;
    for (int row = 0; row < ROWS_ABOVE_BOARD; row++)
    {
      for (int col = 0; col < my_squares[row].length; col++)
      {
        if (my_squares[row][col] != '.')
        {
          result = true;
        }
      }
    }
    return result;
  }
  
  /**
   * Returns the string representation of the board, first the number of rows, then columns,
   * then a series of lines representing each row.
   * @return String representation of the board.
   */
  public String toString()
  {
    final StringBuilder board_string = new StringBuilder();
    board_string.append(my_squares[0].length);
    board_string.append('\n');
    board_string.append(my_squares.length - ROWS_ABOVE_BOARD);
    board_string.append('\n');
    for (int row = ROWS_ABOVE_BOARD; row < my_squares.length; row++)
    { 
      for (int col = 0; col < my_squares[row].length; col++)
      {
        board_string.append(my_squares[row][col]);
      }
      if (row != my_squares.length - 1)
      {
        board_string.append('\n');
      }
      
    }
    
    return board_string.toString();
  }

  /**
   * Queues lines to push onto the board the next a piece finishes dropping.
   * @param the_num_lines The number of lines to queue for pushing.
   */
  public void queueLines(final int the_num_lines)
  {
    my_lines_queue += the_num_lines;
    
  }
  
}
