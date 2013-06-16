package com.yp2012g4.vision.test.utils;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;

public class GestureUtils {
  public static void flingRight(final ActivityInstrumentationTestCase2<?> c) {
    final int screenHeight = c.getActivity().getWindowManager().getDefaultDisplay().getHeight();
    final int screenWidth = c.getActivity().getWindowManager().getDefaultDisplay().getWidth();
    TouchUtils.drag(c, screenWidth / 2, screenWidth / 2 - 150, screenHeight / 2, screenHeight / 2, 20);
  }
  
  public static void flingLeft(final ActivityInstrumentationTestCase2<?> c) {
    final int screenHeight = c.getActivity().getWindowManager().getDefaultDisplay().getHeight();
    final int screenWidth = c.getActivity().getWindowManager().getDefaultDisplay().getWidth();
    TouchUtils.drag(c, screenWidth / 2, screenWidth / 2 + 150, screenHeight / 2, screenHeight / 2, 20);
  }
}
