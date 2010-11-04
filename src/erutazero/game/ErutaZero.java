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
import jatite.T;

/**
 * @author bjorn
 */
public class ErutaZero extends MIDlet implements CommandListener {
    Rakuda.Lexer _lexer;

    private Command exitCommand;
    private TextBox tbox;

    public ErutaZero() {
        exitCommand = new Command("Exit", Command.EXIT, 1);
        tbox = new TextBox("Hello world MIDlet", "Hello World!", 25, 0);
        tbox.addCommand(exitCommand);
        tbox.setCommandListener(this);
    }

    protected void startApp() {
        _lexer = new Rakuda.Lexer("hello 123");
        Rakuda.Token token = _lexer.lex();
        String tokval = token.toString();
        T.test("lex text: " + tokval, tokval.endsWith("hello"));
        Screen screen = new Screen();
        tbox.setTitle(T.report().toString());
        Display.getDisplay(this).setCurrent(tbox);
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean bool) {}

    public void commandAction(Command cmd, Displayable disp) {
        if (cmd == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        }
    }

}
