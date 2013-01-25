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
public class bug10hebrewstrings extends TestCase {

    /*
     * Reproducing the bug: 1. Run the application 2. Go to SMS reader. 3. Press
     * an SMS which is written in hebrew - should say "Hebrew" or read the
     * hebrew text.
     */

}
