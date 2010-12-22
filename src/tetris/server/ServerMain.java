/*
 * James Gunter 
 * Project TCSS 305 Autumn 2010 Assignment Nov 30, 2010
 */
package tetris.server;


/**
 * @author jpgunter
 * @version Nov 30, 2010
 */
public final class ServerMain
{
  /**
   * private constructor that no one can call.
   */
  private ServerMain()
  {
    //do nothing.
  }
  /**
   * Main method.
   * @param the_args command line args.
   */
  public static void main(final String[] the_args)
  {
    final ServerFrame sf = new ServerFrame();
    sf.start();
  }
}
