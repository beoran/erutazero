/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jatite;

/**
 *
 * @author bjorn
 */
public class Jatite {
  public static class Test {
    boolean _ok;
    String _text;

    public Test(String text, boolean ok) {
      _ok   = ok;
      _text = text;
    }

  }


  public static class Suite {
    java.util.Vector _tests;
    

    public Suite() {
      _tests = new java.util.Vector(100);
    }

    public Test add(Test test) {
      _tests.addElement(test);
      return test;
    }

  }

  public static void test(String text, boolean ok) {
    if (ok) {
    } else {
    }
  }


}
