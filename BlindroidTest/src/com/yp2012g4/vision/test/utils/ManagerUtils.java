package com.yp2012g4.vision.test.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.CallLog.Calls;

import com.yp2012g4.vision.managers.SmsManager;

public class ManagerUtils {
  /**
   * remove all unanswered calls from phone.
   * 
   * @param c
   */
  public static void removeAllUnansweredCalls(final Context c) {
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
  public static void addUnansweredCall(final Context c, final String num) {
    final ContentResolver cr = c.getApplicationContext().getContentResolver();
    final ContentValues values = new ContentValues();
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
   * writes an SMS to the SMS inbox  
   * 
   * @param ctx
   *          Context  
   * @param mobNo
   *          mobile number  
   * @param msg
   *          text of the message  
   */
  public final static void storeMessage(final Context ctx, final String mobNo, final String msg) {
    final ContentValues values = new ContentValues();
    values.put("address", mobNo);
    values.put("body", msg);
    ctx.getContentResolver().insert(Uri.parse(SmsManager.CONTENT_SMS_INBOX), values);
  }
}
