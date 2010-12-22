/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Nov 30, 2010
 */
package tetris.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import tetris.pieces.GenericPiece;
import tetris.pieces.Piece;

/**
 * @author jpgunter
 * @version Nov 30, 2010
 */
public class Room implements Runnable
{
  
  /**
   * Delay between each update.
   */
  private static final int INITIAL_UPDATE_DELAY = 1000;
  /**
   * Delay step, change between each success delay.
   */
  private static final int DELAY_STEP = 100;
  /**
   * The fastest the update timer can go.
   */
  private static final int DELAY_MIN = 200;
  /**
   * Delay between each speed increase. (20 secs)
   */
  private static final int UPDATE_SPEED_INCREASE_DELAY = 20000;
  /**
   * A unique identifier for this room.
   */
  private final int my_id;
  /**
   * Game controllers that are in this room.
   */
  private final GameController[] my_games;
  /**
   * Timer to update each of the game controllers.
   */
  private Timer my_update_timer;
  /**
   * Timer to increase speed.
   */
  private Timer my_speed_increase_timer;
  /**
   * Height of the boards to create.
   */
  private final int my_board_height;
  /**
   * Width of the boards to create.
   * For some reason, making this field final breaks things.
   */
  private final int my_board_width;
  /**
   * List of pieces all the players will use.
   */
  private final List<Piece> my_pieces;
  /**
   * Counter to indicate number of players connected.
   */
  private int my_num_players;
  /**
   * Type of room this is.
   */
  private final RoomType my_room_type;
  /**
   * Whether the current game is paused or not.
   */
  private boolean my_game_is_paused;
  /**
   * My room's name.
   */
  private String my_name;
  /**
   * Creates a room of the specified size.
   * @param the_room_type The type of room you want to make.
   * @param the_board_height The height of the board to create.
   * @param the_board_width The width of the board to create.
   * @param the_num_pieces The number of pieces to use for this game.
   * @param the_id The id of this Room.
   */
  public Room(final RoomType the_room_type, final int the_board_height, 
              final int the_board_width, final int the_num_pieces, final int the_id)
  {
    my_id = the_id;
    my_name = "Room #" + my_id;
    my_room_type = the_room_type;
    if (my_room_type == RoomType.SINGLE)
    {
      my_games = new GameController[1];
    }
    else
    {
      my_games = new GameController[2];
    }
    my_board_height = the_board_height;
    my_board_width = the_board_width;
    my_pieces = new ArrayList<Piece>();
    for (int i = 0; i < the_num_pieces; i++)
    {
      my_pieces.add(getRandomPiece());
    }
    my_num_players = 0;
    my_game_is_paused = false;
  }
  
  /**
   * Starts the games.
   */
  @Override
  public void run()
  {
    if (my_room_type == RoomType.SINGLE)
    {
      my_games[0].setOpponent("NO ONE");
    }
    else
    {
      my_games[0].setOpponent(my_games[1].getName());
      my_games[1].setOpponent(my_games[0].getName());
    }
    
    for (GameController g : my_games)
    {
      (new Thread(g)).start();
    }
    
    my_update_timer = new Timer(INITIAL_UPDATE_DELAY, new UpdateTimerListener());
    my_speed_increase_timer = new Timer(UPDATE_SPEED_INCREASE_DELAY, 
                                        new SpeedIncreaseListener());
    my_update_timer.start();
    my_speed_increase_timer.start();
  }
  /**
   * Stops the games.
   */
  public void stopGame()
  {
    my_update_timer.stop();
    my_speed_increase_timer.stop();
  }
  
  /**
   * Gets this room's id.
   * @return This room's id.
   */
  public int getID()
  {
    return my_id;
  }
  
  /**
   * Whether or not his Room's timer is running.
   * @return Whether or not his Room's timer is running.
   */
  public boolean isRunning()
  {
    return my_update_timer.isRunning();
  }
  
  /**
   * Adds the client to the room.
   * @param the_client The socket connection the client is communicating on.
   * @param the_name The name of the player on the socket.
   */
  public void add(final Socket the_client, final String the_name)
  {
    my_name = the_name + "'s Game";
    final GameController g = 
      new GameController(this, my_num_players, my_board_height, my_board_width, 
                         my_pieces, the_client, the_name);
    my_games[my_num_players] = g;
    my_num_players++;
  }
  
