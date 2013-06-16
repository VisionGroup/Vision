package com.yp2012g4.vision.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceToString {
  public static String toString(final Throwable t) {
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw);
    t.printStackTrace(pw);
    return sw.toString();
  }
}
