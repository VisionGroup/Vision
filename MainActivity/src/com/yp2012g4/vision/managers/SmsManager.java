package com.yp2012g4.vision.managers;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SmsManager {
  public static ArrayList<SmsType> getIncomingMessages(Context c) {
    ArrayList<SmsType> $ = new ArrayList<SmsType>();
    Uri uri = Uri.parse("content://sms/inbox");
    Cursor cur = c.getContentResolver().query(uri, null, null, null, null);
    if (cur.moveToFirst())
      do
        try {
          $.add(new SmsType(cur, c));
        } catch (IllegalArgumentException e) {
          // Ignore and go next
        }
      while (cur.moveToNext());
    return $;
  }
  
  /**
   * 
   * @param c
   *          context
   * @param ns
   *          name string
   * @param bs
   *          body string
   */
  static public void markMessageRead(Context c, String ns, String bs) {
    Uri uri = Uri.parse("content://sms/inbox");
    Cursor cursor = c.getContentResolver().query(uri, null, null, null, null);
    try {
      while (cursor.moveToNext()) {
        String ad = cursor.getString(cursor.getColumnIndex("address"));
        int n = cursor.getInt(cursor.getColumnIndex("read"));
        if (ad.equals(ns) && n == 0)
          if (cursor.getString(cursor.getColumnIndex("body")).startsWith(bs)) {
            String SmsMessageId = cursor.getString(cursor.getColumnIndex("_id"));
            ContentValues v = new ContentValues();
            v.put("read", Boolean.TRUE);
            c.getContentResolver().update(Uri.parse("content://sms/inbox"), v, "_id=" + SmsMessageId, null);
            return;
          }
      }
    } catch (Exception e) {
      Log.e("Mark Read", "Error in Read: " + e.toString());
    }
  }
  
  /**
   * Get the number of SMS messages the user didn't read yet
   * 
   * @return the unread SMS number
   */
  public static int getUnreadSMS(Context c) {
    final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    Cursor cur = c.getContentResolver().query(SMS_INBOX, null, "read = 0", null, null);
    return cur.getCount();
  }
  
  /**
   * 
   * @param c
   *          context
   * @param ms
   *          Massage body
   * @param ma
   *          Massage address
   */
  public static void deleteSMS(Context c, String ms, String ma) {
    try {
      Uri uri = Uri.parse("content://sms/inbox");
      Cursor cur = c.getContentResolver().query(uri, new String[] { "_id", "thread_id", "address", "person", "date", "body" },
          null, null, null);
      if (cur != null && cur.moveToFirst())
        do {
          String ad = cur.getString(2);
          String bd = cur.getString(5);
          if (ms.equals(bd) && ad.equals(ma))
            c.getContentResolver().delete(Uri.parse("content://sms/" + cur.getLong(0)), null, null);
        } while (cur.moveToNext());
    } catch (Exception e) {
      Log.e("ReadSmsActivity", "Could not delete SMS from inbox: " + e.getMessage());
    }
  }
}