  /**
   * Checks whether the room is full.
   * @return Whether the room is full.
   */
  public boolean isFull()
  {
    boolean result = true;
    for (int i = 0; i < my_games.length; i++)
    {
      if (my_games[i] == null)
      {
        result = false;
      }
    }
    return result;
  }
  
  /**
   * Gets the type of this room.
   * @return The type of this room.
   */
  public RoomType getType()
  {
    return my_room_type;
  }
  
  /**
   * Gets a random piece that is at position (width/2 - 2, 0).
   * @return A random piece.
   */
  private Piece getRandomPiece()
  {
    final Random rand = new Random();
    final char[] piece_chars = new char[] {'I', 'J', 'L', 'O', 'S', 'T', 'Z'};
    final int rand_int = Math.abs(rand.nextInt() % piece_chars.length);
    final char selected_char = piece_chars[rand_int];
    Piece rand_piece = 
      GenericPiece.charToPiece(selected_char, my_board_width / 2 - 2, 0);
    for (int i = 0; i < Piece.MAX_HEIGHT - rand_piece.getHeight(); i++)
    {
      rand_piece = rand_piece.moveDown();
    }
    return rand_piece;
  }
  
  /**
   * Gets the opponent's board.
   * @param the_game_number The game requesting.
   * @return The opponent's board.
   */
  public String getOpponentBoard(final int the_game_number)
  {
    //basically a blank board string.
    String opp_board = "0\n0";
    if (my_room_type == RoomType.DOUBLE)
    {
      opp_board = my_games[1 - the_game_number].getBoardString();
    }
    return opp_board;
  }
  /**
   * Tells the room you have lost, tells the others they have won.
   * @param the_game_number The game number of the player that lost.
   */
  public void iLose(final int the_game_number)
  {
    if (my_room_type == RoomType.DOUBLE)
    {
      my_games[1 - the_game_number].youWin();
    }
    stopGame();
  }
  /**
   * An action listener for the Timer that will call update() on the game controllers.
   * 
   * @author jpgunter
   * @version Dec 1, 2010
   */
  private class UpdateTimerListener implements ActionListener
  {
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent the_event)
    {
      for (GameController g : my_games)
      {
        g.update();
      }
    }
  }
  /**
   * An action listener for the Timer that will call increase the speed of the update timer.
   * 
   * @author jpgunter
   * @version Dec 1, 2010
   */
  private class SpeedIncreaseListener implements ActionListener
  {
    /**
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent the_event)
    {
      final int cur_delay = my_update_timer.getDelay();
      if (cur_delay > DELAY_MIN)
      {
        my_update_timer.setDelay(cur_delay - DELAY_STEP);
      }
      else
      {
        my_speed_increase_timer.stop();
      }
    }
  }
  /**
   * Tells the room when a player in a multi-player game clears lines to that lines can
   * be pushed up on the other players board.
   * @param the_num_lines The number of lines that were cleared.
   * @param the_game The game who cleared some lines.
   */
  public void linesCleared(final int the_num_lines, final int the_game)
  {
    if (my_room_type != RoomType.SINGLE)
    {
      my_games[1 - the_game].addLines(the_num_lines);
    }
  }

  /**
   * 
   */
  public void requestPause()
  {
    if (my_room_type == RoomType.SINGLE)
    {
      my_game_is_paused = true;
      my_update_timer.stop();
      my_speed_increase_timer.stop();
    }
  }
  
  /**
   * Requests an unPause from the Room.
   */
  public void requestUnPause()
  {
    if (my_game_is_paused)
    {
      my_game_is_paused = false;
      my_update_timer.restart();
      my_speed_increase_timer.restart();
    }
  }

  /**
   * Whether or not this Room is in a paused state.
   * @return Whether or not this Room is in a paused state.
   */
  public boolean isPaused()
  {
    return my_game_is_paused;
  }

  /**
   * Gets the name of this Room.
   * @return The name of this Room.
   */
  public String getName()
  {
    return my_name;
  }
  
  
}
