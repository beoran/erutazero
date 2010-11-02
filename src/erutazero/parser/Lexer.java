/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erutazero.parser;

/**
 *
 * @author bjorn
 */
public class Lexer {
    public final static int NUMBER        = 1;
    public final static int STRING        = 2;
    public final static int SYMBOL        = 3;
    public final static int OPERATOR      = 4;
    public final static int OPEN_BRACKET  = 5;
    public final static int CLOSE_BRACKET = 6;
    public final static int OPEN_BRACE    = 7;
    public final static int CLOSE_BRACE   = 8;
    public final static int OPEN_SQUARE   = 9;
    public final static int CLOSE_SQUARE  = 10;
    public final static int END           = 11;
    public final static int ERROR         = 12;

    public static class Token {

        int     _type;
        String  _string;

        public Token(int type, String string) {
            _type   = type;
            _string = string;
        }

        public String toString() {
          return _string;
        }

        public int toInt() {
          return Integer.parseInt(_string);
        }
    }

    int _line, _column, _index, _offset;
    StringBuffer _buf;

    public Lexer(String to_lex) {
        _line = 0; _column = 0; _index = 0; _offset = 0;
        _buf = new StringBuffer(to_lex);
    }

    public char peek() {
        char res = _buf.charAt(_index + _offset);
        _offset++;
        return res;
    }

    public String part() {
        String res =  _buf.toString().substring(_index, _index + _offset);
        return res;
    }

    public void next() {
        _index += _offset;
        _offset = 0;
    }



    public Lexer.Token lex_number() {
        int offset = 0;
        while (Character.isDigit(this.peek())) {
        }
        return new Token(Lexer.NUMBER, this.part());

        // return new Token(Lexer.ERROR, "Could not parse number");
    }

    public Lexer.Token lex() {
        char ch = _buf.charAt(_index);
        if (Character.isDigit(ch)) {
            return lex_number();
        }
        return new Token(Lexer.ERROR, "Not implemented");
    }

}
