package com.yp2012g4.vision.telephony;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

/**
 * Call type container
 * 
 * @author Yaron
 * 
 */
public class callLog {
  Context c;
  
  public callLog(Context c) {
    this.c = c;
  }
  
  public ArrayList<CallType> dood() {
    String[] strFields = { android.provider.CallLog.Calls.NUMBER,
    /*
     * android.provider.CallLog.Calls.TYPE,
     * android.provider.CallLog.Calls.CACHED_NAME,
     * android.provider.CallLog.Calls.CACHED_NUMBER_TYPE,
     */
    android.provider.CallLog.Calls.DATE };
    String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
    Cursor mCallCursor = c.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, strFields, null, null, null);
    ArrayList<CallType> list = new ArrayList<CallType>();
    if (mCallCursor.moveToFirst()) {
      String num;
      // loop through cursor
      do
        list.add(new CallType(mCallCursor, c));
      while (mCallCursor.moveToNext() && list.size() < 50);
    }
    return list;
  }
}
