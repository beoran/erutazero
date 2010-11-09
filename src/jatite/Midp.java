/*
 * Midp display tools for jatite.Is test results
 */

package jatite;
import javax.microedition.lcdui.*;

/**
 *
 * @author bjorn
 */
public class Midp {
  public static final int COMMAND = 3189;

  public static class ErrorDisplay  {
    Alert _alert;
    ErrorDisplay(CommandListener listener, Display display, Is.Report report) {
      Command exit = new Command("Exit", Command.EXIT, COMMAND);
      _alert = new Alert("Test Results:");
      _alert.setString(report.toString());
      _alert.addCommand(exit);
      _alert.setCommandListener(listener);
      display.setCurrent(_alert);
    }
  }

  public static ErrorDisplay show(CommandListener listener, Display display,Is.Report report) {
    return new ErrorDisplay(listener, display, report);
  }

}
