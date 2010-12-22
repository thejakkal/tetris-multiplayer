/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Dec 3, 2010
 */
package tetris.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * @author jpgunter
 * @version Dec 3, 2010
 */
public class BoardPanel extends JPanel
{
  /**
   * Serialize ID.
   */
  private static final long serialVersionUID = -1230066313296166509L;
  /**
   * Minimum width of a cell in pixels.
   */
  private static final int MIN_WIDTH = 4;
  /**
   * Minimum height of a cell in pixels.
   */
  private static final int MIN_HEIGHT = 4;
  /**
   * preferred size of a cell.
   */
  private static final int PREFERRED_CELL_SIZE = 20;
  /**
   * Default font, used when showing a message.
   */
  private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 16);
  /**
   * current board this panel is drawing.
   */
  private char[][] my_board;
  /**
   * width of the board in number of blocks.
   */
  private final int my_cols;
  /**
   * height of the board in number of blocks.
   */
  private final  int my_rows;
  /**
   * Message to print on the board.
   */
  private String my_message;
  /**
   * boolean whether to show the message, set to false to show the board.
   */
  private boolean my_show_message;
  
  /**
   * Creates a BoardPanel of the specified width, height, and with an initial board.
   * @param the_width The width of the board in number of blocks.
   * @param the_height The height of the board in number of blocks.
   * @param the_board The initial board that will be drawn.
   */
  public BoardPanel(final int the_width, final int the_height, final char[][] the_board)
  {
    super();
    my_cols = the_width;
    my_rows = the_height;
    final char[][] temp_board = new char[the_board.length][the_board[0].length];
    for (int i = 0; i < the_board.length; i++)
    {
      temp_board[i] = the_board[i].clone();
    }
    my_board = temp_board;
    setPreferredSize(new Dimension(my_cols * PREFERRED_CELL_SIZE, 
                                   my_rows * PREFERRED_CELL_SIZE));
  }
  /**
   * {@inheritDoc}
   */
  @Override
  public void paintComponent(final Graphics the_graphics)
  {
    super.paintComponents(the_graphics);
    final Graphics2D graphics_2d = (Graphics2D) the_graphics;
    
    graphics_2d.setColor(Color.GRAY);
    graphics_2d.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
    if (my_show_message)
    {
      graphics_2d.setColor(Color.WHITE);
      graphics_2d.setFont(DEFAULT_FONT);
      graphics_2d.drawString(my_message, PREFERRED_CELL_SIZE, 
                             my_rows * PREFERRED_CELL_SIZE / 2); 
    }
    else
    {
      int x = 0;
      int y = 0;
      final int width = getCellWidth();
      final int height = getCellHeight();
      for (char[] board_row : my_board)
      {
        for (char cell_char : board_row)
        {
          if (cell_char != '.')
          {
            graphics_2d.setColor(getPieceColor(cell_char));
            graphics_2d.fill(new Rectangle2D.Double(x, y, width, height));
          }
          x += width;
        }
        
        x = 0;
        y += height;
        
      }
      for (int i = 0; i < getHeight(); i += height)
      {
        graphics_2d.setColor(Color.WHITE);
        graphics_2d.drawLine(0, i, getWidth(), i);
      }
      for (int i = 0; i < getWidth(); i += width)
      {
        graphics_2d.setColor(Color.WHITE);
        graphics_2d.drawLine(i, 0, i, getHeight());
      }
    }
    
  }
  
  /**
   * Sets the board.
   * @param the_board The board that will drawn by this panel.
   */
  public void setBoard(final char[][] the_board)
  {
    final char[][] temp_board = the_board.clone();
    for (int i = 0; i < the_board.length; i++)
    {
      temp_board[i] = the_board[i].clone();
    }
    my_board = temp_board;
  }
  
  /**
   * Calculates how wide each cell should be.
   * @return How wide each cell should be.
   */
  private int getCellWidth()
  {
    int width = getWidth() / my_cols;
    if (width < MIN_WIDTH)
    {
      width = MIN_WIDTH;
    }
    return width;
  }
  /**
   * Calculates how high each cell should be.
   * @return How high each cell should be.
   */
  private int getCellHeight()
  {
    int height = getHeight() / my_rows;
    if (height < MIN_HEIGHT)
    {
      height = MIN_HEIGHT;
    }
    return height;
  }
  
  /**
   * Returns the correct color for the given piece type.
   * @param the_char Gets the color based on a given piece's char.
   * @return The color of the piece.
   */
  private Color getPieceColor(final char the_char)
  {
    Color piece_color = null;
    switch (the_char)
    {
      case 'I':
        piece_color = Color.RED;
        break;
      case 'J':
        piece_color = Color.YELLOW;
        break;
      case 'L':
        piece_color = Color.MAGENTA;
        break;
      case 'O':
        piece_color = Color.BLUE;
        break;
      case 'S':
        piece_color = Color.GREEN;
        break;
      case 'T':
        piece_color = Color.ORANGE;
        break;
      case 'Z':
        piece_color = Color.CYAN;
        break;
      case 'X':
        piece_color = Color.PINK;
        break;
      default:
        piece_color = Color.DARK_GRAY;
        break;
    }
    return piece_color;
  }
  
  /**
   * Switches drawing mode back to drawing the board.
   */
  public void showBoard()
  {
    my_show_message = false;
  }
  
  /**
   * Clears the board and tries it's best to display the supplied string.
   * @param the_message The message to display.
   */
  public void showMessage(final String the_message)
  {
    my_message = the_message;
    my_show_message = true;
  }
  
}
