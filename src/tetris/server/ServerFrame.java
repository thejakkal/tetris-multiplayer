/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Dec 9, 2010
 */
package tetris.server;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * @author jpgunter
 * @version Dec 9, 2010
 */
public class ServerFrame extends JFrame
{
  /**
   * 
   */
  private static final long serialVersionUID = 901210300342319898L;

  /**
   * Number of columns to provide for entering the port number.
   */
  private static final int PORT_TEXT_COLS = 6;

  /**
   * Height of this frame.
   */
  private static final int FRAME_HEIGHT = 200;

  /**
   * Width of this frame.
   */
  private static final int FRAME_WIDTH = 200;
  /**
   * Default port to pre-populate the port text field with.
   */
  private static final int DEFAULT_PORT = 9998;
  /**
   * The text field to where the user will enter the desired port number.
   */
  private JTextField my_port_field;
  /**
   * The {@link ConnectionListener} that will just sit around accepting sockets all day.
   */
  private ConnectionListener my_connection_listener;
  /**
   * The button to stop the server.
   */
  private JButton my_stop_button;
  /**
   * The button to start the server.
   */
  private JButton my_start_button;
  /**
   * The lobby that clients will interact with.
   */
  private final Lobby my_lobby;
  /**
   * the port number this server is connected on.
   */
  private int my_port_num;

  /**
   * Creates the server frame.
   */
  public ServerFrame()
  {
    super("GTC Server");
    my_lobby = new Lobby();
    
    setLayout(new FlowLayout());
    setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
    addContent();
    
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
   * Adds all the stuff to the frame.
   */
  private void addContent()
  {
    add(new JLabel("Port: "));
    my_port_field = new JTextField(Integer.toString(DEFAULT_PORT), PORT_TEXT_COLS);
    add(my_port_field);
    my_start_button = new JButton("Start Server");
    my_start_button.addActionListener(new startListener());
    add(my_start_button);
    my_stop_button = new JButton("Stop Server");
    my_stop_button.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent the_event)
      {
        stop();
      }
    });
    my_stop_button.setEnabled(false);
    add(my_stop_button);
  }

  /**
   * Trys to stop the server by telling the connection listener to stop listening, then making
   * a last connection to the listener which advances the loop and stops the thread.
   */
  private void stop()
  {
    my_connection_listener.stopListening();
    try
    {
      //opens the last connection to the server 
      //so that it will then stop listening and terminate
      (new Socket("localhost", my_port_num)).close();
    }
    catch (final UnknownHostException exception)
    {
      System.err.println("Couldn't find localhost, something is seriously wrong...");
    }
    catch (final IOException exception)
    {
      System.err.println("Couldn't connect to my own server socket, maybe you closed me " +
                         "after stopping the server, which was a nice thing to do. Thanks!.");
    }
    my_start_button.setEnabled(true);
    my_stop_button.setEnabled(false);
  }
  
  /**
   * Starts the frame (not the connection listener, you have to click a button for that).
   */
  public void start()
  {
    pack();
    setVisible(true);
  }
  
  /**
   * {@link ActionListener} to start the server on the port provided in the text field.
   * @author jpgunter
   * @version Dec 10, 2010
   */
  private class startListener implements ActionListener
  {
    @Override
    public void actionPerformed(final ActionEvent the_event)
    {
      try
      {
        my_port_num = Integer.parseInt(my_port_field.getText());
        my_connection_listener = 
          new ConnectionListener(my_port_num, my_lobby);
        (new Thread(my_connection_listener)).start();
        my_stop_button.setEnabled(true);
        my_start_button.setEnabled(false);
      }
      catch (final NumberFormatException num_exception)
      {
        JOptionPane.showMessageDialog(ServerFrame.this, "Integer Error", 
                                      "Please enter a valid port number", 
                                      JOptionPane.ERROR_MESSAGE);
      }
      catch (final IOException io_exception)
      {
        JOptionPane.showMessageDialog(ServerFrame.this, "Socket Error",
                                      "Could not start server, maybe alread started?",
                                      JOptionPane.ERROR_MESSAGE);
      }
      
    }
  }
  
}
