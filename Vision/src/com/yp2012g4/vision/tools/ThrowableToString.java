package com.yp2012g4.vision.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * An utility class to get a string representation of a Throwable
 * 
 * @author Olivier
 * 
 */
public class ThrowableToString {
  /**
   * Convert a Throwable to a string
   * 
   * @param t
   *          - The Throwable
   * @return a string representation of the Throwable
   */
  public static String toString(final Throwable t) {
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw);
    t.printStackTrace(pw);
    return sw.toString();
  }
}
