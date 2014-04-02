package com.yp2012g4.vision.managers;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.util.Log;

import com.yp2012g4.vision.tools.ThrowableToString;

/**
 * Call type container
 * 
 * @author Yaron
 * 
 */
public class CallType {
  private static int MINIMAL_SIZE_FOR_PHONE_NUMBER = 3;
  private static final String TAG = "vision:CallType";
  public final String number;
  public final String name;
  public final Date date;
  
  public CallType(final String num, final String newName, final Date d) {
    number = num;
    name = newName;
    date = d;
  }
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param c
   */
  public CallType(final Context c, final Cursor cur) {
    date = getDateOfCall(cur);
    String num = "", nam = "";
    try {
      num = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.NUMBER));
      if (num.length() < MINIMAL_SIZE_FOR_PHONE_NUMBER)
        num = nam = c.getString(com.yp2012g4.vision.R.string.incoming_call_from_private_number);
      else
        nam = cur.getString(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NAME));
      if (nam == null)
        nam = " ";
    } catch (final Exception e) {
      nam = " ";
    }
    name = nam;
    number = num;
  }
  
  private static Date getDateOfCall(final Cursor cur) {
    Date $;
    try {
      $ = new Date(DateFormat.format("dd/MM/yy hh:mm", cur.getLong(cur.getColumnIndexOrThrow(android.provider.CallLog.Calls.DATE)))
          .toString());
    } catch (final Exception e) {
      $ = new Date();
      Log.e(TAG, "getDateOfCall " + ThrowableToString.toString(e));
    }
    return $;
  }
}
