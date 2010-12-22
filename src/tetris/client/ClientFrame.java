/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Dec 1, 2010
 */
package tetris.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.Timer;

import tetris.pieces.Piece;

/**
 * @author jpgunter
 * @version Dec 1, 2010
 */
public class ClientFrame extends JFrame
{

  /**
   * The cell size of the large view.
   */
  private static final int LARGE_CELL_SIZE = 30;
  /**
   * The cell size of the normal view.
   */
  private static final int NORMAL_CELL_SIZE = 20;
  /**
   * The cell size of the small view.
   */
  private static final int SMALL_CELL_SIZE = 10;
  /**
   * Default board width.
   */
  private static final int DEFAULT_BOARD_WIDTH = 10;
  /**
   * Default board height.
   */
  private static final int DEFAULT_BOARD_HEIGHT = 20;
  /**
   * Delay in ms to update the dashboard content.
   */
  private static final int GET_DASH_DELAY = 1000;
  /**
   * Delay in ms to update the game board.
   */
  private static final int GET_BOARD_DELAY = 100;
  /**
   * Serialization ID.
   */
  private static final long serialVersionUID = 1940711754466394826L;
  /**
   * Small font to be used.
   */
  private static final Font SMALL_FONT = new Font(Font.MONOSPACED, Font.BOLD, 8);
  /**
   * Default font to be used.
   */
  private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 12);
  /**
   * Big font to be used.
   */
  private static final Font BIG_FONT = new Font(Font.MONOSPACED, Font.BOLD, 18);
  /**
   * Default size of each cell.
   */
  private static final int DEFAULT_CELL_SIZE = 20;
  /**
   * Default horizontal gap for flowlayouts.
   */
  private static final int DEFAULT_HGAP = 5;

  /**
   * Timer to time when to refresh the board.
   */
  private final Timer my_get_board_timer;
  /**
   * Timer to get when to update the score.
   */
  private final Timer my_get_score_timer;
  /**
   * A BoardPanel that will hold the current board in String form.
   */
  private BoardPanel my_board_drawing;
  /**
   * A BoardPanel to hold the opponent's board.
   */
  private BoardPanel my_opponent_board;
  /**
   * Panel to show the next Piece.
   */
  private BoardPanel my_next_piece;
  /**
   * Panel that shows my board.
   */
  private JPanel my_board_panel;
  /**
   * Panel that shows details info.
   */
  private JPanel my_details_panel;
  /**
   * Panel that shows the opponent info.
   */
  private JPanel my_op_board_panel;
  /**
   * Shows details about the opponent.
   */
  private JPanel my_op_details_panel;
  /**
   * Label that will show this player's name.
   */
  private JLabel my_name_label;
  /**
   * A label that will the score.
   */
  private JLabel my_score_label;
  /**
   * The label that will hold the oppponent's name.
   */
  private JLabel my_opponent_label;
  /**
   * Protocol object to communicate with the server.
   */
  private final TetrisProtocol my_tetris_protocol;

  /**
   * size of each cell in pixels.
   */
  private int my_cell_size;
  /**
   * Label that will show the instructions to play the game.
   */
  private JLabel my_instructions_label;
  /**
   * The name of the player.
   */
  private final String my_player_name;
  /**
   * reference to the LobbyFrame that created this.
   */
  private final LobbyFrame my_parent_frame;
  /**
   * The type of game this is.
   */
  private final RoomType my_room_type;
  
  /**
   * Creates the GUI's JFrame. 
   * @param the_parent Parent LobbyFrame that created this frame (so it can return after the
   * game ends)
   * @param the_tetris_protocol A reference to the established {@link TetrisProtocol}.
   * @param the_name The name of the player.
   * @param the_type The type of game this is (SINGLE or DOUBLE)
   */
  public ClientFrame(final LobbyFrame the_parent, final TetrisProtocol the_tetris_protocol, 
                     final String the_name, final RoomType the_type)
  {
    super("GTC - GTC Tetris Client"); //gotta love recursive acronyms
    my_parent_frame = the_parent;
    my_player_name = the_name;
    my_room_type = the_type;
    my_tetris_protocol = the_tetris_protocol;
    setLayout(new FlowLayout(FlowLayout.LEADING, DEFAULT_HGAP, 0));
    setResizable(false);
    my_cell_size = DEFAULT_CELL_SIZE;
    makePanels();
    my_get_board_timer = new Timer(GET_BOARD_DELAY, new RefreshBoardsListener());
    my_get_score_timer = new Timer(GET_DASH_DELAY, new RefreshDetailsListener());

  }
  
  /**
   * Creates all of the panels.
   */
  private void makePanels()
  {
    my_board_panel = new JPanel(new FlowLayout(FlowLayout.LEADING, DEFAULT_HGAP, 0));
    my_details_panel = new JPanel(new FlowLayout(FlowLayout.LEADING, DEFAULT_HGAP, 0));
    if (my_room_type == RoomType.DOUBLE)
    {
      my_op_board_panel = new JPanel(new FlowLayout(FlowLayout.LEADING, DEFAULT_HGAP, 0));
      my_op_details_panel = new JPanel(new FlowLayout(FlowLayout.LEADING, DEFAULT_HGAP, 0));
    }
    setPanelSizes();
  }
  /**
   * Sets the panels sizes based on the current my_cell_size.
   */
  private void setPanelSizes()
  {
    my_board_panel.setPreferredSize(new Dimension(DEFAULT_BOARD_WIDTH * my_cell_size +
                                                  DEFAULT_HGAP * 2,
                                                  DEFAULT_BOARD_HEIGHT * my_cell_size));
    my_details_panel.setPreferredSize(new Dimension((Piece.MAX_WIDTH + 2) * my_cell_size +
                                                    DEFAULT_HGAP * 2, 
                                                    DEFAULT_BOARD_HEIGHT * my_cell_size));
    if (my_room_type == RoomType.DOUBLE)
    {
      my_op_board_panel.setPreferredSize(new Dimension(DEFAULT_BOARD_WIDTH * my_cell_size +
                                                       DEFAULT_HGAP * 2, 
                                                       DEFAULT_BOARD_HEIGHT * my_cell_size));
      my_op_details_panel.setPreferredSize(new Dimension((Piece.MAX_WIDTH + 2) * my_cell_size +
                                                         DEFAULT_HGAP * 2,
                                                         DEFAULT_BOARD_HEIGHT * my_cell_size));
    }
    
  }
  
  /**
   * Changes the size of the interface based on a new cell size.
   * @param the_new_cell_size New size each cell should be.
   */
  private void changeSize(final int the_new_cell_size)
  {
    my_cell_size = the_new_cell_size;
    setPanelSizes();
    my_board_drawing.setPreferredSize(new Dimension(DEFAULT_BOARD_WIDTH * my_cell_size, 
                                                    DEFAULT_BOARD_HEIGHT * my_cell_size));

    my_next_piece.setPreferredSize(new Dimension((Piece.MAX_WIDTH + 2) * my_cell_size, 
                                                 (Piece.MAX_HEIGHT + 2) * my_cell_size));
    my_board_drawing.repaint();
    my_next_piece.repaint();
    
    if (my_room_type == RoomType.DOUBLE)
    {
      my_opponent_board.setPreferredSize(new Dimension(DEFAULT_BOARD_WIDTH * my_cell_size, 
                                                    DEFAULT_BOARD_HEIGHT * my_cell_size));
      my_opponent_board.repaint();
    }
    pack();
    validate();
  }
  

  /**
   * Starts this client.
   */
  public void start()
  {

    setJMenuBar(createMenuBar());
    
    add(my_board_panel);
    add(my_details_panel);
    if (my_room_type == RoomType.DOUBLE)
    {
      add(my_op_board_panel);
      add(my_op_details_panel);
    }
    final char[][] blank_board = new char[DEFAULT_BOARD_HEIGHT][DEFAULT_BOARD_WIDTH];
    for (char[] row : blank_board)
    {
      Arrays.fill(row, '.');
    }
    
    my_board_drawing = new BoardPanel(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, blank_board);
    my_board_panel.add(my_board_drawing);
    my_board_drawing.repaint();
    
    if (my_room_type == RoomType.DOUBLE)
    {
      my_opponent_board = 
        new BoardPanel(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT, blank_board);
      my_op_board_panel.add(my_opponent_board);
      my_opponent_board.repaint();
    }
    
    
    
    
    
    my_name_label = new JLabel("<html><u>Local Player</u><br/>Waiting");
    my_name_label.setFont(DEFAULT_FONT);
    
    my_details_panel.add(my_name_label);
    
    my_score_label = new JLabel("<html><u>Score</u>:<br/>0</html>");
    my_score_label.setFont(DEFAULT_FONT);
    my_details_panel.add(my_score_label);
    
    final char[][] blank_piece = new char[Piece.MAX_HEIGHT + 2][Piece.MAX_WIDTH + 2];
    for (char[] row : blank_piece)
    {
      Arrays.fill(row, '.');
    }
    my_next_piece = new BoardPanel(Piece.MAX_WIDTH + 2, Piece.MAX_HEIGHT + 2, blank_piece);
    my_details_panel.add(my_next_piece);
    
    
    final StringBuilder inst_text = new StringBuilder("<html>\u2191 - Rotate<br />" +
                                                      "\u2190 - Left<br />\u2192 - Right" +
                                                      "<br />\u2193 - Down<br />Space - Drop");
    if (my_room_type == RoomType.SINGLE)
    {
      inst_text.append("<br />P - Pause<br /> U - Unpause");
    }
    my_instructions_label = new JLabel(inst_text.toString());
    my_instructions_label.setFont(DEFAULT_FONT);
    my_details_panel.add(my_instructions_label, BorderLayout.WEST);
    
    if (my_room_type == RoomType.DOUBLE)
    {
      my_opponent_label = new JLabel();
      my_opponent_label.setFont(DEFAULT_FONT);
      my_opponent_label.setText("<html><u>Opponent</u><br/> Waiting");
      my_op_details_panel.add(my_opponent_label);
    }
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(final WindowEvent the_event)
      {
        stop();
        my_parent_frame.reconnect();
        dispose();
      }
    });
    

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
    connect();
  }
  
  
  /**
   * Creates and returns the menu bar.
   * @return The menu bar.
   */
  private JMenuBar createMenuBar()
  {
    final JMenuBar menu = new JMenuBar();
    //menu.add(createFileMenu());
    menu.add(createViewMenu());
    return menu;
  }

  /**
   * Creates the view menu.
   * @return The view menu.
   */
  private JMenu createViewMenu()
  {
    final JMenu view = new JMenu("View");
    view.setMnemonic(KeyEvent.VK_V);
    final JMenu size = new JMenu("Size");
    size.setMnemonic(KeyEvent.VK_S);
    final ButtonGroup size_group = new ButtonGroup();
    JRadioButtonMenuItem size_button = new JRadioButtonMenuItem("Small");
    size_button.setMnemonic(KeyEvent.VK_M);
    size_button.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent the_event)
      {
        changeSize(SMALL_CELL_SIZE);
        my_name_label.setFont(SMALL_FONT);
        my_score_label.setFont(SMALL_FONT);
        my_instructions_label.setFont(SMALL_FONT);
        my_opponent_label.setFont(SMALL_FONT);
      }
    });
    size_group.add(size_button);
    size.add(size_button);
    
    size_button = new JRadioButtonMenuItem("Normal");
    size_button.setMnemonic(KeyEvent.VK_N);
    size_button.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent the_event)
      {
        changeSize(NORMAL_CELL_SIZE);
        my_name_label.setFont(DEFAULT_FONT);
        my_score_label.setFont(DEFAULT_FONT);
        my_instructions_label.setFont(DEFAULT_FONT);
        my_opponent_label.setFont(DEFAULT_FONT);
      }
    });
    size_group.add(size_button);
    size.add(size_button);
    
    size_button = new JRadioButtonMenuItem("Large");
    size_button.setMnemonic(KeyEvent.VK_L);
    size_button.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(final ActionEvent the_event)
      {
        changeSize(LARGE_CELL_SIZE);
        my_name_label.setFont(BIG_FONT);
        my_score_label.setFont(BIG_FONT);
        my_instructions_label.setFont(BIG_FONT);
        my_opponent_label.setFont(BIG_FONT);
      }
    });
    size_group.add(size_button);
    size.add(size_button);
    
    view.add(size);
    return view;
  }

  /**
   * Starts the timers and shows the GUI.
   */
  public void connect()
  {
    //switch the board panel back to showing a board mode in case is what put into show
    //message mode.
    my_board_drawing.showBoard();
    my_board_drawing.repaint();
    
    my_name_label.setText("<html><u>Local Player</u><br/>" + my_player_name);
    if (my_room_type == RoomType.DOUBLE)
    {
      my_opponent_label.setText("<html><u>Opponent</u><br/>" + 
                                my_tetris_protocol.getOpponent());
    }
    my_get_board_timer.start();
    my_get_score_timer.start();
    setFocusable(true);
    requestFocus();
    addKeyListener(new ClientKeyListener());
    
  }

  

  /**
   * Stops the timers and closes the socket connection.
   */
  private void stop()
  {
    if (my_tetris_protocol != null)
    {
      my_tetris_protocol.quit();
    }
    if (my_get_board_timer != null && my_get_board_timer.isRepeats())
    {
      my_get_board_timer.stop();
    }
    if (my_get_score_timer != null && my_get_score_timer.isRepeats())
    {
      my_get_score_timer.stop();
    }
    //allow the player to connect again
    //my_connect_item.setEnabled(true);

    
  }
  /**
   * A Keyboard listener to listen for key presses.
   * 
   * @author jpgunter
   * @version Dec 1, 2010
   */
  private class ClientKeyListener extends KeyAdapter
  {
    @Override
    public void keyPressed(final KeyEvent the_event)
    {
      switch (the_event.getKeyCode())
      {
        
        case KeyEvent.VK_DOWN:
          my_tetris_protocol.moveDown();
          break;
        case KeyEvent.VK_RIGHT:
          my_tetris_protocol.moveRight(); 
          break;
        case KeyEvent.VK_LEFT:
          my_tetris_protocol.moveLeft();
          break;
        case KeyEvent.VK_UP:
          my_tetris_protocol.rotate();
          break;
        case KeyEvent.VK_SPACE:
          my_tetris_protocol.drop();
          break;
          //the following two only actually work on single player, but no harm in leaving them
        case KeyEvent.VK_P:
          my_tetris_protocol.pause();
          break;
        case KeyEvent.VK_U:
          my_tetris_protocol.unpause();
          break;
        default:
          //do nothing if the command makes no sense.
          break;
      }
    }
  }
  /**
   * Action listener that will update the dash board content.
   * @author jpgunterwhich
   * @version Dec 1, 2010
   */
  private class RefreshDetailsListener implements ActionListener
  {

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(final ActionEvent the_event)
    {
      final char[][] temp_board = my_tetris_protocol.getNextPiece();
      if (temp_board != null)
      {
        my_next_piece.setBoard(temp_board);
        my_next_piece.repaint();
      }
      my_score_label.setText("<html><u>Score</u>:<br/>" + my_tetris_protocol.getScore());
    }
  }
  /**
   * Action listener that will update the game board.
   * @author jpgunter
   * @version Dec 1, 2010
   */
  private class RefreshBoardsListener implements ActionListener
  {
  /**
    * {@inheritDoc}
    */
    public void actionPerformed(final ActionEvent the_event)
    {      
      
      if (my_tetris_protocol.isGameOver())
      {
        if (my_tetris_protocol.playerWon())
        {
          my_board_drawing.showMessage("You win! :)");
          my_board_drawing.repaint();
          stop();
        }
        else
        {
          my_board_drawing.showMessage("You lose :(");
          my_board_drawing.repaint();
          stop();
        }
      }
      else 
      {
        final char[][] temp_board = my_tetris_protocol.getBoard();
        char[][] opp_board = null;
        if (my_room_type == RoomType.DOUBLE)
        {
          opp_board = my_tetris_protocol.getOpponentBoard();
        }
        if (temp_board != null)
        {
          my_board_drawing.setBoard(temp_board);
          my_board_drawing.repaint();
        }
        if (opp_board != null)
        {
          my_opponent_board.setBoard(opp_board);
          my_opponent_board.repaint();
        }
      }
      
    }
  }
}
