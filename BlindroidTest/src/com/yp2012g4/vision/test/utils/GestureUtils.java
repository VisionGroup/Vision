package com.yp2012g4.vision.test.utils;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.Display;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.smsReader.DeleteConfirmation;

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
  
  /**
   * @param confirmDelete
   *          test
   */
  public static void useDeleteConfirmation(final boolean confirmDelete, final Solo solo, final ActivityInstrumentationTestCase2<?> c) {
    solo.assertCurrentActivity("wrong activity", DeleteConfirmation.class);
    if (confirmDelete)
      GestureUtils.flingRight(c);
    else
      solo.clickOnView(solo.getView(R.id.Delete_Confirmation_Button));
  }
}
