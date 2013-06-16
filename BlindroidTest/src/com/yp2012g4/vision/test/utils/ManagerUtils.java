package com.yp2012g4.vision.test.utils;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.smsReader.DeleteConfirmation;

public class ManagerUtils {
  /**
   * @param confirmDelete
   */
  public static void useDeleteConfirmation(final boolean confirmDelete, final Solo solo, final ActivityInstrumentationTestCase2<?> c) {
    solo.assertCurrentActivity("wrong activity", DeleteConfirmation.class);
    if (confirmDelete)
      GestureUtils.flingRight(c);
    else
      solo.clickOnView(solo.getView(R.id.Delete_Confirmation_Button));
  }
}
