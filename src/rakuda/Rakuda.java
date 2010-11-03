/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Rakuda's grammar:
 *
 *   Verified with : http://smlweb.cpsc.ucalgary.ca/start.html
 *
PROGRAM -> EXPRESSION PROGRAM | .
EXPRESSION -> VALUE PARAMLIST NL | BLOCK .
PARAMLIST -> PARAMETER PARAMLIST | .
PARAMETER -> BLOCK | VALUE .
BLOCK -> do PROGRAM end | ob PROGRAM cb | os PROGRAM cs | oa PROGRAM ca.
NL -> nl | semicolon .
VALUE -> string | number | symbol | operator .
 * 
 * nl is ' or ';'
 *
 */
package rakuda;

/**
 *
 * @author bjmey
 */
public class Rakuda {
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

  public static class Lexer {
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
    public final static int SEPARATOR     = 11;
    public final static int END_STREAM    = 12;
    public final static int ERROR         = 13;

    int _line, _column, _index, _offset;
    StringBuffer _buf;

    public Lexer(String to_lex) {
        _line = 0; _column = 0; _index = 0; _offset = 0;
        _buf = new StringBuffer(to_lex);
    }

    static boolean isDigit(char c) {
       return Character.isDigit(c);
    }

    static boolean isLowerAlpha(char c) {
       return (c >= 'a' && c <= 'z');
    }

    static boolean isUpperAlpha(char c) {
       return (c >= 'A' && c <= 'Z');
    }

    static boolean isAlpha(char c) {
        return isLowerAlpha(c) || isUpperAlpha(c);
    }

    static boolean isAlnum(char c) {
        return isAlpha(c) || isDigit(c);
    }

    static boolean isBlank(char c) {
        return c == ' ' || c == '\t';
    }

    static boolean isEol(char c) {
        return c == '\n' || c == ';' || c == '\r';
    }

    static boolean isOp(char c) {
        return !(isBlank(c) || isAlnum(c) || isEol(c));
    }

    public char peep() {
        return _buf.charAt(_index + _offset);
    }

    public char peek() {
        char res = peep();
        _offset++;
        return res;
    }

    public String part() {
        String res =  _buf.toString().substring(_index, _index + _offset);
        return res;
    }

    public String part_noquotes() {
        String res =  _buf.toString().substring(_index + 1, _index + _offset - 1);
        return res;
    }

    public boolean eos() {
        return _index + _offset >= _buf.length();
    }

    public void next() {
        _index += _offset;
        _offset = 0;
    }

    public void skip(int count) {
        int index;
        for (index = 0; index < count; index ++) { skip(); }
    }

    public void skip() {
        _index += 1;
        _offset = 0;
    }


    Token lex_error(String text) {
        return new Token(Lexer.ERROR, text);
    }


    public Token lex_number() {
        while (isDigit(this.peek())) {
            if (eos()) return lex_error("Unexpected end of stream in number");
        }
        return new Token(Lexer.NUMBER, this.part());

        // return new Token(Lexer.ERROR, "Could not parse number");
    }

    public Token lex_symbol() {
        while (isAlnum(this.peek())) {
            if (eos()) return lex_error("Unexpected end of stream in symbol");
        }
        return new Token(Lexer.SYMBOL, this.part());
        // return new Token(Lexer.ERROR, "Could not parse number");
    }

    public Token lex_op() {
        while (isOp(this.peek())) {
            if (eos()) return lex_error("Unexpected end of stream in operator");
        }
        return new Token(Lexer.OPERATOR, this.part());
        // return new Token(Lexer.ERROR, "Could not parse number");
    }


    public Token lex_string() {
        char first = this.peek();
        char now  = 0;
        boolean escape = false;
        boolean done   = false;
        while (!done) {
            now     = peek();
            if (eos()) return lex_error("Unexpected end of stream in string");
            escape  = now == '\\';
            done    = (!escape) && now == first;
        }
        return new Token(Lexer.SYMBOL, this.part_noquotes());
        // return new Token(Lexer.ERROR, "Could not parse number");
    }

    public Token lex() {
        while (!eos()) {
            char ch = peep();
            if (isDigit(ch)) {
                return lex_number();
            } else if (isAlpha(ch)) {
                return lex_symbol();
            } else if (ch == '\'' || ch == '"') {
                return lex_string();
            } else if (ch == '\\') {
                skip(2);
                // skip escaped character and ignore it
            } else if (isBlank(ch)) {
                skip();
                // skip whitespace character and ignore it
            } else if (isEol(ch)) {
                return new Token(Lexer.SEPARATOR, String.valueOf(ch));
            } else if (isOp(ch)) {
               return lex_op();
            }
            else {
                return lex_error("Unknown character");
            }
       }
      return null;
    }
  }



    public static class Node {
 /*
 * program       := expression program | EMPTY
 * expression    := value parameter newline
 * parameter     := value parameter | block | EMPTY
 * block         := brace_block | bracket_block | square_block
 * brace_block   := optnewline '{' optnewline program '}'
 * bracket_block := optnewline '(' optnewline program ')'
 * square_block  := optnewline '[' optnewline program ']'
 * newline       := '\n' newline | '\n' | ';'
 * opt_newline   := '\n' | ';' | EMPTY
 * value         := NUMBER || STRING || SYMBOL || OPERATOR
*/
        public final static int PROGRAM       = 1;
        public final static int EXPRESSION    = 2;
        public final static int BLOCK         = 3;
        public final static int BRACE_BLOCK   = 4;
        public final static int BRACKET_BLOCK = 5;
        public final static int SQUARE_BLOCK  = 6;
        public final static int NEWLINE       = 7;
        public final static int OPT_NEWLINE   = 8;
        public final static int VALUE         = 9;
        public final static int NUMBER        = 10;
        public final static int STRING        = 11;
        public final static int SYMBOL        = 12;
        public final static int OPERATOR      = 13;
        public final static int ERROR         = 14;

        Node _parent, _next, _previous;


    }

    public static class Parser  {
        



    }


}
