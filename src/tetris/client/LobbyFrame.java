/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Dec 9, 2010
 */
package tetris.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

/**
 * @author jpgunter
 * @version Dec 9, 2010
 */
public class LobbyFrame extends JFrame
{
  /**
   * Serial ID.
   */
  private static final long serialVersionUID = -2381757587223238762L;
  /**
   * Height of the window.
   */
  private static final int WINDOW_HEIGHT = 400;
  /**
   * width of the buttons panel.
   */
  private static final int BUTTONS_PANEL_WIDTH = 120;
  /**
   * Port number to connect to the server with. 
   */
  private static final int PORT_NUMBER = 9998;
  /**
   * Tab character string.
   */
  private static final String TAB_STRING = "\\t";
  /**
   * New line character string. 
   */
  private static final String NEWLINE_STRING = "\\n";
  /**
   * Max lenght of the player name.
   */
  private static final int MAX_NAME_LENGTH = 10;
  /**
   * Maximum length for a url.
   */
  private static final int MAX_URL_LENGTH = 100;
  /**
   * JList to maintain a list of available rooms on ther server.
   */
  private final JList my_room_list;
  /***
   * Socket to communicate with the server.
   */
  private Socket my_server_socket;
  /**
   * Tetris protocal to communicate with the server.
   */
  private TetrisProtocol my_tetris_protocol;
  /**
   * Address of the server to connect to.
   */
  private String my_server_addr;
  /**
   * Name of the player.
   */
  private String my_name;
  
