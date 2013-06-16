package com.yp2012g4.vision.bug.test;

import junit.framework.TestCase;

/**
 * How to reproduce bug 10 which causes the device to say nothing when given a
 * Hebrew text to read.
 * 
 * @author Amit Yaffe
 * @version 1.1
 * 
 */
public class bugChangeToNoneSupportedLanguage extends TestCase {
  /*
   * Reproducing the bug: Precondition - Default TTS engine does not support
   * Hebrew. 1. Run the application 2. Go to settings screen. 3. Press change
   * locale. 4. Check selected language. 5.Go to Settings screen. 6. Check
   * selected language.
   * 
   * The language selected might be Hebrew at some occasions.
   */
}
