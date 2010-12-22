/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Nov 30, 2010
 */
package tetris.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import tetris.board.Board;
import tetris.pieces.Piece;

/**
 * @author jpgunter
 * @version Nov 30, 2010
 */
public class GameController implements Runnable, Observer
{
  /**
   * Name of the player.
   */
  private final String my_name;
  /**
   * The name of the player's opponent.
   */
  private String my_opponent;
  /**
   * The game board.
   */
  private final Board my_board;
  /**
   * Reference to the parent Room.
   */
  private final Room my_room;
  /**
   * Variable that will be set when this player wins.
   */
  private boolean my_have_won;
  /**
   * A number given to this game to identify it.
   */
  private final int my_game_number;
  /**
   * Socket the client is connected on.
   */
  private final Socket my_client_socket;
  /**
   * output buffer.
   */
  private PrintWriter my_out;
  /**
   * Input buffer.
   */
  private BufferedReader my_in;
  /**
   * Creates a game controller.
   * @param the_room The room that this game is in.
   * @param the_game_number The number given to this game to identify it its room.
   * @param the_height The height of the game board.
   * @param the_width The width of the game board.
   * @param the_pieces A list of pieces that all the game controllers in 
   *                   the room will use.
   * @param the_client_socket The socket over which this controller will 
   *                          communicate with the client.
   * @param the_name The name of this game's player.
   */
  public GameController(final Room the_room, final int the_game_number, 
                        final int the_height, final int the_width, 
                        final List<Piece> the_pieces, final Socket the_client_socket,
                        final String the_name)
  {
    my_game_number = the_game_number;
    my_name = the_name;
    my_board = new Board(the_height, the_width, the_pieces);
    my_board.addObserver(this);
    my_room = the_room;
    my_have_won = false;
    my_client_socket = the_client_socket;
    try
    {
      my_out = new PrintWriter(my_client_socket.getOutputStream(), true);
      my_in = new BufferedReader(new InputStreamReader(my_client_socket.getInputStream()));
      //readName();
    }
    catch (final IOException exception)
    {
      System.err.println("Could not establish buffers");
    }
    
  }
  /**
   * {@inheritDoc}
   */
  @Override
  public void run()
  {
    
    String input = "";
    String output = "";
    try
    {
      
      input = my_in.readLine();
      
      while (input != null)
      {
        //System.err.println("Received from " + my_name + ": " + input);
        if (my_have_won)
        {
          
          output = "YOU WON";
        }
        else if (my_board.isGameOver())
        {
          
          output = "GAME OVER";
          my_room.iLose(my_game_number);
        }
        else
        {
          output = parseCommand(input);
        }
        
        if (output != null)
        {
          my_out.println(output);
          //System.err.println("Sending " + my_name + ":" + output);
        }

        
        input = my_in.readLine();
      }
      
      my_client_socket.close();
    }
    catch (final IOException exception)
    {
      System.err.println("Client abruptly disconnected, reason:" + exception.toString());
      my_room.iLose(my_game_number);
    }
  }
  
  /**
   * Sets the opponent name.
   * @param the_opponent The opponent's name.
   */
  public void setOpponent(final String the_opponent)
  {
    my_opponent = the_opponent;
  }
  /**
   * Gets the name of the player.
   * @return The name of the player.
   */
  public String getName()
  {
    return my_name;
  }
  /**
   * Get this player's board as a string.
   * @return This player's board.
   */
  public String getBoardString()
  {
    return my_board.toString();
  }
  /**
   * 
   */
  public void youWin()
  {
    //System.err.println("This player won: " + my_name);
    my_have_won = true;
  }  
  /**
   * Update the game board, to be called by a timer.
   */
  public void update()
  {
    my_board.update();
  }
  
  /**
   * Parses a command.
   * @param the_command The command to parse.
   * @return The result of the command.
   */
  public String parseCommand(final String the_command)
  {
    String return_string = null;

    if (the_command.startsWith("GET"))
    {
      return_string = parseGetCommand(the_command);
    }
    else
    {
      parseControlCommand(the_command);
    }

    return return_string;
  }
  
  /**
   * Processes commands that don't return anything.
   * @param the_command Command to parse.
   */
  private void parseControlCommand(final String the_command)
  {
    if (my_room.isPaused())
    {
      if ("UNPAUSE".equals(the_command.toUpperCase(Locale.US)))
      {
        my_room.requestUnPause();
      }
    }
    else
    {
      if ("MOVE DOWN".equals(the_command.toUpperCase(Locale.US)))
      {
        my_board.moveDown();
      }
      else if ("MOVE RIGHT".equals(the_command.toUpperCase(Locale.US)))
      {
        my_board.moveRight();
      }
      else if ("MOVE LEFT".equals(the_command.toUpperCase(Locale.US)))
      {
        my_board.moveLeft();
      }
      else if ("ROTATE".equals(the_command.toUpperCase(Locale.US)))
      {
        my_board.rotateRight();
      }
      else if ("DROP".equals(the_command.toUpperCase(Locale.US)))
      {
        my_board.drop();
      }
      else if ("QUIT".equals(the_command.toUpperCase(Locale.US)))
      {
        my_room.iLose(my_game_number);
      }
      else if ("PAUSE".equals(the_command.toUpperCase(Locale.US)))
      {
        my_room.requestPause();
      }
    }
  }
  
  /**
   * Parses commands that require the server respond to the client.
   * @param the_command Command to parse.
   * @return Information to pass to the client.
   */
  private String parseGetCommand(final String the_command)
  {
    String return_string = null;
    if ("GET BOARD".equals(the_command.toUpperCase(Locale.US)))
    {
      return_string = my_board.toString();
    }
    else if ("GET SCORE".equals(the_command.toUpperCase(Locale.US)))
    {
      return_string = Integer.toString(my_board.getScore());
    }
    else if ("GET NEXT PIECE".equals(the_command.toUpperCase(Locale.US)))
    {
      return_string = my_board.getNextPiece().toString();
    }
    else if ("GET OPPONENT".equals(the_command.toUpperCase(Locale.US)))
    {
      return_string = my_opponent;
    }
    else if ("GET OPPONENT BOARD".equals(the_command.toUpperCase(Locale.US)))
    {
      return_string = my_room.getOpponentBoard(my_game_number);
    }
    return return_string;
  }
  /**
   * {@inheritDoc}
   */
  @Override
  public void update(final Observable the_observalbe, final Object the_update_int)
  {
    
    if (the_update_int instanceof Integer)
    {
      final Integer lines_cleared = (Integer) the_update_int;
      my_room.linesCleared(lines_cleared.intValue(), my_game_number);
    }
  }
  /**
   * Causes lines to pushed up from the bottom of this player's board.
   * 
   * @param the_num_lines Number of lines to queue against this player.
   */
  public void addLines(final int the_num_lines)
  {
    my_board.queueLines(the_num_lines);
    
  }
  
}
