package jatite;

/**
 * Jatite is a java tine test framework.
 * Is, is a small, one-class testing framework.
 * @author bjorn
 */
public class Is {

  public static interface Test {
    boolean test();
  }

  /**
   * Is.Result is a class that models a test result.
   */
  public static class Result {
    boolean _ok;
    String _text;

    public Result(String text, boolean ok) {
      _ok   = ok;
      _text = text;
    }

    public boolean fail() {
      return !_ok;
    }

    public String text() {
      return _text;
    }
  }

  /** A Report is the reort of running  a test suite.  Not used yet. */
  public static class Report {
    StringBuffer _dots, _errors;
    int _ok, _failed;
    /** Initializes a new empty toStringBuffer. */
    public Report() {
      _ok     = _failed = 0;
      _dots   = new StringBuffer("");
      _errors = new StringBuffer("");
    }

    public int ok()               { return _ok;             }
    public int failed()           { return _failed;         }
    public int total()            { return _ok + _failed;   }
    public String dots()    { return _dots.toString();      }
    public String errors()  { return _errors.toString();    }

    /** Adds a failure to the toStringBuffer.*/
    public void add_fail(Result res) {
      _failed ++;
      _errors.append("Failed: ").append(res.text()).append("\n");
      _dots.append("F");
    }

    /** Adds a success to the toStringBuffer. */
    public void add_ok(Result res) {
      _ok ++;
      _dots.append(".");
    }

    /** Formats the toStringBuffer into a StringBuffer */
    public StringBuffer toStringBuffer() {
      StringBuffer res = new StringBuffer(dots());
      if(failed() == 0) {
        res.append("\nOK! ").append(ok()).append("/").append(total());
      } else {
        res.append("\n").append(errors());
        res.append("FAIL! ").append(ok()).append("/").append(total());
      }
      return res;
    }

    /** Formats the toStringBuffer into a String */
    public String toString() {
      return toStringBuffer().toString();
    }
  }

  /**
   * Is.Suite contains the results of a suite of tests.
   */
  public static class Suite {
    java.util.Vector _results;

    /** Initializes a new empty suite. */
    public Suite() {
      _results = new java.util.Vector(100);
    }

    /** Adds a result to the suite. */
    public Result add(Result res) {
      _results.addElement(res);
      return res;
    }

    /** Adds a result to the suite that has the given text and OK value. */
    public Result add(String text, boolean ok) {
      Result t = new Result(text, ok);
      return this.add(t);
    }

    /** Returns a toStringBuffer about this suite. */
    public Report report() {
      Report rep = new Report();
      int index = 0;
      for (index = 0; index < _results.size(); index ++) {
        Result res = (Result) _results.elementAt(index);
        if (res.fail()) { rep.add_fail(res); } else { rep.add_ok(res); }
      }
      return rep;
    }

    /** Formats the result of this suite to a StringBuffer. */
    public StringBuffer toStringBuffer() {
      return report().toStringBuffer();
    }

    /** Formats the result of this suite to a String. */
    public String toString() {
      return report().toString();
    }
  }

  /**
   * Static functions for ease of use.
   */

  static Suite _suite;
  static {
    _suite = new Suite();
  }

  /** Adds the result of an equality test. */
  public static Result ok(String text, boolean ok) {
    return _suite.add(text, ok);
  }

  /** Adds the result of an equality test. */
  public static Result ok(boolean ok) {
    return _suite.add("Not ok", ok);
  }
  
  /** Adds the result of an equality test. */
  public static Result equal(String text, Object o1, Object o2) {
    return ok(text + ": not equal: " + o1.toString() + "," + o2.toString(), o1.equals(o2));
  }
  
  /** Adds the result of an equality test. */
  public static Result equal(Object o1, Object o2) {
    return equal("Obj not equal", o1, o2);
  }
  
  /** Adds the result of an equality test. */
  public static Result equal(int i1, int i2) {
    return ok("Int not equal", i1 == i2);
  }

  /** Adds the result of an equality test. */
  public static Result equal(String s1, String s2) {
    return ok("Str not equal", s1.equals(s2));
  }

  public static StringBuffer reportBuffer() {
    return _suite.toStringBuffer();
  }

  /** Returns a toStringBuffer on the active suite of tests. */
  public static String reportString() {
    return _suite.toString();
  }

  /** Returns a report on the active suite of tests. */
  public static Report report() {
    return _suite.report();
  }


}
