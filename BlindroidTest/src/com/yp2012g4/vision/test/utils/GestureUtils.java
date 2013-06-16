package com.yp2012g4.vision.test.utils;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.Display;

public class GestureUtils {
  public static void flingRight(final ActivityInstrumentationTestCase2<?> c) {
    fling(c, -150);
  }
  
  public static void flingLeft(final ActivityInstrumentationTestCase2<?> c) {
    fling(c, 150);
  }
  
  static Display getDefaultDisplay(final ActivityInstrumentationTestCase2<?> c) {
    return c.getActivity().getWindowManager().getDefaultDisplay();
  }
  
  public static void fling(final ActivityInstrumentationTestCase2<?> c, final int offset) {
    final int screenHeight = getDefaultDisplay(c).getHeight();
    final int screenWidth = getDefaultDisplay(c).getWidth();
    TouchUtils.drag(c, screenWidth / 2, screenWidth / 2 + offset, screenHeight / 2, screenHeight / 2, 20);
  }
}
