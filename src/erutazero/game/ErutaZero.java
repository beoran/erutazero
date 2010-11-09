/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * JavaMe Docs at: http://download.oracle.com/javame/
 */

package erutazero.game;

import rakuda.Rakuda;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import jatite.Is;

/**
 * @author bjorn
 */
public class ErutaZero extends MIDlet implements CommandListener {
    Rakuda.Lexer _lexer;

    private Command exitCommand;
    private TextBox tbox;
    private Alert alert;

    public ErutaZero() {
    }

   protected void startApp() {
      _lexer = new Rakuda.Lexer("hello 123");
      Rakuda.Token token = _lexer.lex();
      String tokval = token.toString();
      Is.equal("abc", "abc");
      Is.equal(tokval, "hello ");
      Display.getDisplay(this).setCurrent(alert);
      jatite.Midp.show(this, Display.getDisplay(this), Is.report());
      Screen screen = new Screen();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean bool) {}

    public void commandAction(Command cmd, Displayable disp) {
      if (cmd.getCommandType() == Command.EXIT) {
        destroyApp(false);
        notifyDestroyed();
      }
    }

}
