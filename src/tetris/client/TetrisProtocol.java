/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Dec 4, 2010
 */
package tetris.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tetris.pieces.Piece;

/**
 * @author jpgunter
 * @version Dec 4, 2010
 */
public class TetrisProtocol
{
  /**
   * 
   */
  private static final String JOINED_ROOM_CONFIRM = "JOINED ROOM";
  /**
   * String that indicates the game is over.
   */
  private static final String GAME_OVER_STRING = "GAME OVER";
  /**
   * String that indicates the you win.
   */
  private static final String YOU_WIN_STRING = "YOU WON";
  /**
   * Input buffer.
   */
  private final BufferedReader my_in;
  /**
   * Output buffer.
   */
  private final PrintWriter my_out;
  /**
   * Whether or not the game is over.
   */
  private boolean my_game_is_over;
  /**
   * Whether or not this player won.
   */
  private boolean my_player_won;
  /**
   * Creates a new Tetris Protocol object that can be used to communicate with a Tetris Server.
   * @param the_socket The socket over which to communicate with the server.
   * @throws IOException Will throw an exception if it can't establish input/output buffers.
   */
  public TetrisProtocol(final Socket the_socket) throws IOException
  {
    my_out = new PrintWriter(the_socket.getOutputStream(), true);
    my_in = new BufferedReader(new InputStreamReader(the_socket.getInputStream()));
    my_game_is_over = false;
    my_player_won = false;
  }
  
  /**
   * Gets the list of games.
   * @return A List<String> of all the available games to join, each starts with a number
   * That number is the id of the game to join and should be passed to joinGame().
   */
  public List<String> getRoomList()
  {

    List<String> room_list = null;
    try
    {
      my_out.println("GET ROOM LIST");
      final String input = my_in.readLine();
      int num_rooms = 0;
      if (input != null)
      {
        num_rooms = Integer.parseInt(input);
      }
      room_list = new ArrayList<String>(num_rooms);
      
      for (int i = 0; i < num_rooms; i++)
      {
        room_list.add(my_in.readLine());
      }
    }
    catch (final IOException exception) 
    {
      System.err.println("FAILED TO GET ROOM LIST");
    }
    return room_list;
  }
  /**
   * Joins the specified game if it can.
   * @param the_room_id The id of the room to join
   * @return Whether the join succeeded or not.
   */
  public boolean joinGame(final int the_room_id)
  {
    boolean result = false;
    my_out.println("JOIN ROOM " + the_room_id);
    try
    {
      if (JOINED_ROOM_CONFIRM.equals(my_in.readLine()))
      {
        result = true;
      }
    }
    catch (final IOException exceptiun)
    {
      // TODO Auto-generated catch block
      result = false;
    }
    return result;
  }
  /**
   * Creates a room of the specified type, the player is immediately put into that game.
   * @param the_type The type of game to create, either RoomType.SINGLE or RoomType.DOUBLE.
   * @return Whether the creation succeeded, it should always succeed, but just in case.
   */
  public boolean createGame(final RoomType the_type)
  {
    boolean result = false;
    if (RoomType.SINGLE == the_type)
    {
      my_out.println("CREATE ROOM SINGLE");
    }
    else
    {
      my_out.println("CREATE ROOM DOUBLE");
    }
    try
    {
      if (JOINED_ROOM_CONFIRM.equals(my_in.readLine()))
      {
        result = true;
      }
    }
    catch (final IOException exceptiun)
    {
      // TODO Auto-generated catch block
      result = false;
    }
    return result;
    
    
  }
  
  /**
   * Gets whether the game is over.
   * @return Whether the game is over.
   */
  public boolean isGameOver()
  {
    return my_game_is_over;
  }
  /**
   * Gets whether this player won the game, does not get set until the game is over.
   * @return Whether this player won the game.
   */
  public boolean playerWon()
  {
    return my_player_won;
  }
  /**
   * Sets this player's name.
   * @param the_name The name of this player.
   */
  public void setName(final String the_name)
  {
    my_out.println(the_name);
  }
  
