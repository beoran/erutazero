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
 * nl is '\n' or ';'
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

    public int type()                   { return _type; }

    public boolean typeIs(int type)     { return _type == type;  }

    public boolean typeIn(int[] types) {
      int index = 0;
      for (index = 0 ; index < types.length ; index ++) {
        if (typeIs(types[index])) return true;
      }
      return false;
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
    public final static int OPEN_PAREN    = 9;
    public final static int CLOSE_PAREN   = 10;
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

    static boolean isBracket(char c) {
        return c == '(' || c == ')' || c == '[' || c == ']' || c== '{' || c == '}';
    }

    static boolean isOp(char c) {
        return !(isBlank(c) || isAlnum(c) || isEol(c) || isBracket(c));
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

    Token token(int type, String text) {
        return new Token(type, text);
    }

    Token token(int type, char c) {
        return token(type, String.valueOf(c));
    }

    Token lex_error(String text) {
        return token(Lexer.ERROR, text);
    }


    public Token lex_number() {
        while (isDigit(this.peek())) {
            if (eos()) return lex_error("Unexpected end of stream in number");
        }
        return token(Lexer.NUMBER, this.part());

        // return new Token(Lexer.ERROR, "Could not parse number");
    }

    public Token lex_symbol() {
        while (isAlnum(this.peek())) {
            if (eos()) return lex_error("Unexpected end of stream in symbol");
        }
        return token(Lexer.SYMBOL, this.part());
        // return new Token(Lexer.ERROR, "Could not parse number");
    }

    public Token lex_op() {
        while (isOp(this.peek())) {
            if (eos()) return lex_error("Unexpected end of stream in operator");
        }
        return token(Lexer.OPERATOR, this.part());
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
        return token(Lexer.SYMBOL, this.part_noquotes());
        // return new Token(Lexer.ERROR, "Could not parse number");
    }

    public Token lex_bracket(char c) {
      switch(c) {
        case '(': return token(Lexer.OPEN_PAREN, c);
        case '{': return token(Lexer.OPEN_BRACE, c);
        case '[': return token(Lexer.OPEN_BRACKET, c);
        case ')': return token(Lexer.CLOSE_PAREN, c);
        case '}': return token(Lexer.CLOSE_BRACE, c);
        case ']': return token(Lexer.CLOSE_BRACKET, c);
        default:
          return lex_error("Bracket is not a bracket!");
      }
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
            } else if(isBracket(ch))  {
              return lex_bracket(ch);
            } else if (isBlank(ch)) {
                skip();
                // skip whitespace character and ignore it
            } else if (isEol(ch)) {
                return token(Lexer.SEPARATOR, ch);
            } else if (isOp(ch)) {
               return lex_op();
            } else {
               return lex_error("Unknown character. Should not happen.");
            }
       }
      return null;
    }
  }



    public static class Node {



/*
PROGRAM -> EXPRESSION PROGRAM | .
EXPRESSION -> VALUE PARAMLIST NL | BLOCK .
PARAMLIST -> PARAMETER PARAMLIST | .
PARAMETER -> BLOCK | VALUE .
BLOCK -> do PROGRAM end | ob PROGRAM cb | os PROGRAM cs | oa PROGRAM ca.
NL -> nl | semicolon .
VALUE -> string | number | symbol | operator .
*/

        public final static int PROGRAM       = 1;
        public final static int EXPRESSION    = 2;
        public final static int PARAMLIST     = 3;
        public final static int PARAMETER     = 4;
        public final static int BLOCK         = 5;
        public final static int BRACE_BLOCK   = 6;
        public final static int BRACKET_BLOCK = 7;
        public final static int SQUARE_BLOCK  = 8;
        public final static int NEWLINE       = 9;
        public final static int VALUE         = 10;
        public final static int NUMBER        = 11;
        public final static int STRING        = 12;
        public final static int SYMBOL        = 13;
        public final static int OPERATOR      = 14;
        public final static int ERROR         = 15;

        Node _parent, _next, _previous;
        String _text;
        int _type;

        public Node(Node parent, int type, String text) {
          _parent = parent;
          _next   = _previous = null;
          _type   = type;
          _text   = text;
        }

    }

    public static class Parser  {

      Lexer _lex;
      Node _root, _now;

      public Parser(Lexer lex) {
        _lex  = lex;
        _root = new Node(null, Node.PROGRAM, "<root>");
        _now  = _root;
      }

      Node parse_value(Token tok) {
        int []ok = { Lexer.STRING, Lexer.NUMBER,  Lexer.OPERATOR, Lexer.SYMBOL};
        if (tok.typeIn(ok)) {
          return new Node(_now, Node.VALUE, tok.toString());
        }
        return new Node(_now, Node.ERROR, "Expected value.");
      }

      Node parse() {
        while (!_lex.eos()) {
          parse_value(_lex.lex());
        }
        return _root;
      }



    }


}
