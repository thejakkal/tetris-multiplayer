/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Dec 9, 2010
 */
package tetris.server;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jpgunter
 * @version Dec 9, 2010
 */
public class Lobby
{
  /**
   * Height of the player's boards.
   */
  private static final int BOARD_HEIGHT = 20;
  /**
   * Width of the player's boards.
   */
  private static final int BOARD_WIDTH = 10;
  /**
   * Number of pieces to randomly generate.
   */
  private static final int NUM_PIECES = 200;
  /**
   * Map of available rooms.
   */
  private final List<Room> my_rooms;
  /**
   * id_counter to uniquely identify each room.
   */
  private int my_room_id_counter;
  
  /**
   * Creates an empty lobby.
   */
  public Lobby()
  {
    my_rooms = new LinkedList<Room>();

  }
  
  /**
   * Creates a room of the specified type, then adds the client to it.
   * @param the_type The type of room to create.
   * @param the_first_client The first client's socket connection.
   * @param the_name the name of the player on the socket.
   */
  public void createRoom(final RoomType the_type, final Socket the_first_client, 
                         final String the_name)
  {
    final Room room = new Room(the_type, BOARD_HEIGHT, BOARD_WIDTH, NUM_PIECES, 
                               my_room_id_counter);
    my_rooms.add(room);
    room.add(the_first_client, the_name);
    if (room.isFull())
    {
      (new Thread(room)).start();
    }
    my_room_id_counter++;
  }
  
  /**
   * Joins the specified client, with the specified name to the specified Room.
   * @param the_room_id The id of the Room to join.
   * @param the_client The client's socket connection.
   * @param the_name The name of the player on the socket.
   * @return Whether the join succeeded, can fail if the room is not empty.
   */
  public boolean joinRoom(final int the_room_id, final Socket the_client, 
                          final String the_name)
  {
    boolean result = false;
    for (Room r : my_rooms)
    {
      if (r.getID() == the_room_id && !r.isFull())
      {
        r.add(the_client, the_name);
        if (r.isFull())
        {
          (new Thread(r)).start();
        }
        result = true;
      }
    }
    return result;
  }
  
  /**
   * Gets a multi-line String that represents the available rooms. The int at the
   * beginning of each line represents the ID of the room.
   * @return The string representation of the available rooms, ready to be sent to a client.
   */
  public String getRoomList()
  {
    final StringBuilder room_list = new StringBuilder();
    int num_open_rooms = 0;
    for (Room r : my_rooms)
    {
      if (!r.isFull())
      {
        room_list.append(r.getID());
        room_list.append(" - ");
        room_list.append(r.getName());
        room_list.append('\n');
        num_open_rooms++;
      }
    }
    room_list.insert(0, num_open_rooms + "\n");
    room_list.deleteCharAt(room_list.length() - 1);
    return room_list.toString();
  }
}