  /**
   * Sends move down command.
   */
  public void moveDown()
  {
    my_out.println("MOVE DOWN");
  }
  /**
   * Sends move left command.
   */
  public void moveLeft()
  {
    my_out.println("MOVE LEFT");
  }
  /**
   * Sends move right command.
   */
  public void moveRight()
  {
    my_out.println("MOVE RIGHT");
  }
  /**
   * Sends rotate command.
   */
  public void rotate()
  {
    my_out.println("ROTATE");
  }
  /**
   * Sends rotate command.
   */
  public void drop()
  {
    my_out.println("DROP");
  }
  /**
   * Gets the current board as a 2D character array.
   * @return The current board.
   */
  public char[][] getBoard()
  {

    my_out.println("GET BOARD");
    String input = "";
    char[][] temp_board = null;
    try
    {
      //check the first returned line, it will either be GAME OVER or the
      //width of the board.
      input = my_in.readLine();
      if (GAME_OVER_STRING.equals(input))
      {
        my_game_is_over = true;
      }
      else if (YOU_WIN_STRING.equals(input))
      {
        my_game_is_over = true;
        my_player_won = true;
      }
      else if (input != null)
      {
        //parse the width we got earlier.
        final int width = Integer.parseInt(input);
        input = my_in.readLine();
        if (input != null)
        {
          final int height = Integer.parseInt(input);
          temp_board = new char[height][width];
          
          for (int i = 0; i < height; i++)
          {          
            input = my_in.readLine();
            if (input != null)
            {
              temp_board[i] = input.trim().toCharArray();
            }
          }
        }
      }
    }
    catch (final IOException exception)
    {
      //if i get an IOexception return the empty board, run from CLI to see error messages
      System.err.println("Got IOException when trying to get board");
    }
    return temp_board;
  }
  /**
   * Gets the current board as a 2D character array.
   * @return The current board.
   */
  public char[][] getOpponentBoard()
  {

    my_out.println("GET OPPONENT BOARD");
    String input = "";
    char[][] temp_board = null;
    try
    {
      //check the first returned line, it will either be GAME OVER or the
      //width of the board.
      input = my_in.readLine();
      if (GAME_OVER_STRING.equals(input))
      {
        my_game_is_over = true;
      }
      else if (YOU_WIN_STRING.equals(input))
      {
        my_game_is_over = true;
        my_player_won = true;
      }
      else if (input != null)
      {
        //parse the width we got earlier.
        final int width = Integer.parseInt(input);
        input = my_in.readLine();
        if (input != null)
        {
          final int height = Integer.parseInt(input);
          temp_board = new char[height][width];
          
          for (int i = 0; i < height; i++)
          {          
            input = my_in.readLine();
            if (input != null)
            {
              temp_board[i] = input.trim().toCharArray();
            }
          }
        }
      }
    }
    catch (final IOException exception)
    {
      //if i get an IOexception return the empty board, run from CLI to see error messages
      System.err.println("Got IOException when trying to get opponent board");
    }
    return temp_board;
  }
  /**
   * Gets the next piece as a char[][] padded on all sides by at least one block.
   * @return The next piece.
   */
  public char[][] getNextPiece()
  {
    my_out.println("GET NEXT PIECE");
    String input = "";
    char[][] temp_board = null;
    try
    {
      input = my_in.readLine();
      //ignore the first line, thats the width
      if (input != null && !GAME_OVER_STRING.equals(input) && !YOU_WIN_STRING.equals(input))
      {
        //parse the width we got earlier.
        final int width = Integer.parseInt(input);
        input = my_in.readLine();
        if (input != null)
        {
          final int height = Integer.parseInt(input);
          //create the next piece char[][] with padding
          temp_board = new char[height + 2][width + 2];
          //pad the top and bottom with bg chars
          Arrays.fill(temp_board[0], '.');
          Arrays.fill(temp_board[temp_board.length - 1], '.');
          //start at one, to pad the top, stop at height to pad bottom
          for (int i = 1; i <= height; i++)
          {          
            input = my_in.readLine();
            if (input != null)
            {
              //pad the string with a bg char on either side, then convert to char[]
              final StringBuilder padded = new StringBuilder(".");
              final int padding_avail = Piece.MAX_WIDTH - width;
              //if i have more than 1 char on each side, pad it with that much
              for (int j = 0; j < padding_avail / 2; j++)
              {
                padded.append('.');
              }
              padded.append(input.trim());
              temp_board[i] = padded.toString().toCharArray();
            }
          }
        }
      }
    }
    catch (final IOException exception)
    {
      //if i get an IOexception return the empty board, run from CLI to see error messages
      System.err.println("Got IOException when trying to get next piece");
    }
    return temp_board;
  }

  /**
   * Gets the opponent that this player is playing against.
   * @return The opponent that this player is playing against.
   */
  public String getOpponent()
  {
    my_out.println("GET OPPONENT");
    String opponent;
    try
    {
      opponent = my_in.readLine();
    }
    catch (final IOException exception)
    {
      opponent = "GET OPPONENT FAILED";
    }
    return opponent;
  }

  /**
   * Gets the score.
   * @return The score as a string.
   */
  public String getScore()
  {
    my_out.println("GET SCORE");
    String score;
    try
    {
      score = my_in.readLine();
    }
    catch (final IOException exception)
    {
      score = "GET SCORE FAILED";
    }
    return score;
  }
  /**
   * Tells the server that this player quits.
   */
  public void quit()
  {
    my_out.println("QUIT");
  }

  /**
   * 
   */
  public void pause()
  {
    my_out.println("PAUSE");
  }
  /**
   * 
   */
  public void unpause()
  {
    my_out.println("UNPAUSE");
  }


}
