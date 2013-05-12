package com.yp2012g4.vision.bug.test;

/**
 * How to reproduce the following bug: when dialing a number containing "*" or
 * "#" and clicking the dialed number, it says "Hebrew".
 * 
 * @author Maytal
 * @version 1.1
 * 
 */
public class bugHebrewInDialer {
  /*
   * Reproducing the bug: 1. Run the application 2. Go to Contacts Menu. 3. Go
   * to Dialer. 4. Dial 7*. 5. Click the dialed number display.
   * 
   * Expected: speak out "seven asterisk" (and not "Hebrew").
   */
}
