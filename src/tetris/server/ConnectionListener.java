/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Dec 9, 2010
 */
package tetris.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author jpgunter
 * @version Dec 9, 2010
 */
public class ConnectionListener implements Runnable
{
  /**
   * Lobby object to connect clients with.
   */
  private final Lobby my_lobby;
  /**
   * Whether or not to keep listening for connections. Set to false, then make a connection
   * to stop this thread.
   */
  private boolean my_keep_listening;
  /**
   * Socket to accept connections on.
   */
  private final ServerSocket my_server_socket;
  
  /**
   * Creates a ConnectionListener to listen for incoming socket connections, then passes them
   * off to a {@link LobbyController}.
   * @param the_port_numer The port number to listen on.
   * @param the_lobby The lobby to connect clients to.
   * @throws IOException If it cannot establish the socket.
   */
  public ConnectionListener(final int the_port_numer, final Lobby the_lobby) 
    throws IOException
  {
    my_lobby = the_lobby;
    my_keep_listening = true;
    my_server_socket = new ServerSocket(the_port_numer);
  }
  /**
   * {@inheritDoc}
   */
  @Override
  public void run()
  {
    try
    {
      
      while (my_keep_listening)
      {
        final Socket client_socket = my_server_socket.accept();
        System.out.println("Client from: " + client_socket.getInetAddress());
        final LobbyController lc = new LobbyController(client_socket, my_lobby);
        (new Thread(lc)).start();
      }
      my_server_socket.close();
    }
    catch (final IOException exception) 
    {
      System.err.println("Could not close socket connection, reason" + exception.toString());
    }
  }
  
  /**
   * Sets a flag to stop listening for incoming connections, make a socket connect to this
   * listener to finish the process.
   */
  public void stopListening()
  {
    my_keep_listening = false;
  }
}
