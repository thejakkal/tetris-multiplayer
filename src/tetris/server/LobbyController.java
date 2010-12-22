/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Dec 9, 2010
 */
package tetris.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

/**
 * @author jpgunter
 * @version Dec 9, 2010
 */
public class LobbyController implements Runnable
{
  
  /**
   * 
   */
  private static final String JOINED_ROOM_CONFIRM_STRING = "JOINED ROOM";
  /**
   * The starting index of the room id in a JOIN ROOM command.
   */
  private static final int JOIN_ROOM_ID_START_INDEX = 9;
  /**
   * Socket that the client is connected to.
   */
  private final Socket my_client_socket;
  /**
   * Lobby to interact with.
   */
  private final Lobby my_lobby;
  /**
   * Whether or not the client has joined a Room.
   */
  private boolean my_joined_room;
  /**
   * output buffer.
   */
  private PrintWriter my_out;
  /**
   * Input buffer.
   */
  private BufferedReader my_in;
  /**
   * The name of the player.
   */
  private String my_name;
  
  /**
   * Creates a lobby controller to listen and respond to commands from the client.
   * @param the_socket The socket the client is connected to.
   * @param the_lobby The lobby to interact with.
   */
  public LobbyController(final Socket the_socket, final Lobby the_lobby)
  {
    my_client_socket = the_socket;
    my_lobby = the_lobby;
    my_joined_room = false;
    try
    {
      my_out = new PrintWriter(my_client_socket.getOutputStream(), true);
      my_in = new BufferedReader(new InputStreamReader(my_client_socket.getInputStream()));
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
      readName();
      input = my_in.readLine();
      
      while (input != null)
      {
        //System.err.println("Received from " + my_name + ": " + input);
        
        output = parseCommand(input);
        
        if (output != null)
        {
          my_out.println(output);
          //System.err.println("Sending " + my_name + ":" + output);
        }
        
        if (my_joined_room)
        {
          break;
        }
        
        input = my_in.readLine();
      }
    }
    catch (final IOException exception)
    {
      System.err.println("Client abruptly disconnected, reason: " + exception.toString());
    }
  }
  /**
   * Reads the name from the input buffer.
   */
  private void readName()
  {
    
    String input = null;
    try
    {
      input = my_in.readLine();
      //System.out.println("Received: " + input);
    }
    catch (final IOException exception)
    {
      System.err.println("Couldn't read name");
    }
    if (input != null)
    {
      my_name = input;
    }
  }
  
  /**
   * Parses the command submitted by the client and generates the output to
   * send back to the client.
   * @param the_input Command to parse.
   * @return The output to send back to the client.
   */
  private String parseCommand(final String the_input)
  {
    String output_string = null;
    if ("GET ROOM LIST".equals(the_input.toUpperCase(Locale.US)))
    {
      output_string = my_lobby.getRoomList();
    }
    else if ("CREATE ROOM SINGLE".equals(the_input.toUpperCase(Locale.US)))
    {
      my_lobby.createRoom(RoomType.SINGLE, my_client_socket, my_name);
      my_joined_room = true;
      output_string = JOINED_ROOM_CONFIRM_STRING;
    }
    else if ("CREATE ROOM DOUBLE".equals(the_input.toUpperCase(Locale.US)))
    {
      my_lobby.createRoom(RoomType.DOUBLE, my_client_socket, my_name);
      my_joined_room = true;
      output_string = JOINED_ROOM_CONFIRM_STRING;
    }
    else if (the_input.toUpperCase(Locale.US).startsWith("JOIN ROOM"))
    {
      final String id_string = the_input.substring(JOIN_ROOM_ID_START_INDEX).trim();
      if (my_lobby.joinRoom(Integer.parseInt(id_string), my_client_socket, my_name))
      {
        output_string = JOINED_ROOM_CONFIRM_STRING;
        my_joined_room = true;
      }
      else
      {
        output_string = "JOIN FAILED";
      }
    }
    return output_string;
  }

}
