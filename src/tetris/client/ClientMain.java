/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Dec 1, 2010
 */
package tetris.client;

/**
 * @author jpgunter
 * @version Dec 1, 2010
 */
public final class ClientMain
{
  /**
   * private contstructor to make singleton.
   */
  private ClientMain()
  {
    //do nothing.
  }
  /**
   * Main method.
   * @param the_args Command line args.
   */
  public static void main(final String[] the_args)
  {
    final LobbyFrame lf = new LobbyFrame();
    lf.start();
  }

}
