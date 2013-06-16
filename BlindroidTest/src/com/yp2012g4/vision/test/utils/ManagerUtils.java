package com.yp2012g4.vision.test.utils;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.smsReader.DeleteConfirmation;

public class ManagerUtils {
  /**
   * remove all unanswered calls from phone.
   * 
   * @param c
   */
  public static void removeAllUnansweredCalls(Context c) {
    try {
      final ContentValues values = new ContentValues();
      values.put(Calls.NEW, Integer.valueOf(0));
      final StringBuilder where = new StringBuilder();
      where.append(Calls.NEW + " = 1");
      where.append(" AND ");
      where.append(Calls.TYPE + " = ?");// + Calls.MISSED_TYPE);
      final int i = c.getContentResolver().update(Calls.CONTENT_URI, values, where.toString(),
          new String[] { Integer.toString(Calls.MISSED_TYPE) });
      System.out.println(i);
    } catch (final Exception e) {
      e.getMessage();
    }
  }
  
  /**
   * add unanswered Call to number
   * 
   * @param c
   *          context.
   * @param num
   *          number of the unanswered call.
   * 
   */
  public static void addUnansweredCall(Context c, String num) {
    ContentResolver cr = c.getApplicationContext().getContentResolver();
    ContentValues values = new ContentValues();
    values.put(CallLog.Calls.NUMBER, num);
    values.put(CallLog.Calls.DATE, Long.valueOf(System.currentTimeMillis()));
    values.put(CallLog.Calls.DURATION, Integer.valueOf(2));
    values.put(CallLog.Calls.TYPE, Integer.valueOf(CallLog.Calls.MISSED_TYPE));
    values.put(CallLog.Calls.NEW, Integer.valueOf(1));
    values.put(CallLog.Calls.CACHED_NAME, "");
    values.put(CallLog.Calls.CACHED_NUMBER_TYPE, Integer.valueOf(0));
    values.put(CallLog.Calls.CACHED_NUMBER_LABEL, "");
    cr.insert(CallLog.Calls.CONTENT_URI, values);
  }
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
