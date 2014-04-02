package com.yp2012g4.vision.bug.test;

/**
 * How to reproduce the following bug: when calculating two decimal numbers (one
 * ends with ".4" and the other with ".7") the result isn't truncated.
 * 
 * @author Amir M.
 * @version 2
 * 
 */
public class bugDecimalNumberCalculation {
  /**
   * Reproducing the bug: 1. Run the application 2. Go to Settings 3. Go to
   * calculator 4. Calculate 5.7+23.4 5. Click on equals button
   * 
   * Expected: 29.1 (and not 29.099999999998)
   */
}