  /**
   * Creates a LobbyFrame to allow the client to create/join a game or Room.
   */
  public LobbyFrame()
  {
    super("GTC - Lobby");
    
    initializeSocket();
    setPreferredSize(new Dimension(WINDOW_HEIGHT, WINDOW_HEIGHT));
    my_room_list = new JList();
    my_room_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    add(my_room_list);
    
    
    add(makeButtonsPanel(), BorderLayout.EAST);
    
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(final WindowEvent the_event)
      {
        stop();
        dispose();
      }
    });
  }
  
  /**
   * Prepares this lobby frame for closing, but does not close it.
   */
  private void stop()
  {
    if (my_server_socket != null)
    {
      try
      {
        my_server_socket.close();
      }
      catch (final IOException exception)
      {
        System.err.println("Could not close socket, that was wierd...");
      }
    }
  }

  /**
   * Starts up the ClientFrame with the specified room type.
   * @param the_type The type of game to start.
   */
  private void startClient(final RoomType the_type)
  {
    final ClientFrame cf = new ClientFrame(this, my_tetris_protocol, my_name, the_type);
    cf.start();
    setVisible(false);
  }
  /**
   * Reconnects to the server and will show this frame again, if is was hidden.
   */
  public void reconnect()
  {
    setVisible(true);
    try
    {
      my_server_socket.close();
      my_server_socket = new Socket(my_server_addr, PORT_NUMBER);
      my_tetris_protocol = new TetrisProtocol(my_server_socket);
    }
    catch (final IOException exception)
    {
      JOptionPane.showMessageDialog(this, "Could not reconnection to server", 
                                    "Could not reconnect to server :(, no more tetris for now",
                                    JOptionPane.ERROR_MESSAGE);
      System.exit(-1);
    }
    
    my_tetris_protocol.setName(my_name);
    refreshRooms();
  }
  
  /**
   * Starts up this client frame by prompting the player for a name and then showing the frame.
   */
  public void start()
  {
    my_name = promptForName();
    my_tetris_protocol.setName(my_name);
    refreshRooms();
    pack();
    setVisible(true);
  }
  
  /**
   * Creates the Buttons JPanel and adds all the buttons to it.
   * @return A JPanel with all the needed buttons on it.
   */
  private JPanel makeButtonsPanel()
  {
    final JPanel buttons_panel = new JPanel(new FlowLayout());
    buttons_panel.setPreferredSize(new Dimension(BUTTONS_PANEL_WIDTH, WINDOW_HEIGHT));
    final JButton refresh = new JButton("Refresh");
    refresh.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent the_event)
      {
        refreshRooms();
      }
    });
    buttons_panel.add(refresh);
    final JButton join = new JButton("Join");
    join.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent the_event)
      {
        final String room = (String) my_room_list.getSelectedValue();
        if (room != null)
        {
          final int room_id = Integer.parseInt(room.substring(0, room.indexOf(' ')));
          if (my_tetris_protocol.joinGame(room_id))
          {
            startClient(RoomType.DOUBLE);
          }
          else
          {
            JOptionPane.showMessageDialog(LobbyFrame.this, 
                                          "Failed to Join Game", 
                                          "Sorry could not join that game, perhaps the game " +
                                          "filled before you could join :(", 
                                          JOptionPane.ERROR_MESSAGE);
          } 
        }
      }
    });
    buttons_panel.add(join);
    
    final JButton create1p = new JButton("Create 1P");
    create1p.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent the_event)
      {
        if (my_tetris_protocol.createGame(RoomType.SINGLE))
        {
          startClient(RoomType.SINGLE);
        }
        else
        {
          JOptionPane.showMessageDialog(LobbyFrame.this, 
                                        "Failed to Create 1P Game", 
                                        "Sorry could not create 1P game, not sure why...", 
                                        JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    buttons_panel.add(create1p);
    final JButton create2p = new JButton("Create 2P");
    create2p.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent the_event)
      {
        if (my_tetris_protocol.createGame(RoomType.DOUBLE))
        {
          startClient(RoomType.DOUBLE);
        }
        else
        {
          JOptionPane.showMessageDialog(LobbyFrame.this, 
                                        "Failed to Create 2P Game", 
                                        "Sorry could not create 2P game, not sure why...", 
                                        JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    buttons_panel.add(create2p);
    return buttons_panel;
  }
  
  /**
   * Refreshes the room list.
   */
  private void refreshRooms()
  {
    final List<String> rooms = my_tetris_protocol.getRoomList();
    
    my_room_list.setListData(rooms.toArray());
  }
  /**
   * Connects to the server.
   */
  private void initializeSocket()
  {
    try
    {
      my_server_addr = promptForServer();
      my_server_socket = new Socket(my_server_addr, PORT_NUMBER);
      my_tetris_protocol = new TetrisProtocol(my_server_socket);
    }
    catch (final IOException exception)
    {
      JOptionPane.showMessageDialog(this, "Could not connect to server", 
                                    "Could not connect to server :(, no tetris for now",
                                    JOptionPane.ERROR_MESSAGE);
      System.exit(-1);
    }
  }
  
  /**
   * Prompts the player for their name.
   * @return The name of the player.
   */
  private String promptForName()
  {
    String player_name = JOptionPane.showInputDialog(this, "Enter Player Name (No more " +
                                                           "than 10 characters)");
    while (player_name.length() > MAX_NAME_LENGTH || player_name.trim().length() < 1)
    {
      player_name = JOptionPane.showInputDialog(this, "Try Again, this time under 10");
    }
    //strip anything that might confuse the server
    player_name = player_name.replaceAll(NEWLINE_STRING, "");
    player_name = player_name.replaceAll(TAB_STRING, "");
    return player_name.trim();
  }
  /**
   * Prompts the player for the server to connect to.
   * @return The url/ip address of the server to connect to.
   */
  private String promptForServer()
  {
    String server_address = JOptionPane.showInputDialog(this, "Enter Server Name (url or IP)");
    while (server_address.length() > MAX_URL_LENGTH || server_address.trim().length() < 1)
    {
      server_address = JOptionPane.showInputDialog(this, "Try Again, this time under 100");
    }
    //strip anything that might confuse the server
    server_address = server_address.replaceAll(NEWLINE_STRING, "");
    server_address = server_address.replaceAll(TAB_STRING, "");
    return server_address.trim();
  }
  
}
